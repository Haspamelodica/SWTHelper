package net.haspamelodica.swt.helper.zoomablecanvas.helper;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Listener;

import net.haspamelodica.swt.helper.zoomablecanvas.ZoomableCanvas;

public class ZoomableCanvasUserInput
{
	private final ZoomableCanvas	canvas;
	private final Listener			listenerMouseDown;
	private final Listener			listenerMouseUp;
	private final Listener			listenerMouseMove;
	private final Listener			listenerMouseWheel;

	public int		buttonDrag		= 1;
	public int		buttonZoom		= 3;
	public double	zoomMouseDrag	= .1;
	public double	zoomScroll		= 1;

	private double	lastMouseX, lastMouseY;
	private int		button;

	public ZoomableCanvasUserInput(ZoomableCanvas canvas)
	{
		this.canvas = canvas;
		listenerMouseDown = e -> mouseButtonDown(e.button);
		listenerMouseUp = e -> mouseButtonUp();
		listenerMouseMove = e -> mouseMovedTo(e.x, e.y);
		listenerMouseWheel = e -> mouseScrolled(e.count);
		canvas.addListener(SWT.MouseMove, listenerMouseMove);
	}
	public void enableUserInput()
	{
		canvas.addListener(SWT.MouseDown, listenerMouseDown);
		canvas.addListener(SWT.MouseUp, listenerMouseUp);
		canvas.addListener(SWT.MouseWheel, listenerMouseWheel);
	}
	public void disableUserInput()
	{
		canvas.removeListener(SWT.MouseDown, listenerMouseDown);
		canvas.removeListener(SWT.MouseUp, listenerMouseUp);
		canvas.removeListener(SWT.MouseWheel, listenerMouseWheel);
		button = 0;
	}
	private void mouseButtonDown(int button)
	{
		this.button = button;
	}
	private void mouseButtonUp()
	{
		button = 0;
	}
	private void mouseMovedTo(double x, double y)
	{
		if(button == buttonDrag)
			canvas.move(x - lastMouseX, y - lastMouseY);
		else if(button == buttonZoom)
			canvas.zoomSteps((y - lastMouseY) * zoomMouseDrag, canvas.getContentWidth() / 2d,
					canvas.getContentHeight() / 2d);
		lastMouseX = x;
		lastMouseY = y;
	}
	public void mouseScrolled(double steps)
	{
		canvas.zoomSteps(steps * zoomScroll, lastMouseX, lastMouseY);
	}
}