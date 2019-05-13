package net.haspamelodica.swt.helper.buffered;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;

public abstract class BufferedPaintListener extends GenericBufferedPaintListener<PaintEvent> implements PaintListener
{
	public BufferedPaintListener()
	{
		super((e, gc) -> e.gc = gc);
	}

	public void paintControl(PaintEvent e)
	{
		bufferedPaintControl(e, e.gc, e.display, e.x, e.y, e.width, e.height);
	}
}