package net.haspamelodica.swt.helper.zoomablecanvas;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

import net.haspamelodica.swt.helper.buffered.BufferedCanvas;
import net.haspamelodica.swt.helper.gcs.GCDefaultConfig;
import net.haspamelodica.swt.helper.gcs.GeneralGC;
import net.haspamelodica.swt.helper.gcs.SWTGC;
import net.haspamelodica.swt.helper.gcs.TranslatedGC;

public class ZoomableCanvas extends BufferedCanvas
{
	private final List<ZoomedRenderer> zoomedRenderersCorrectOrder;

	private final List<TransformListener> transformListeners;

	protected double	offX, offY, zoom = 1;
	protected int		gW, gH;

	public ZoomableCanvas(Composite parent, int style)
	{
		super(parent, style);

		GC gc = new GC(this);
		gc.dispose();

		zoomedRenderersCorrectOrder = new ArrayList<>();

		transformListeners = new ArrayList<>();

		addListener(SWT.Resize, e -> updateSize());
		addPaintListener(this::render);
	}
	private void render(PaintEvent e)
	{
		GeneralGC gc = new SWTGC(e.gc);
		GCDefaultConfig gcConfig = new GCDefaultConfig(gc);
		GeneralGC worldGC = new TranslatedGC(gc, zoom, offX, offY, true);
		gcConfig.reset(worldGC);
		zoomedRenderersCorrectOrder.forEach(r -> r.render(worldGC));
		worldGC.disposeThisLayer();
		gcConfig.reset(gc);
		gc.disposeThisLayer();
	}
	private void updateSize()
	{
		Point size = getSize();
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
		redrawThreadsafe();
	}
	private double clampToNormalValues(double d, double nanReplacement)
	{
		if(Double.isNaN(d))
			d = nanReplacement;
		if(d < -Double.MAX_VALUE)
			d = -Double.MAX_VALUE;
		if(d > Double.MAX_VALUE)
			d = Double.MAX_VALUE;
		return d;
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
}