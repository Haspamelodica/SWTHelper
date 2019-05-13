package net.haspamelodica.swt.helper.zoomablecanvas.helper;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;

import net.haspamelodica.swt.helper.gcs.GeneralGC;
import net.haspamelodica.swt.helper.gcs.SWTGC;
import net.haspamelodica.swt.helper.zoomablecanvas.ZoomableCanvas;

public class ZoomableCanvasOverlay
{
	private final int[]	ONE_TWO_FIVE_TEN	= {1, 2, 5, 10};
	public double		TARGET_SCALE_WIDTH	= 100;
	public double		scaleXOffset		= 30;
	public double		scaleYOffset		= 10;
	public double		scaleVBarTop		= 15;
	public double		scaleVBarBottom		= 5;
	public double		spacingScaleLabel	= 3;
	public double		scaleLabelXOffset	= 3;

	public double	mousePosXOffset	= 30;
	public double	mousePosYOffset	= 10;

	private final ZoomableCanvas canvas;

	private final PaintListener		scaleRenderer;
	private final PaintListener		mousePosRenderer;
	private final MouseMoveListener	mouseMoveListener;
	private int						mouseX, mouseY;

	public ZoomableCanvasOverlay(ZoomableCanvas canvas)
	{
		this.canvas = canvas;
		scaleRenderer = this::renderScale;
		mousePosRenderer = this::renderMousePos;
		mouseMoveListener = this::mouseMoved;
	}
	public void enableScale()
	{
		canvas.addPaintListener(scaleRenderer);
		canvas.redraw();
	}
	public void disableScale()
	{
		canvas.removePaintListener(scaleRenderer);
		canvas.redraw();
	}
	private void renderScale(PaintEvent e)
	{
		GeneralGC gc = new SWTGC(e.gc);
		Point size = canvas.getSize();
		int bw = canvas.getBorderWidth();
		double sx = size.x - bw - bw;
		double sy = size.y - bw - bw;
		double scaleWidth = getBestScaleWidth();
		double scaleWidthPx = scaleWidth * canvas.getZoom();
		double scaleR = sx - scaleXOffset;
		double scaleY = sy - scaleYOffset;
		double scaleL = scaleR - scaleWidthPx;
		double scaleT = scaleY - scaleVBarTop;
		double scaleB = scaleY + scaleVBarBottom;
		gc.drawLine(scaleR, scaleY, scaleL, scaleY);
		gc.drawLine(scaleR, scaleB, scaleR, scaleT);
		gc.drawLine(scaleL, scaleB, scaleL, scaleT);
		String label = scaleWidth + " m";
		net.haspamelodica.swt.helper.swtobjectwrappers.Point textExtent = gc.textExtent(label);
		gc.drawText(label, scaleR - textExtent.x - scaleLabelXOffset, scaleY - textExtent.y - spacingScaleLabel, true);
		gc.disposeThisLayer();
	}
	private double getBestScaleWidth()
	{
		double exactTargetScaleWidth = TARGET_SCALE_WIDTH / canvas.getZoom();
		double roundedDownToPowsOf10 = Math.pow(10, (int) Math.floor(Math.log10(exactTargetScaleWidth)));
		int bestMultiplier = -1;
		double bestMultiplierDiff = Double.MAX_VALUE;
		for(int mul : ONE_TWO_FIVE_TEN)
		{
			double diff = Math.abs(exactTargetScaleWidth - roundedDownToPowsOf10 * mul);
			if(diff < bestMultiplierDiff)
			{
				bestMultiplier = mul;
				bestMultiplierDiff = diff;
			}
		}
		return roundedDownToPowsOf10 * bestMultiplier;
	}
	public void enableMousePos()
	{
		canvas.addPaintListener(mousePosRenderer);
		canvas.addMouseMoveListener(mouseMoveListener);
		canvas.redraw();
	}
	public void disableMousePos()
	{
		canvas.removePaintListener(mousePosRenderer);
		canvas.removeMouseMoveListener(mouseMoveListener);
		canvas.redraw();
	}
	private void renderMousePos(PaintEvent e)
	{
		GeneralGC gc = new SWTGC(e.gc);
		double offX = canvas.getOffX();
		double offY = canvas.getOffY();
		double zoom = canvas.getZoom();
		gc.drawText((mouseX - offX) / zoom + "\n" + (mouseY - offY) / zoom, mousePosXOffset, mousePosYOffset, true);
		gc.disposeThisLayer();
	}
	private void mouseMoved(MouseEvent e)
	{
		mouseX = e.x;
		mouseY = e.y;
		canvas.redraw();
	}
}