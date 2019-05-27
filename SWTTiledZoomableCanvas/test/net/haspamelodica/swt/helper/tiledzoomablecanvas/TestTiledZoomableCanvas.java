package net.haspamelodica.swt.helper.tiledzoomablecanvas;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import net.haspamelodica.swt.helper.swtobjectwrappers.Font;
import net.haspamelodica.swt.helper.zoomablecanvas.helper.ZoomableCanvasUserInput;

public class TestTiledZoomableCanvas
{
	private static final int	WIDTH		= 300, HEIGHT = 100;
	private static final double	FONT_SCALE	= 100;
	private static int			callIndex;

	public static void main(String[] args)
	{
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		TiledZoomableCanvas canvas = new TiledZoomableCanvas(shell, SWT.NO_BACKGROUND);
		canvas.addTileRenderer((gc, t) ->
		{
			Font oldFont = gc.getFont();
			gc.setFont(oldFont.unscale(FONT_SCALE / t.width));
			gc.drawText("Draw call #" + callIndex ++ + "\n" + t.x + '|' + t.y + '\n' + t.width + '|' + t.height, t.x, t.y);
			gc.setFont(oldFont);
			gc.drawRectangle(t.x, t.y, t.width, t.height);
			gc.drawOval(t.x, t.y, t.width, t.height);
			gc.drawText("Test", -10, -10, true);
			gc.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
			gc.setAlpha(100);
			gc.fillRectangle(-10, -10, WIDTH / 2, HEIGHT / 2);
			gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
			gc.fillOval(-2, -2, 4, 4);
			gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
			gc.fillOval(-12, -12, 4, 4);
			gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
			gc.fillOval(-12 + WIDTH / 2, -12 + HEIGHT / 2, 4, 4);
			//			try
			//			{
			//				Thread.sleep(400);
			//			}
			//			catch(InterruptedException ex)
			//			{}
		});
		new ZoomableCanvasUserInput(canvas).enableUserInput();
		canvas.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		canvas.setWorldBounds(-WIDTH, -HEIGHT, 2 * WIDTH, 2 * HEIGHT);
		shell.open();
		while(!shell.isDisposed())
			if(!display.readAndDispatch())
				display.sleep();
		display.dispose();
	}
}