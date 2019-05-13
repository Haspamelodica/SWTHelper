package net.haspamelodica.swt.helper.buffered;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public abstract class UntypedBufferedPaintListener extends GenericBufferedPaintListener<Event> implements Listener
{
	public UntypedBufferedPaintListener()
	{
		super((e, gc) -> e.gc = gc);
	}
	public void handleEvent(Event e)
	{
		bufferedPaintControl(e, e.gc, e.display, e.x, e.y, e.width, e.height);
	}
}