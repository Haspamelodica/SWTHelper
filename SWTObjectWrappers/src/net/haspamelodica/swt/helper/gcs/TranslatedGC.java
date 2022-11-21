package net.haspamelodica.swt.helper.gcs;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GCData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.graphics.Transform;

import net.haspamelodica.swt.helper.swtobjectwrappers.Font;
import net.haspamelodica.swt.helper.swtobjectwrappers.Path;
import net.haspamelodica.swt.helper.swtobjectwrappers.Point;
import net.haspamelodica.swt.helper.swtobjectwrappers.Rectangle;

public class TranslatedGC implements GeneralGC
{
	private final GeneralGC	gc;
	private final double	imgOffX, imgOffY, zoomX, zoomY;

	public TranslatedGC(GeneralGC gc, double x, double y, double zoom, boolean invertOffset)
	{
		this(gc, x, y, zoom, zoom, invertOffset);
	}
	public TranslatedGC(GeneralGC gc, double x, double y, double zoomX, double zoomY, boolean invertOffset)
	{
		this.gc = gc;
		this.zoomX = zoomX;
		this.zoomY = zoomY;
		if(invertOffset)
		{
			this.imgOffX = -x;
			this.imgOffY = -y;
		} else
		{
			this.imgOffX = sx(x);
			this.imgOffY = sy(y);
		}
	}
	public TranslatedGC(GeneralGC gc, Point off)
	{
		this(gc, off.x, off.y);
	}
	public TranslatedGC(GeneralGC gc, double xOff, double yOff)
	{
		this(gc, xOff, yOff, 1, true);
	}

	private double sx(double s)
	{
		return s * zoomX;
	}
	private double sy(double s)
	{
		return s * zoomY;
	}
	private double six(double s)
	{
		return s / zoomX;
	}
	private double siy(double s)
	{
		return s / zoomY;
	}
	private double tx(double x)
	{
		return x * zoomX - imgOffX;
	}
	private double ty(double y)
	{
		return y * zoomY - imgOffY;
	}

