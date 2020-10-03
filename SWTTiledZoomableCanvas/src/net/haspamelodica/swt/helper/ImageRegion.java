package net.haspamelodica.swt.helper;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import net.haspamelodica.swt.helper.gcs.ClippingGC;
import net.haspamelodica.swt.helper.gcs.GeneralGC;
import net.haspamelodica.swt.helper.gcs.SWTGC;
import net.haspamelodica.swt.helper.gcs.TranslatedGC;

public class ImageRegion
{
	private final Image		image;
	private final int		xOff, yOff;
	private final GeneralGC	translatedGC;
	private final GeneralGC	toDispose1, toDispose2;

	public ImageRegion(Image image, GC gc, int x, int y, double w, double h)
	{
		this.image = image;
		this.xOff = x;
		this.yOff = y;
		toDispose1 = new SWTGC(gc);
		toDispose2 = new ClippingGC(toDispose1, x, y, w, h);
		this.translatedGC = new TranslatedGC(toDispose2, x, y);
	}

	public void drawTo(GeneralGC gc, int srcX, int srcY, int srcWidth, int srcHeight, double destX, double destY, double destWidth, double destHeight)
	{
		gc.drawImage(image,
				srcX + xOff, srcY + yOff, srcWidth, srcHeight,
				destX, destY, destWidth, destHeight);
	}
	public GeneralGC getGC()
	{
		return translatedGC;
	}
	public void dispose()
	{
		translatedGC.disposeThisLayer();
		toDispose2.disposeThisLayer();
		toDispose1.disposeThisLayer();
	}
}