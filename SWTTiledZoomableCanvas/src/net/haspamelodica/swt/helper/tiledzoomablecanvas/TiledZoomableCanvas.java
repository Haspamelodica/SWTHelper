package net.haspamelodica.swt.helper.tiledzoomablecanvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import net.haspamelodica.swt.helper.ClippingHelper;
import net.haspamelodica.swt.helper.ClippingHelper.RectangleClippingResult;
import net.haspamelodica.swt.helper.ImageRegion;
import net.haspamelodica.swt.helper.gcs.ClippingGC;
import net.haspamelodica.swt.helper.gcs.GCConfig;
import net.haspamelodica.swt.helper.gcs.GeneralGC;
import net.haspamelodica.swt.helper.gcs.SWTGC;
import net.haspamelodica.swt.helper.gcs.TranslatedGC;
import net.haspamelodica.swt.helper.swtobjectwrappers.Rectangle;
import net.haspamelodica.swt.helper.tiledzoomablecanvas.tilecachingpolicy.ListTileCachingPolicyAdapter;
import net.haspamelodica.swt.helper.tiledzoomablecanvas.tilecachingpolicy.TileCachingPolicy;
import net.haspamelodica.swt.helper.tiledzoomablecanvas.tilecachingpolicy.lists.DefaultListTileCachingPolicy;
import net.haspamelodica.swt.helper.zoomablecanvas.ZoomableCanvas;

public class TiledZoomableCanvas extends ZoomableCanvas
{
	private static final boolean DEBUG = false;

	private static final int	TILE_CACHE_IMAGES		= 8;
	private static final int	TILES_PER_IMAGE_SIDE	= 8;
	private static final int	TILE_CACHE_SIZE			= TILE_CACHE_IMAGES * TILES_PER_IMAGE_SIDE * TILES_PER_IMAGE_SIDE;
	private static final int	TILE_WIDTH				= 100;

	private final Display						display;
	private final List<Resource>				toDispose;
	private final Color							noWorldColor;
	private final TileCachingPolicy				tileCachingPolicy;
	private final ImageRegion[]					tilePool;
	private int									currentPoolSize;
	private final Image							tileRedrawCacheImage;
	private final GeneralGC						tileRedrawCacheImageGC;
	private final Map<Rectangle, ImageRegion>	cachedTiles;
	private final Set<Rectangle>				unmodifiableCachedTilePositions;
	private final List<Rectangle>				cachedTilesSortedCache;
	private GCConfig							defaultGCValues;

	private final List<GeneralTileRenderer>							tileRenderersCorrectOrder;
	private final Map<PixelBasedTileRenderer, GeneralTileRenderer>	pixelBasedTileRenderers;
	private final Map<TileBasedTileRenderer, GeneralTileRenderer>	tileBasedTileRenderers;

	private double wX1, wY1, wX2, wY2, wW, wH;

	private final Object	tileRedrawStateLock;
	private Rectangle		tileToRedraw;
	private int				tileRedrawState;

