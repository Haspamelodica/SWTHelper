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
	private final double	imgOffX, imgOffY, zoom;

	public TranslatedGC(GeneralGC gc, double x, double y, double zoom, boolean invertOffset)
	{
		this.gc = gc;
		this.zoom = zoom;
		if(invertOffset)
		{
			this.imgOffX = -x;
			this.imgOffY = -y;
		} else
		{
			this.imgOffX = s(x);
			this.imgOffY = s(y);
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

	private double s(double s)
	{
		return s * zoom;
	}
	private double si(double s)
	{
		return s / zoom;
	}
	private double sx(double x)
	{
		return x * zoom - imgOffX;
	}
	private double sy(double y)
	{
		return y * zoom - imgOffY;
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
		gc.drawImage(image, bounds.x, bounds.y, bounds.width, bounds.height, sx(x), sy(y), s(bounds.width), s(bounds.height));
	}
	@Override
	public void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, double destX, double destY, double destWidth, double destHeight)
	{
		gc.drawImage(image, srcX, srcY, srcWidth, srcHeight, sx(destX), sy(destY), s(destWidth), s(destHeight));
	}
	@Override
	public void drawLine(double x1, double y1, double x2, double y2)
	{
		gc.drawLine(sx(x1), sy(y1), sx(x2), sy(y2));
	}
	@Override
	public void drawOval(double x, double y, double width, double height)
	{
		gc.drawOval(sx(x), sy(y), s(width), s(height));
	}
	@Override
	public void drawPath(Path path)
	{
		gc.drawPath(path.translate(imgOffX, imgOffY, zoom));
	}
	@Override
	public void drawPoint(double x, double y)
	{
		gc.drawPoint(sx(x), sy(y));
	}
	@Override
	public void drawPolygon(double[] pointArray)
	{
		double[] pointArrayScaled = new double[pointArray.length];
		for(int i = 0; i < pointArray.length; i += 2)
		{
			pointArrayScaled[i + 0] = sx(pointArray[i + 0]);
			pointArrayScaled[i + 1] = sy(pointArray[i + 1]);
		}
		gc.drawPolygon(pointArrayScaled);
	}
	@Override
	public void drawPolyline(double[] pointArray)
	{
		double[] pointArrayScaled = new double[pointArray.length];
		for(int i = 0; i < pointArray.length; i += 2)
		{
			pointArrayScaled[i + 0] = sx(pointArray[i + 0]);
			pointArrayScaled[i + 1] = sy(pointArray[i + 1]);
		}
		gc.drawPolyline(pointArrayScaled);
	}
	@Override
	public void drawRectangle(double x, double y, double width, double height)
	{
		gc.drawRectangle(sx(x), sy(y), s(width), s(height));
	}
	@Override
	public void drawRectangle(Rectangle rect)
	{
		gc.drawRectangle(rect.translate(imgOffX, imgOffY, zoom));
	}
	@Override
	public void drawRoundRectangle(double x, double y, double width, double height, double arcWidth, double arcHeight)
	{
		gc.drawRoundRectangle(sx(x), sy(y), s(width), s(height), s(arcWidth), s(arcHeight));
	}
	@Override
	public void drawString(String string, double x, double y)
	{
		gc.drawString(string, sx(x), sy(y));
	}
	@Override
	public void drawString(String string, double x, double y, boolean isTransparent)
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public void drawText(String string, double x, double y)
	{
		gc.drawText(string, sx(x), sy(y));
	}
	@Override
	public void drawText(String string, double x, double y, boolean isTransparent)
	{
		gc.drawText(string, sx(x), sy(y), isTransparent);
	}
	@Override
	public void drawText(String string, double x, double y, int flags)
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public void fillArc(double x, double y, double width, double height, double startAngle, double arcAngle)
	{
		gc.fillArc(sx(x), sy(y), s(width), s(height), startAngle, arcAngle);
	}
	@Override
	public void fillGradientRectangle(double x, double y, double width, double height, boolean vertical)
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public void fillOval(double x, double y, double width, double height)
	{
		gc.fillOval(sx(x), sy(y), s(width), s(height));
	}
	@Override
	public void fillPath(Path path)
	{
		gc.fillPath(path.translate(imgOffX, imgOffY, zoom));
	}
	@Override
	public void fillPolygon(double[] pointArray)
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public void fillRectangle(double x, double y, double width, double height)
	{
		gc.fillRectangle(sx(x), sy(y), s(width), s(height));
	}
	@Override
	public void fillRectangle(Rectangle rect)
	{
		throw new IllegalStateException("unimplemented");
	}
	@Override
	public void fillRoundRectangle(double x, double y, double width, double height, double arcWidth, double arcHeight)
	{
		gc.fillRoundRectangle(sx(x), sy(y), s(width), s(height), s(arcWidth), s(arcHeight));
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
		return gc.getFont().unscale(zoom);
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
			dashesUnscaled[i] = s(dashesScaled[i]);
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
		return si(gc.getLineWidth());
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
		transform.scale((float) zoom, (float) zoom);
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
		gc.setClipping(sx(x), sy(y), s(width), s(height));
	}
	@Override
	public void setClipping(Path path)
	{
		gc.setClipping(path.translate(imgOffX, imgOffY, zoom));
	}
	@Override
	public void setClipping(Rectangle rect)
	{
		if(rect == null)
			gc.setClipping(rect);
		else
			gc.setClipping(sx(rect.x), sy(rect.y), s(rect.width), s(rect.height));
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
		gc.setFont(font.scale(zoom));
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
				dashesScaled[i] = s(dashes[i]);
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
		gc.setLineWidth(s(lineWidth));
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
			transform.scale((float) (1 / zoom), (float) (1 / zoom));
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
		textExtent.x = si(textExtent.x);
		textExtent.y = si(textExtent.y);
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