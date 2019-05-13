package net.haspamelodica.swt.helper.tiledzoomablecanvas.helper;

import net.haspamelodica.swt.helper.tiledzoomablecanvas.TiledZoomableCanvas;

public class SmoothZoomAdjuster
{
	private SmoothZoomAdjuster()
	{}
	public static void resetZoom(TiledZoomableCanvas canvas, double marginPercentage, long animationTime, int waitPerRedraw) throws InterruptedException
	{
		int sx = canvas.getContentWidth();
		int sy = canvas.getContentHeight();

		double startZoom = canvas.getZoom();
		double startOffX = canvas.getOffX();
		double startOffY = canvas.getOffY();

		double targetZoom = (1 - marginPercentage) * Math.min(sx / canvas.getWorldW(), sy / canvas.getWorldH());
		double targetOffX = (sx - (canvas.getWorldX() * 2 + canvas.getWorldW()) * targetZoom) / 2;
		double targetOffY = (sy - (canvas.getWorldY() * 2 + canvas.getWorldH()) * targetZoom) / 2;

		long t = 0;
		long lastTime = System.currentTimeMillis();
		while(t < animationTime)
		{
			double newZoom = smootherstep(0, animationTime, t, startZoom, targetZoom);
			double newOffX = smootherstep(0, animationTime, t, startOffX, targetOffX);
			double newOffY = smootherstep(0, animationTime, t, startOffY, targetOffY);
			canvas.moveTo(newOffX, newOffY, newZoom);
			canvas.redrawThreadsafe(true);
			long now = System.currentTimeMillis();
			long delta = now - lastTime;
			t += delta;
			lastTime = now;
			long toWait = waitPerRedraw - delta;
			if(toWait > 0)
				Thread.sleep(toWait);
		}
		canvas.moveTo(targetOffX, targetOffY, targetZoom);
		canvas.commitTransform();
	}
	private static double smootherstep(double xMin, double xMax, double x, double rMin, double rMax)
	{
		// Scale, and clamp x to 0..1 range
		x = clamp((x - xMin) / (xMax - xMin), 0.0, 1.0);
		// Evaluate polynomial
		return rMin + (rMax - rMin) * x * x * x * (x * (x * 6 - 15) + 10);
	}

	private static double clamp(double x, double lowerlimit, double upperlimit)
	{
		if(x < lowerlimit)
			return lowerlimit;
		if(x > upperlimit)
			return upperlimit;
		return x;
	}
}