	// TODO implement more methods!
	@Override
	public void copyArea(Image image, double x, double y)
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public void copyArea(double srcX, double srcY, double width, double height, double destX, double destY)
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public void copyArea(double srcX, double srcY, double width, double height, double destX, double destY, boolean paint)
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public void drawArc(double x, double y, double width, double height, double startAngle, double arcAngle)
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public void drawFocus(double x, double y, double width, double height)
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public void drawImage(Image image, double x, double y)
	{
		org.eclipse.swt.graphics.Rectangle bounds = image.getBounds();
		gc.drawImage(image, bounds.x, bounds.y, bounds.width, bounds.height, tx(x), ty(y), sx(bounds.width), sy(bounds.height));
	}
	@Override
	public void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, double destX, double destY, double destWidth, double destHeight)
	{
		gc.drawImage(image, srcX, srcY, srcWidth, srcHeight, tx(destX), ty(destY), sx(destWidth), sy(destHeight));
	}
	@Override
	public void drawLine(double x1, double y1, double x2, double y2)
	{
		gc.drawLine(tx(x1), ty(y1), tx(x2), ty(y2));
	}
	@Override
	public void drawOval(double x, double y, double width, double height)
	{
		gc.drawOval(tx(x), ty(y), sx(width), sy(height));
	}
	@Override
	public void drawPath(Path path)
	{
		gc.drawPath(path.translate(imgOffX, imgOffY, zoomX, zoomY));
	}
	@Override
	public void drawPoint(double x, double y)
	{
		gc.drawPoint(tx(x), ty(y));
	}
	@Override
	public void drawPolygon(double[] pointArray)
	{
		double[] pointArrayScaled = new double[pointArray.length];
		for(int i = 0; i < pointArray.length; i += 2)
		{
			pointArrayScaled[i + 0] = tx(pointArray[i + 0]);
			pointArrayScaled[i + 1] = ty(pointArray[i + 1]);
		}
		gc.drawPolygon(pointArrayScaled);
	}
	@Override
	public void drawPolyline(double[] pointArray)
	{
		double[] pointArrayScaled = new double[pointArray.length];
		for(int i = 0; i < pointArray.length; i += 2)
		{
			pointArrayScaled[i + 0] = tx(pointArray[i + 0]);
			pointArrayScaled[i + 1] = ty(pointArray[i + 1]);
		}
		gc.drawPolyline(pointArrayScaled);
	}
	@Override
	public void drawRectangle(double x, double y, double width, double height)
	{
		gc.drawRectangle(tx(x), ty(y), sx(width), sy(height));
	}
	@Override
	public void drawRectangle(Rectangle rect)
	{
		gc.drawRectangle(rect.translate(imgOffX, imgOffY, zoomX, zoomY));
	}
	@Override
	public void drawRoundRectangle(double x, double y, double width, double height, double arcWidth, double arcHeight)
	{
		gc.drawRoundRectangle(tx(x), ty(y), sx(width), sy(height), sx(arcWidth), sy(arcHeight));
	}
	@Override
	public void drawString(String string, double x, double y)
	{
		gc.drawString(string, tx(x), ty(y));
	}
	@Override
	public void drawString(String string, double x, double y, boolean isTransparent)
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public void drawText(String string, double x, double y)
	{
		gc.drawText(string, tx(x), ty(y));
	}
	@Override
	public void drawText(String string, double x, double y, boolean isTransparent)
	{
		gc.drawText(string, tx(x), ty(y), isTransparent);
	}
	@Override
	public void drawText(String string, double x, double y, int flags)
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public void fillArc(double x, double y, double width, double height, double startAngle, double arcAngle)
	{
		gc.fillArc(tx(x), ty(y), sx(width), sy(height), startAngle, arcAngle);
	}
	@Override
	public void fillGradientRectangle(double x, double y, double width, double height, boolean vertical)
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public void fillOval(double x, double y, double width, double height)
	{
		gc.fillOval(tx(x), ty(y), sx(width), sy(height));
	}
	@Override
	public void fillPath(Path path)
	{
		gc.fillPath(path.translate(imgOffX, imgOffY, zoomX, zoomY));
	}
	@Override
	public void fillPolygon(double[] pointArray)
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public void fillRectangle(double x, double y, double width, double height)
	{
		gc.fillRectangle(tx(x), ty(y), sx(width), sy(height));
	}
	@Override
	public void fillRectangle(Rectangle rect)
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public void fillRoundRectangle(double x, double y, double width, double height, double arcWidth, double arcHeight)
	{
		gc.fillRoundRectangle(tx(x), ty(y), sx(width), sy(height), sx(arcWidth), sy(arcHeight));
	}
	@Override
	public double getAdvanceWidth(char ch)
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public boolean getAdvanced()
	{
		return gc.getAdvanced();
	}
	@Override
	public int getAlpha()
	{
		return gc.getAlpha();
	}
	@Override
	public int getAntialias()
	{
		return gc.getAntialias();
	}
	@Override
	public Color getBackground()
	{
		return gc.getBackground();
	}
	@Override
	public Pattern getBackgroundPattern()
	{
		Pattern patternUnscaled = gc.getBackgroundPattern();
		if(patternUnscaled == null)
			return null;
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public double getCharWidth(char ch)
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public Rectangle getClipping()
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public void getClipping(Region region)
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public Device getDevice()
	{
		return gc.getDevice();
	}
	@Override
	public int getFillRule()
	{
		return gc.getFillRule();
	}
	@Override
	public Font getFont()
	{
		//TODO we have to choose here, which isn't optimal.
		// Since most text is rather wide than high, use zoomY.
		return gc.getFont().unscale(zoomY);
	}
	@Override
	public FontMetrics getFontMetrics()
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public Color getForeground()
	{
		return gc.getForeground();
	}
	@Override
	public Pattern getForegroundPattern()
	{
		Pattern patternUnscaled = gc.getForegroundPattern();
		if(patternUnscaled == null)
			return null;
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public GCData getGCData()
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public int getInterpolation()
	{
		return gc.getInterpolation();
	}
	@Override
	public LineAttributes getLineAttributes()
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public int getLineCap()
	{
		return gc.getLineCap();
	}
	@Override
	public double[] getLineDash()
	{
		double[] dashesScaled = gc.getLineDash();
		if(dashesScaled == null)
			return null;
		double[] dashesUnscaled = new double[dashesScaled.length];
		for(int i = 0; i < dashesScaled.length; i ++)
			//TODO we have to choose here, which isn't optimal.
			// Arbitrarily use zoomY.
			dashesUnscaled[i] = sy(dashesScaled[i]);
		return dashesUnscaled;
	}
	@Override
	public int getLineJoin()
	{
		return gc.getLineJoin();
	}
	@Override
	public int getLineStyle()
	{
		return gc.getLineStyle();
	}
	@Override
	public double getLineWidth()
	{
		//TODO we have to choose here, which isn't optimal.
		// Arbitrarily use zoomY.
		return siy(gc.getLineWidth());
	}
	@Override
	public int getStyle()
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public int getTextAntialias()
	{
		return gc.getTextAntialias();
	}
	@Override
	public void getTransform(Transform transform)
	{
		gc.getTransform(transform);
		transform.translate((float) -imgOffX, (float) -imgOffY);
		transform.scale((float) zoomX, (float) zoomY);
	}
	@Override
	public boolean getXORMode()
	{
		return gc.getXORMode();
	}
	@Override
	public boolean isClipped()
	{
		return gc.isClipped();
	}
	@Override
	public boolean isDisposed()
	{
		return gc.isDisposed();
	}
	@Override
	public void setAdvanced(boolean advanced)
	{
		gc.setAdvanced(true);
	}
	@Override
	public void setAntialias(int antialias)
	{
		gc.setAntialias(antialias);
	}
	@Override
	public void setAlpha(int alpha)
	{
		gc.setAlpha(alpha);
	}
	@Override
	public void setBackground(Color color)
	{
		gc.setBackground(color);
	}
	@Override
	public void setBackgroundPattern(Pattern pattern)
	{
		if(pattern == null)
			gc.setBackgroundPattern(null);
		else if(!pattern.equals(getBackgroundPattern()))
			throw new IllegalStateException("unimplemented");
	}
	@Override
	public void setClipping(double x, double y, double width, double height)
	{
		gc.setClipping(tx(x), ty(y), sx(width), sy(height));
	}
	@Override
	public void setClipping(Path path)
	{
		gc.setClipping(path.translate(imgOffX, imgOffY, zoomX, zoomY));
	}
	@Override
	public void setClipping(Rectangle rect)
	{
		if(rect == null)
			gc.setClipping(rect);
		else
			gc.setClipping(tx(rect.x), ty(rect.y), sx(rect.width), sy(rect.height));
	}
	@Override
	public void setClipping(Region region)
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public void setFillRule(int rule)
	{
		gc.setFillRule(rule);
	}
	@Override
	public void setFont(Font font)
	{
		//TODO we have to choose here, which isn't optimal.
		// Since most text is rather wide than high, use zoomY.
		gc.setFont(font.scale(zoomY));
	}
	@Override
	public void setForeground(Color color)
	{
		gc.setForeground(color);
	}
	@Override
	public void setForegroundPattern(Pattern pattern)
	{
		if(pattern == null)
			gc.setForegroundPattern(null);
		else if(!pattern.equals(getForegroundPattern()))
			throw new IllegalStateException("unimplemented");
	}
	@Override
	public void setInterpolation(int interpolation)
	{
		gc.setInterpolation(interpolation);
	}
	@Override
	public void setLineAttributes(LineAttributes attributes)
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public void setLineCap(int cap)
	{
		gc.setLineCap(cap);
	}
	@Override
	public void setLineDash(double[] dashes)
	{
		if(dashes != null)
		{
			double[] dashesScaled = new double[dashes.length];
			for(int i = 0; i < dashes.length; i ++)
				//TODO we have to choose here, which isn't optimal.
				// Arbitrarily use zoomY.
				dashesScaled[i] = sy(dashes[i]);
			gc.setLineDash(dashesScaled);
		} else
			gc.setLineDash(null);
	}
	@Override
	public void setLineJoin(int join)
	{
		gc.setLineJoin(join);
	}
	@Override
	public void setLineStyle(int lineStyle)
	{
		gc.setLineStyle(lineStyle);
	}
	@Override
	public void setLineWidth(double lineWidth)
	{
		//TODO we have to choose here, which isn't optimal.
		// Arbitrarily use zoomY.
		gc.setLineWidth(sy(lineWidth));
	}

	@Override
	@SuppressWarnings("deprecation")
	public void setXORMode(boolean xor)
	{
		gc.setXORMode(xor);
	}
	@Override
	public void setTextAntialias(int antialias)
	{
		gc.setTextAntialias(antialias);
	}
	@Override
	public void setTransform(Transform transform)
	{
		if(transform == null)
			gc.setTransform(null);
		else
		{
			float[] floatsBuf = new float[6];
			transform.getElements(floatsBuf);
			transform.scale((float) (1 / zoomX), (float) (1 / zoomY));
			transform.translate((float) imgOffX, (float) imgOffY);
			gc.setTransform(transform);
			transform.setElements(floatsBuf[0], floatsBuf[1], floatsBuf[2], floatsBuf[3], floatsBuf[4], floatsBuf[5]);
		}
	}
	@Override
	public Point stringExtent(String string)
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public Point textExtent(String string)
	{
		Point textExtent = gc.textExtent(string);
		textExtent.x = six(textExtent.x);
		textExtent.y = siy(textExtent.y);
		return textExtent;
	}
	@Override
	public Point textExtent(String string, int flags)
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public void disposeThisLayer()
	{
		//nothing to do
	}
}