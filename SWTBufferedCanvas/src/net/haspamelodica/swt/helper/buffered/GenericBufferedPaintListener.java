package net.haspamelodica.swt.helper.buffered;

import java.util.function.BiConsumer;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import net.haspamelodica.swt.helper.gcs.GCDefaultConfig;
import net.haspamelodica.swt.helper.gcs.GeneralGC;
import net.haspamelodica.swt.helper.gcs.SWTGC;

public abstract class GenericBufferedPaintListener<E>
{
	private final BiConsumer<E, GC> setGCInEvent;

	private Image		buffer;
	private GC			bufferGC;
	private GeneralGC	bufferGeneralGC;
	private int			bufW	= -1, bufH = -1;

	public GenericBufferedPaintListener(BiConsumer<E, GC> setGCInEvent)
	{
		this.setGCInEvent = setGCInEvent;
	}
	public void bufferedPaintControl(E event, GC gc, Display display, int x, int y, int width, int height)
	{
		recreateBufferIfNecessary(width + x, height + y, display);
		GC widgetGC = gc;
		GCDefaultConfig gcConfig = new GCDefaultConfig(gc);
		bufferGC.fillRectangle(x, y, width, height);
		setGCInEvent.accept(event, bufferGC);
		unbufferedPaintControl(event, gcConfig);
		setGCInEvent.accept(event, widgetGC);
		widgetGC.drawImage(buffer, 0, 0);
	}
	private void recreateBufferIfNecessary(int w, int h, Display d)
	{
		if(w > bufW || h > bufH)
		{
			if(buffer != null)
				disposeBuffer();
			buffer = new Image(d, w, h);
			bufferGC = new GC(buffer);
			bufferGeneralGC = new SWTGC(bufferGC);
			bufW = w;
			bufH = h;
		}
	}
	public abstract void unbufferedPaintControl(E event, GCDefaultConfig gcConfig);
	public void disposeBuffer()
	{
		bufferGeneralGC.disposeThisLayer();
		bufferGC.dispose();
		buffer.dispose();
	}

	public static <E> GenericBufferedPaintListener<E> create(BiConsumer<E, GC> setGCInEvent, BiConsumer<E, GCDefaultConfig> unbuffered)
	{
		return new GenericBufferedPaintListener<E>(setGCInEvent)
		{
			@Override
			public void unbufferedPaintControl(E e, GCDefaultConfig gcConfig)
			{
				unbuffered.accept(e, gcConfig);
			}
		};
	}
}