	public TiledZoomableCanvas(Composite parent, int style)
	{
		super(parent, style);

		display = getDisplay();

		toDispose = new ArrayList<>();

		noWorldColor = new Color(display, 100, 100, 100);

		tileCachingPolicy = new ListTileCachingPolicyAdapter(new DefaultListTileCachingPolicy(TILE_WIDTH));
		tileCachingPolicy.setCacheSize(TILE_CACHE_SIZE);

		tilePool = new ImageRegion[TILE_CACHE_SIZE];

		currentPoolSize = TILE_CACHE_SIZE;

		tileRedrawCacheImage = new Image(display, TILE_WIDTH, TILE_WIDTH);
		toDispose.add(tileRedrawCacheImage);

		tileRedrawCacheImageGC = new SWTGC(new GC(tileRedrawCacheImage));

		cachedTiles = new HashMap<>();

		unmodifiableCachedTilePositions = Collections.unmodifiableSet(cachedTiles.keySet());

		cachedTilesSortedCache = new ArrayList<>();

		Image currentCacheImage = null;
		int currentCacheImageI = 0;
		GC currentCacheImageGC = null;
		for(int tileIndex = TILE_CACHE_SIZE - 1, tileXIndex = 0, tileYIndex = 0; tileIndex >= 0; tileIndex --)
		{
			if(currentCacheImage == null)
			{
				currentCacheImage = new Image(display, TILE_WIDTH * TILES_PER_IMAGE_SIDE, TILE_WIDTH * TILES_PER_IMAGE_SIDE);
				if(DEBUG)
					openImgShell(currentCacheImage, currentCacheImageI ++);
				toDispose.add(currentCacheImage);
				currentCacheImageGC = new GC(currentCacheImage);
				toDispose.add(currentCacheImageGC);
			}
			ImageRegion tileCache = new ImageRegion(currentCacheImage, currentCacheImageGC, tileXIndex * TILE_WIDTH, tileYIndex * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
			tilePool[tileIndex] = tileCache;
			tileXIndex ++;
			if(tileXIndex == TILES_PER_IMAGE_SIDE)
			{
				tileXIndex = 0;
				tileYIndex ++;
				if(tileYIndex == TILES_PER_IMAGE_SIDE)
				{
					tileYIndex = 0;
					currentCacheImage = null;
				}
			}
		}

		defaultGCValues = new GCConfig(tilePool[0].getGC());

		tileRenderersCorrectOrder = new ArrayList<>();
		pixelBasedTileRenderers = new HashMap<>();
		tileBasedTileRenderers = new HashMap<>();

		tileRedrawStateLock = new Object();

		addListener(SWT.Resize, e -> updateSize());
		addPaintListener(this::renderWorldBorder);
		addZoomedRenderer(this::renderWorld);
		addDisposeListener(e ->
		{
			tileRedrawCacheImageGC.disposeThisLayer();
			toDispose.forEach(Resource::dispose);
			synchronized(cachedTiles)
			{
				cachedTiles.values().forEach(ImageRegion::dispose);
				for(ImageRegion ir : tilePool)
					ir.dispose();
			}
		});
		if(DEBUG)
			addListener(SWT.KeyDown, e ->
			{
				if(e.character == 'i')
					invalidateCache();
			});
	}
	private void renderWorldBorder(PaintEvent e)
	{
		double gX1 = Math.max(0, wX1 * zoom + offX);
		double gY1 = Math.max(0, wY1 * zoom + offY);
		double gX2 = Math.min(gW, wX2 * zoom + offX);
		double gY2 = Math.min(gH, wY2 * zoom + offY);

		GeneralGC gc = new SWTGC(e.gc);
		gc.setBackground(noWorldColor);
		gc.fillRectangle(0, 0, gX1, gY2);
		gc.fillRectangle(0, gY2, gX2, gH - gY2);
		gc.fillRectangle(gX2, gY1, gW - gX2, gH - gY1);
		gc.fillRectangle(gX1, 0, gW - gX1, gY1);

		if(DEBUG)
		{
			GeneralGC worldGC = new TranslatedGC(gc, offX, offY, zoom, true);
			worldGC.disposeThisLayer();
			drawAllTileOutlines(worldGC);
		}

		gc.disposeThisLayer();
	}
	private void renderWorld(GeneralGC gc)
	{
		//if something (like BG color) changes, use this new "config" also for tiles getting drawn from now on
		defaultGCValues = new GCConfig(gc);
		synchronized(cachedTiles)
		{
			cachedTilesSortedCache.addAll(unmodifiableCachedTilePositions);
			cachedTilesSortedCache.removeIf(t -> !t.intersects(wX1, wY1, wW, wH));
			double bestWidth = TILE_WIDTH / zoom;
			cachedTilesSortedCache.sort((a, b) -> (int) Math.signum(Math.abs(b.width - bestWidth) - Math.abs(a.width - bestWidth)));
			for(Rectangle tilePos : cachedTilesSortedCache)
				drawTile(gc, tilePos, cachedTiles.get(tilePos));
			//if(cachedTilesSortedCache.size() > 0)
			//	drawTile(gc, cachedTilesSortedCache.get(cachedTilesSortedCache.size() - 1), cachedTiles.get(cachedTilesSortedCache.get(cachedTilesSortedCache.size() - 1)));
			cachedTilesSortedCache.clear();
		}
	}
	public void updateTileCache()
	{
		freeUnusedTiles();
		queueTileRenderIfNecessary();
	}
	private void updateSize()
	{
		Point size = getSize();
		int bw = getBorderWidth();
		int bw2 = bw + bw;
		gW = size.x - bw2;
		gH = size.y - bw2;
		tileCachingPolicy.setScreenSize(gW, gH);
		updateTileCache();
	}
	public void setWorldBounds(double x, double y, double w, double h)
	{
		wX1 = x;
		wY1 = y;
		wX2 = x + w;
		wY2 = y + h;
		wW = w;
		wH = h;
		tileCachingPolicy.setWorldBounds(x, y, w, h);
		updateTileCache();
		redrawThreadsafe();
	}
	public double getWorldX()
	{
		return wX1;
	}
	public double getWorldY()
	{
		return wY1;
	}
	public double getWorldW()
	{
		return wX2 - wX1;
	}
	public double getWorldH()
	{
		return wY2 - wY1;
	}
	@Override
	public void commitTransform()
	{
		updateTileCache();
		super.commitTransform();
	}
	public void addTileRenderer(TileBasedTileRenderer renderer)
	{
		GeneralTileRenderer generalRenderer = GeneralTileRenderer.create(renderer);
		tileBasedTileRenderers.put(renderer, generalRenderer);
		tileRenderersCorrectOrder.add(generalRenderer);
	}
	public void removeTileRenderer(TileBasedTileRenderer renderer)
	{
		GeneralTileRenderer generalRenderer = tileBasedTileRenderers.remove(renderer);
		tileRenderersCorrectOrder.remove(generalRenderer);
	}
	public void addTileRenderer(PixelBasedTileRenderer renderer)
	{
		GeneralTileRenderer generalRenderer = GeneralTileRenderer.create(renderer);
		pixelBasedTileRenderers.put(renderer, generalRenderer);
		tileRenderersCorrectOrder.add(generalRenderer);
	}
	public void removeTileRenderer(PixelBasedTileRenderer renderer)
	{
		GeneralTileRenderer generalRenderer = pixelBasedTileRenderers.remove(renderer);
		tileRenderersCorrectOrder.remove(generalRenderer);
	}
	private void drawTile(GeneralGC worldGC, Rectangle tilePos, ImageRegion tile)
	{
		RectangleClippingResult c = ClippingHelper.clipRectangleRectangleSrcAsInts(
				0, 0, TILE_WIDTH, TILE_WIDTH,
				tilePos.x, tilePos.y, tilePos.x + tilePos.width, tilePos.y + tilePos.height,
				wX1, wY1, wX2, wY2);
		if(c != null)
			tile.drawTo(worldGC,
					c.srcX1, c.srcY1, c.srcX2 - c.srcX1, c.srcY2 - c.srcY1,
					c.dstX1, c.dstY1, c.dstX2 - c.dstX1, c.dstY2 - c.dstY1);
	}
	private void queueTileRenderIfNecessary()
	{
		if(currentPoolSize != 0)
		{
			Rectangle toRender = tileCachingPolicy.getNextTileToRender(offX, offY, zoom, unmodifiableCachedTilePositions);
			if(toRender != null)
				if(toRender.width != toRender.height)
					throw new IllegalArgumentException("Only square tiles supported at the moment");
				else if(!cachedTiles.containsKey(toRender) && !isDisposed())
				{
					int oldTileRedrawState;
					synchronized(tileRedrawStateLock)
					{
						oldTileRedrawState = tileRedrawState;
						if(tileRedrawState == 0 || tileRedrawState == 1)
						{
							tileRedrawState = 1;
							tileToRedraw = toRender;
						}
					}
					if(oldTileRedrawState == 0)
						display.asyncExec(this::renderTile);
				}
		} else
			System.out.println("Oh no!");
	}
	private void renderTile()
	{
		if(!isDisposed())
		{
			Rectangle toRender;
			synchronized(tileRedrawStateLock)
			{
				toRender = tileToRedraw;
				tileRedrawState = toRender == null ? 0 : 2;
				tileToRedraw = null;
			}
			if(toRender != null)
			{
				GeneralGC untranslatedGC = tileRedrawCacheImageGC;
				ClippingGC cgc = new ClippingGC(untranslatedGC, 0, 0, TILE_WIDTH, TILE_WIDTH);
				TranslatedGC translatedGC = new TranslatedGC(cgc, toRender.x, toRender.y, TILE_WIDTH / toRender.width, false);
				defaultGCValues.reset(translatedGC);

				untranslatedGC.fillRectangle(0, 0, TILE_WIDTH, TILE_WIDTH);

				tileRenderersCorrectOrder.forEach(r -> r.renderTile(untranslatedGC, translatedGC, toRender));
				translatedGC.disposeThisLayer();
				cgc.disposeThisLayer();

				synchronized(tileRedrawStateLock)
				{
					if(tileRedrawState != 3)
						synchronized(cachedTiles)
						{
							ImageRegion tileCache = tilePool[-- currentPoolSize];
							tileCache.getGC().drawImage(tileRedrawCacheImage, 0, 0);
							ImageRegion oldValue = cachedTiles.put(toRender, tileCache);
							if(oldValue != null)
								tilePool[currentPoolSize ++] = oldValue;
						}
					tileRedrawState = 0;
				}
				updateTileCache();
				redraw();
			}
		}
	}
	private void freeUnusedTiles()
	{
		synchronized(cachedTiles)
		{
			freeAll(tileCachingPolicy.getTilesToFree(offX, offY, zoom, unmodifiableCachedTilePositions));
		}
	}
	private void freeAll(Set<Rectangle> toFree)
	{
		synchronized(cachedTiles)
		{
			if(!toFree.isEmpty())
				toFree.forEach(this::freeWithoutRedraw);
		}
	}
	private void freeWithoutRedraw(Rectangle tilePos)
	{
		synchronized(cachedTiles)
		{
			ImageRegion tileCache = cachedTiles.remove(tilePos);
			if(tileCache != null)
				tilePool[currentPoolSize ++] = tileCache;
		}
	}
	public void invalidateCache()
	{
		synchronized(tileRedrawStateLock)
		{
			tileToRedraw = null;
			if(tileRedrawState == 2)
				tileRedrawState = 3;
		}
		synchronized(cachedTiles)
		{
			if(!cachedTiles.isEmpty())
				for(ImageRegion tileCache : cachedTiles.values())
					tilePool[currentPoolSize ++] = tileCache;
			cachedTiles.clear();
		}
		updateTileCache();
		redrawThreadsafe();
	}
	private static interface GeneralTileRenderer
	{
		public void renderTile(GeneralGC untranslatedGC, GeneralGC translatedGC, Rectangle tilePos);
		static GeneralTileRenderer create(PixelBasedTileRenderer renderer)
		{
			return new GeneralTileRenderer()
			{
				@Override
				public void renderTile(GeneralGC untranslatedGC, GeneralGC translatedGC, Rectangle tilePos)
				{
					Color oldForeground = untranslatedGC.getForeground();
					int oldAlpha = untranslatedGC.getAlpha();

					double pxWidthX = TILE_WIDTH / tilePos.width;
					double pxWidthY = TILE_WIDTH / tilePos.height;

					int xDst, yDst;
					double xImg, yImg;
					for(xDst = 0, xImg = tilePos.x; xDst < TILE_WIDTH; xDst ++, xImg += pxWidthX)
						for(yDst = 0, yImg = tilePos.y; yDst < TILE_WIDTH; yDst ++, yImg += pxWidthY)
						{
							Color col = renderer.getColorAt(xImg, yImg, pxWidthX, pxWidthY);
							if(col != null)
							{
								untranslatedGC.setForeground(col);
								untranslatedGC.setAlpha(renderer.getAlphaAt(xImg, yImg, pxWidthX, pxWidthY));
								untranslatedGC.drawPoint(xDst, yDst);
							}
						}
					untranslatedGC.setForeground(oldForeground);
					untranslatedGC.setAlpha(oldAlpha);
				}
			};
		}
		static GeneralTileRenderer create(TileBasedTileRenderer renderer)
		{
			return new GeneralTileRenderer()
			{
				@Override
				public void renderTile(GeneralGC untranslatedGC, GeneralGC translatedGC, Rectangle tilePos)
				{
					renderer.renderTile(translatedGC, tilePos);
				}
			};
		}
	}
	public static interface PixelBasedTileRenderer
	{
		@SuppressWarnings("unused")
		public default int getAlphaAt(double x, double y, double pxWidthX, double pxWidthY)
		{
			return 255;
		}
		public Color getColorAt(double x, double y, double pxWidthX, double pxWidthY);
	}
	public static interface TileBasedTileRenderer
	{
		public void renderTile(GeneralGC gc, Rectangle tilePos);
	}

	//debug code
	private void drawAllTileOutlines(GeneralGC gc)
	{
		for(Rectangle tilePos : cachedTiles.keySet())
			gc.drawRectangle(tilePos.x, tilePos.y, tilePos.width, tilePos.height);
	}
	private void openImgShell(Image img, int imgI)
	{
		Shell imgShell = new Shell(display);
		imgShell.setText("Cache img #" + imgI);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		imgShell.setLayout(layout);
		Canvas drawImg = new Canvas(imgShell, SWT.NONE);
		drawImg.setLayoutData(new GridData(TILES_PER_IMAGE_SIDE * TILE_WIDTH, TILES_PER_IMAGE_SIDE * TILE_WIDTH));
		drawImg.addPaintListener(e -> e.gc.drawImage(img, 0, 0));
		addPaintListener(e -> drawImg.redraw());
		imgShell.pack();
		imgShell.open();
	}
}