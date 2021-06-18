package net.haspamelodica.swt.helper.zoomablecanvas;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import net.haspamelodica.swt.helper.gcs.ClippingGC;
import net.haspamelodica.swt.helper.gcs.GCConfig;
import net.haspamelodica.swt.helper.gcs.GeneralGC;
import net.haspamelodica.swt.helper.gcs.SWTGC;
import net.haspamelodica.swt.helper.gcs.ScalingImprovingGC;
import net.haspamelodica.swt.helper.gcs.TranslatedGC;
import net.haspamelodica.swt.helper.swtobjectwrappers.Point;
import net.haspamelodica.swt.helper.swtobjectwrappers.Rectangle;

public class ZoomableCanvas extends Canvas
{
	private final List<ZoomedRenderer> zoomedRenderersCorrectOrder;

	private final List<TransformListener> transformListeners;

	private boolean		improveScaling;
	private float		lineDashImprovementFactor;
	protected double	offX, offY, zoom = 1;
	protected int		gW, gH;

	private final AtomicBoolean redrawQueued;

	public ZoomableCanvas(Composite parent, int style)
	{
		this(parent, style, false, -1);
	}
	public ZoomableCanvas(Composite parent, int style, boolean improveScaling, float lineDashImprovementFactor)
	{
		super(parent, style | SWT.DOUBLE_BUFFERED);

		zoomedRenderersCorrectOrder = new ArrayList<>();

		transformListeners = new ArrayList<>();

		this.improveScaling = improveScaling;
		this.lineDashImprovementFactor = lineDashImprovementFactor;

		redrawQueued = new AtomicBoolean(false);

		addListener(SWT.Resize, e -> updateSize());
		addPaintListener(this::render);
	}
	private void render(PaintEvent e)
	{
		redrawQueued.set(false);
		GeneralGC gc = new SWTGC(e.gc);
		GCConfig gcConfig = new GCConfig(gc);
		GeneralGC igc = improveScaling ? new ScalingImprovingGC(gc, lineDashImprovementFactor) : null;
		GeneralGC cgc = new ClippingGC(improveScaling ? igc : gc, 0, 0, gW, gH);
		GeneralGC worldGC = new TranslatedGC(cgc, offX, offY, zoom, true);
		gcConfig.reset(worldGC);
		zoomedRenderersCorrectOrder.forEach(r -> r.render(worldGC));
		worldGC.disposeThisLayer();
		cgc.disposeThisLayer();
		if(igc != null)
			igc.disposeThisLayer();
		gcConfig.reset(gc);
		gc.disposeThisLayer();
		e.gc.setFont(null);
	}
	private void updateSize()
	{
		org.eclipse.swt.graphics.Point size = getSize();
		int bw = getBorderWidth();
		int bw2 = bw + bw;
		gW = size.x - bw2;
		gH = size.y - bw2;
	}
	public int getContentWidth()
	{
		return gW;
	}
	public int getContentHeight()
	{
		return gH;
	}
	public double getOffX()
	{
		return offX;
	}
	public double getOffY()
	{
		return offY;
	}
	public double getZoom()
	{
		return zoom;
	}
	public void move(double x, double y)
	{
		offX += x;
		offY += y;
		transformChanged(true);
	}
	public void zoom(double f, double xCenter, double yCenter)
	{
		zoom *= f;
		offX += (xCenter - this.offX) * (1 - f);
		offY += (yCenter - this.offY) * (1 - f);
		transformChanged(true);
	}
	public void zoomSteps(double steps, double xCenter, double yCenter)
	{
		zoom(Math.pow(1.1, steps), xCenter, yCenter);
	}
	/**
	 * This method does not redraw, update the tile cache or call transform listeners!
	 * Callers manually have to call commitTransform()
	 */
	public void moveTo(double offX, double offY, double zoom)
	{
		this.offX = offX;
		this.offY = offY;
		this.zoom = zoom;
		transformChanged(false);
	}
	private void transformChanged(boolean commit)
	{
		if(zoom < 0)
			zoom = -zoom;
		zoom = clampToNormalValues(zoom, 1);
		offX = clampToNormalValues(offX, 0);
		offY = clampToNormalValues(offY, 0);
		if(commit)
			commitTransform();
	}
	public void commitTransform()
	{
		transformListeners.forEach(l -> l.transformChanged(offX, offY, zoom));
		//		redrawThreadsafe();
		getDisplay().asyncExec(this::redraw);
	}
	private static double clampToNormalValues(double d, double nanReplacement)
	{
		double clamped = d;
		if(Double.isNaN(clamped))
			clamped = nanReplacement;
		if(clamped < -Double.MAX_VALUE)
			clamped = -Double.MAX_VALUE;
		if(clamped > Double.MAX_VALUE)
			clamped = Double.MAX_VALUE;
		return clamped;
	}
	public void redrawThreadsafe()
	{
		redrawThreadsafe(false);
	}
	public void redrawThreadsafe(boolean sync)
	{
		if(redrawQueued.compareAndSet(false, true))
			if(sync)
				getDisplay().syncExec(this::forceRedraw);
			else
				getDisplay().asyncExec(this::forceRedraw);
	}
	@Override
	public void redraw()
	{
		if(redrawQueued.compareAndSet(false, true) && !isDisposed())
			forceRedraw();
	}
	private void forceRedraw()
	{
		if(!isDisposed())
			super.redraw();
	}
	public void addZoomedRenderer(ZoomedRenderer renderer)
	{
		zoomedRenderersCorrectOrder.add(renderer);
	}
	public void removeZoomedRenderer(ZoomedRenderer renderer)
	{
		zoomedRenderersCorrectOrder.remove(renderer);
	}
	public void addTransformListener(TransformListener listener)
	{
		transformListeners.add(listener);
	}
	public void removeTransformListener(TransformListener listener)
	{
		transformListeners.remove(listener);
	}
	public static interface ZoomedRenderer
	{
		public void render(GeneralGC gc);
	}
	public static interface TransformListener
	{
		public void transformChanged(double offX, double offY, double zoom);
	}
	public Point canvasToWorldCoords(org.eclipse.swt.graphics.Point canvasCoords)
	{
		return canvasToWorldCoords(canvasCoords.x, canvasCoords.y);
	}
	public Point canvasToWorldCoords(Point canvasCoords)
	{
		return canvasToWorldCoords(canvasCoords.x, canvasCoords.y);
	}
	public Point canvasToWorldCoords(double x, double y)
	{
		return new Point((x - offX) / zoom, (y - offY) / zoom);
	}
	public Point worldToCanvasCoords(Point worldCoords)
	{
		return worldToCanvasCoords(worldCoords.x, worldCoords.y);
	}
	public Point worldToCanvasCoords(double x, double y)
	{
		return new Point(x * zoom + offX, y * zoom + offY);
	}
	public Rectangle canvasToWorldCoords(org.eclipse.swt.graphics.Rectangle canvasCoords)
	{
		return canvasToWorldCoords(canvasCoords.x, canvasCoords.y, canvasCoords.width, canvasCoords.height);
	}
	public Rectangle canvasToWorldCoords(Rectangle canvasCoords)
	{
		return canvasToWorldCoords(canvasCoords.x, canvasCoords.y, canvasCoords.width, canvasCoords.height);
	}
	public Rectangle canvasToWorldCoords(double x, double y, double width, double height)
	{
		return new Rectangle((x - offX) / zoom, (y - offY) / zoom, width / zoom, height / zoom);
	}
	public Rectangle worldToCanvasCoords(Rectangle worldCoords)
	{
		return worldToCanvasCoords(worldCoords.x, worldCoords.y, worldCoords.width, worldCoords.height);
	}
	public Rectangle worldToCanvasCoords(double x, double y, double width, double height)
	{
		return new Rectangle(x * zoom + offX, y * zoom + offY, width * zoom, height * zoom);
	}
}
