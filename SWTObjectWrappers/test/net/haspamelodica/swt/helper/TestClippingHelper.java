package net.haspamelodica.swt.helper;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import net.haspamelodica.swt.helper.ClippingHelper.LineClipResult;

public class TestClippingHelper
{
	private static final int	POINT_SELECT_RADIUS	= 6;
	private static final int	POINT_SELECT_DIAM	= 2 * POINT_SELECT_RADIUS;
	private final Display		display;
	private final Shell			shell;
	private final Canvas		canvas;

	private final Rectangle	clip;
	private final Point		line1;
	private final Point		line2;
	private int				lineWidth	= 10;

	private int		selectedPoint;
	private int		mouseX, mouseY;
	private boolean	mouseDown;

	public TestClippingHelper()
	{
		display = new Display();
		shell = new Shell(display);
		shell.setLayout(new FillLayout());
		canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED);

		clip = new Rectangle(100, 100, 100, 100);
		line1 = new Point(50, 100);
		line2 = new Point(170, 220);
		canvas.addPaintListener(e ->
		{
			e.gc.drawText("Press +/- to change line width (current: " + lineWidth + ")\nDrag points to move", 10, 10);
			e.gc.setLineWidth(lineWidth);
			e.gc.drawLine(line1.x, line1.y, line2.x, line2.y);
			e.gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
			LineClipResult c = ClippingHelper.clipLineRectangleCohenSutherland(line1.x, line1.y, line2.x, line2.y, clip.x, clip.y, clip.x + clip.width, clip.y + clip.height, lineWidth);
			if(c != null)
				e.gc.drawLine((int) c.x1, (int) c.y1, (int) c.x2, (int) c.y2);
			e.gc.setLineWidth(1);
			e.gc.setForeground(display.getSystemColor(SWT.COLOR_GREEN));
			e.gc.drawRectangle(clip);
			Point sel = getSelectedPoint();
			if(sel != null)
			{
				e.gc.setAlpha(127);
				e.gc.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
				e.gc.fillOval(sel.x - POINT_SELECT_RADIUS, sel.y - POINT_SELECT_RADIUS, POINT_SELECT_DIAM, POINT_SELECT_DIAM);
			}
		});
		canvas.addListener(SWT.MouseDown, e -> mouseDown = true);
		canvas.addListener(SWT.MouseUp, e -> mouseDown = false);
		canvas.addListener(SWT.MouseMove, e ->
		{
			if(mouseDown)
			{
				Point oldPos = getSelectedPoint();
				if(oldPos != null)
				{
					setSelectedPoint(oldPos.x + e.x - mouseX, oldPos.y + e.y - mouseY);
					canvas.redraw();
				}
			} else
			{
				int oldSelPoint = selectedPoint;
				int bestDistSqr = POINT_SELECT_RADIUS * POINT_SELECT_RADIUS;
				int bestSelPoint = 0;
				for(selectedPoint = 1; selectedPoint < 7; selectedPoint ++)
				{
					Point pos = getSelectedPoint();
					int dx = pos.x - e.x;
					int dy = pos.y - e.y;
					int distSqr = dx * dx + dy * dy;
					if(distSqr < bestDistSqr)
					{
						bestDistSqr = distSqr;
						bestSelPoint = selectedPoint;
					}
				}
				selectedPoint = bestSelPoint;
				if(selectedPoint != oldSelPoint)
					canvas.redraw();
			}
			mouseX = e.x;
			mouseY = e.y;
		});
		canvas.addListener(SWT.KeyDown, e ->
		{
			if(e.keyCode == '+')
			{
				lineWidth ++;
				canvas.redraw();
			} else if(e.keyCode == '-')
			{
				lineWidth --;
				canvas.redraw();
			}
		});
	}

	private Point getSelectedPoint()
	{
		switch(selectedPoint)
		{
			case 0:
				return null;
			case 1:
				return new Point(line1.x, line1.y);
			case 2:
				return new Point(line2.x, line2.y);
			case 3:
				return new Point(clip.x, clip.y);
			case 4:
				return new Point(clip.x + clip.width, clip.y);
			case 5:
				return new Point(clip.x, clip.y + clip.height);
			case 6:
				return new Point(clip.x + clip.width, clip.y + clip.height);
			default:
				throw new IllegalArgumentException("Unknown selected point: " + selectedPoint);
		}
	}
	private void setSelectedPoint(int x, int y)
	{
		switch(selectedPoint)
		{
			case 0:
				return;
			case 1:
				line1.x = x;
				line1.y = y;
				return;
			case 2:
				line2.x = x;
				line2.y = y;
				return;
			case 3:
				clip.width -= x - clip.x;
				clip.x = x;
				clip.height -= y - clip.y;
				clip.y = y;
				return;
			case 4:
				clip.width = x - clip.x;
				clip.height -= y - clip.y;
				clip.y = y;
				return;
			case 5:
				clip.width -= x - clip.x;
				clip.x = x;
				clip.height = y - clip.y;
				return;
			case 6:
				clip.width = x - clip.x;
				clip.height = y - clip.y;
				return;
			default:
				throw new IllegalArgumentException("Unknown selected point: " + selectedPoint);
		}
	}

	public void run()
	{
		shell.open();
		while(!shell.isDisposed())
			if(!display.readAndDispatch())
				display.sleep();
	}

	public static void main(String[] args)
	{
		new TestClippingHelper().run();
	}
}