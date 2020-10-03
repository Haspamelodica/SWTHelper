package net.haspamelodica.swt.helper;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import net.haspamelodica.swt.helper.ClippingHelper.RectangleClippingResult;

public class TestClippingHelper
{
	private final static int	xC			= 120, yC = 120, wC = 500, hC = 100;
	private final static int	DRAG_MARGIN	= 10;
	private final static int	CO			= -100;
	private static int			x			= 10, y = 10, w = 300, h = 150;
	private static int			draggingMode;
	private static int			lastMouseX, lastMouseY;
	private static boolean		srcMode;

	private static Canvas c;

	public static void main(String[] args)
	{
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		c = new Canvas(shell, SWT.BORDER | SWT.DOUBLE_BUFFERED);
		c.addPaintListener(e ->
		{
			c.redraw();
			GC gc = e.gc;
			gc.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
			gc.fillRectangle(xC, yC, wC, hC);
			gc.setAlpha(127);
			RectangleClippingResult c;
			if(srcMode)
			{
				gc.drawRectangle(0, 0, 20, 20);
				c = ClippingHelper.clipRectangleRectangleSrcAsInts(0, 0, 20, 20,
						x + CO, y + CO, x + CO + w, y + CO + h,
						xC + CO, yC + CO, xC + CO + wC, yC + CO + hC);
			} else
			{
				gc.drawRectangle(x / 2, y / 2, w / 2, h / 2);
				c = ClippingHelper.clipRectangleRectangleSrcAsInts(x / 2, y / 2, (x + w) / 2, (y + h) / 2,
						x + CO, y + CO, x + CO + w, y + CO + h,
						xC + CO, yC + CO, xC + CO + wC, yC + CO + hC);
			}
			if(c != null)
			{
				gc.setBackground(display.getSystemColor(SWT.COLOR_GRAY));
				gc.fillRectangle(c.srcX1, c.srcY1, c.srcX2 - c.srcX1, c.srcY2 - c.srcY1);
				if(srcMode || Math.sin(System.currentTimeMillis() * Math.PI / 1000) >= 0)
				{
					gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
					gc.fillRectangle((int) c.dstX1 - CO, (int) c.dstY1 - CO, (int) (c.dstX2 - c.dstX1), (int) (c.dstY2 - c.dstY1));
				}
			}
			gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
			gc.fillRectangle(x, y, w, h);
		});
		c.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		c.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		c.addListener(SWT.MouseDown, TestClippingHelper::mouseDown);
		c.addListener(SWT.MouseUp, TestClippingHelper::mouseUp);
		c.addListener(SWT.MouseMove, TestClippingHelper::mouseMove);
		c.addListener(SWT.KeyDown, e -> srcMode = !srcMode);
		shell.open();
		while(!shell.isDisposed())
			if(!display.readAndDispatch())
				display.sleep();
	}

	private static void mouseDown(Event e)
	{
		if(e.button != 1)
			return;
		if(Math.abs(e.x - x) < DRAG_MARGIN && Math.abs(e.y - y) < DRAG_MARGIN)
			draggingMode = 1;
		else if(Math.abs(e.x - x - w) < DRAG_MARGIN && Math.abs(e.y - y) < DRAG_MARGIN)
			draggingMode = 2;
		else if(Math.abs(e.x - x) < DRAG_MARGIN && Math.abs(e.y - y - h) < DRAG_MARGIN)
			draggingMode = 3;
		else if(Math.abs(e.x - x - w) < DRAG_MARGIN && Math.abs(e.y - y - h) < DRAG_MARGIN)
			draggingMode = 4;
		else
			draggingMode = 5;
	}

	private static void mouseUp(Event e)
	{
		if(e.button == 1)
			draggingMode = 0;
	}

	private static void mouseMove(Event e)
	{
		int dx = e.x - lastMouseX;
		int dy = e.y - lastMouseY;
		switch(draggingMode)
		{
			case 1:
				x += dx;
				w -= dx;
				y += dy;
				h -= dy;
				break;
			case 2:
				w += dx;
				y += dy;
				h -= dy;
				break;
			case 3:
				x += dx;
				w -= dx;
				h += dy;
				break;
			case 4:
				w += dx;
				h += dy;
				break;
			case 5:
				x += dx;
				y += dy;
				break;
			default:
				throw new IllegalStateException("Illegal dragging mode: " + draggingMode);
		}
		lastMouseX = e.x;
		lastMouseY = e.y;
		c.redraw();
	}
}