package net.haspamelodica.swt.helper.buffered;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import net.haspamelodica.swt.helper.gcs.GCConfig;
import net.haspamelodica.swt.helper.gcs.SWTGC;

public class BufferedCanvas extends Canvas
{
	private final Display display;

	private final List<Consumer<Event>>					paintListenersCorrectOrder;
	private final Map<PaintListener, Consumer<Event>>	paintListeners;
	private final Map<Listener, Consumer<Event>>		untypedPaintListeners;

	private final AtomicBoolean redrawQueued;

	public BufferedCanvas(Composite parent, int style)
	{
		super(parent, style | SWT.NO_BACKGROUND);

		display = getDisplay();

		paintListenersCorrectOrder = new ArrayList<>();
		paintListeners = new HashMap<>();
		untypedPaintListeners = new HashMap<>();

		redrawQueued = new AtomicBoolean(false);

		super.addListener(SWT.Paint, new UntypedBufferedPaintListener()
		{
			@Override
			public void unbufferedPaintControl(Event e, GCConfig gcConfig)
			{
				redrawQueued.set(false);

				drawBackground(e.gc, e.x, e.y, e.width, e.height);

				SWTGC gc = new SWTGC(e.gc);
				paintListenersCorrectOrder.forEach(l ->
				{
					gcConfig.reset(gc);
					l.accept(e);
				});
				gc.disposeThisLayer();
			}
		});
	}
	public void redrawThreadsafe()
	{
		redrawThreadsafe(false);
	}
	public void redrawThreadsafe(boolean sync)
	{
		if(redrawQueued.compareAndSet(false, true))
			if(sync)
				display.syncExec(this::forceRedraw);
			else
				display.asyncExec(this::forceRedraw);
	}
	@Override
	public void addPaintListener(PaintListener listener)
	{
		Consumer<Event> listenerAsConsumer = e -> listener.paintControl(new PaintEvent(e));
		paintListeners.put(listener, listenerAsConsumer);
		paintListenersCorrectOrder.add(listenerAsConsumer);
	}
	@Override
	public void removePaintListener(PaintListener listener)
	{
		Consumer<Event> listenerAsConsumer = paintListeners.remove(listener);
		paintListenersCorrectOrder.remove(listenerAsConsumer);
	}
	@Override
	public void addListener(int eventType, Listener listener)
	{
		if(eventType == SWT.Paint)
		{
			Consumer<Event> listenerAsConsumer = e -> listener.handleEvent(e);
			untypedPaintListeners.put(listener, listenerAsConsumer);
			paintListenersCorrectOrder.add(listenerAsConsumer);
		} else
			super.addListener(eventType, listener);
	}
	@Override
	public void removeListener(int eventType, Listener listener)
	{
		if(eventType == SWT.Paint)
		{
			Consumer<Event> listenerAsConsumer = untypedPaintListeners.remove(listener);
			paintListenersCorrectOrder.remove(listenerAsConsumer);
		} else
			super.removeListener(eventType, listener);
	}
	@Override
	public void redraw()
	{
		if(redrawQueued.compareAndSet(false, true) && !isDisposed())
			forceRedraw();
	}
	private void forceRedraw()
	{
		super.redraw();
	}
}