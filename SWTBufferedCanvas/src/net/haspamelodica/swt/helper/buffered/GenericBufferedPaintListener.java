package net.haspamelodica.swt.helper.buffered;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import net.haspamelodica.swt.helper.gcs.GCDefaultConfig;
import net.haspamelodica.swt.helper.gcs.GeneralGC;
import net.haspamelodica.swt.helper.gcs.SWTGC;

public abstract class GenericBufferedPaintListener<E>
{
	private final BiConsumer<E, GC> setGCInEvent;

	private Image			buffer;
	private GC				bufferGC;
	private GeneralGC		bufferGeneralGC;
	private GCDefaultConfig	defaultGCValues;
	private int				bufW	= -1, bufH = -1;

	public GenericBufferedPaintListener(BiConsumer<E, GC> setGCInEvent)
	{
		this.setGCInEvent = setGCInEvent;
	}
	public void bufferedPaintControl(E event, GC gc, Display display, int x, int y, int width, int height)
	{
		recreateBufferIfNecessary(width + x, height + y, display);
		GC widgetGC = gc;
		defaultGCValues.reset(bufferGeneralGC);
		bufferGC.setForeground(widgetGC.getForeground());
		bufferGC.setBackground(widgetGC.getBackground());
		bufferGC.setFont(widgetGC.getFont());
		bufferGC.fillRectangle(x, y, width, height);
		setGCInEvent.accept(event, bufferGC);
		unbufferedPaintControl(event);
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
			defaultGCValues = new GCDefaultConfig(bufferGeneralGC);
			bufW = w;
			bufH = h;
		}
	}
	public abstract void unbufferedPaintControl(E event);
	public void disposeBuffer()
	{
		bufferGeneralGC.disposeThisLayer();
		bufferGC.dispose();
		buffer.dispose();
	}

	public static <E> GenericBufferedPaintListener<E> create(BiConsumer<E, GC> setGCInEvent, Consumer<E> unbuffered)
	{
		return new GenericBufferedPaintListener<E>(setGCInEvent)
		{
			@Override
			public void unbufferedPaintControl(E e)
			{
				unbuffered.accept(e);
			}
		};
	}
}