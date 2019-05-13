package net.haspamelodica.swt.helper.gcs;

import java.util.Arrays;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GCData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.graphics.Transform;

import net.haspamelodica.swt.helper.ZoomedRegion;
import net.haspamelodica.swt.helper.swtobjectwrappers.Font;
import net.haspamelodica.swt.helper.swtobjectwrappers.Path;
import net.haspamelodica.swt.helper.swtobjectwrappers.Point;
import net.haspamelodica.swt.helper.swtobjectwrappers.Rectangle;

public class TranslatedGC implements GeneralGC
{
	private final GeneralGC	gc;
	private final double	z, imgOffX, imgOffY;

	public TranslatedGC(GeneralGC gc, ZoomedRegion tilePos)
	{
		this(gc, tilePos.zoom, tilePos.x, tilePos.y, false);
	}
	public TranslatedGC(GeneralGC gc, double z, double x, double y, boolean invertOffset)
	{
		this.gc = gc;
		this.z = z;
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
		this(gc, 1, xOff, yOff, true);
	}

	private double s(double s)
	{
		return s * z;
	}

	private double sx(double x)
	{
		return x * z - imgOffX;
	}

	private double sy(double y)
	{
		return y * z - imgOffY;
	}

	// TODO implement more methods!
	public void copyArea(Image image, double x, double y)
	{
		throw new IllegalStateException("unimplemented");
	}
	public void copyArea(double srcX, double srcY, double width, double height, double destX, double destY)
	{
		throw new IllegalStateException("unimplemented");
	}
	public void copyArea(double srcX, double srcY, double width, double height, double destX, double destY, boolean paint)
	{
		throw new IllegalStateException("unimplemented");
	}
	public void drawArc(double x, double y, double width, double height, double startAngle, double arcAngle)
	{
		throw new IllegalStateException("unimplemented");
	}
	public void drawFocus(double x, double y, double width, double height)
	{
		throw new IllegalStateException("unimplemented");
	}
	public void drawImage(Image image, double x, double y)
	{
		Rectangle bounds = new Rectangle(image.getBounds());
		gc.drawImage(image, bounds.x, bounds.y, bounds.width, bounds.height, sx(x), sy(y), s(bounds.width), s(bounds.height));
	}
	public void drawImage(Image image, double srcX, double srcY, double srcWidth, double srcHeight, double destX, double destY, double destWidth, double destHeight)
	{
		gc.drawImage(image, srcX, srcY, srcWidth, srcHeight, sx(destX), sy(destY), s(destWidth), s(destHeight));
	}
	public void drawLine(double x1, double y1, double x2, double y2)
	{
		gc.drawLine(sx(x1), sy(y1), sx(x2), sy(y2));
	}
	public void drawOval(double x, double y, double width, double height)
	{
		gc.drawOval(sx(x), sy(y), s(width), s(height));
	}
	public void drawPath(Path path)
	{
		gc.drawPath(path.translate(imgOffX, imgOffY, z));
	}
	public void drawPoint(double x, double y)
	{
		gc.drawPoint(sx(x), sy(y));
	}
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
	public void drawPolyline(double[] pointArray)
	{
		throw new IllegalStateException("unimplemented");
	}
	public void drawRectangle(double x, double y, double width, double height)
	{
		gc.drawRectangle(sx(x), sy(y), s(width), s(height));
	}
	public void drawRectangle(Rectangle rect)
	{
		throw new IllegalStateException("unimplemented");
	}
	public void drawRoundRectangle(double x, double y, double width, double height, double arcWidth, double arcHeight)
	{
		gc.drawRoundRectangle(sx(x), sy(y), s(width), s(height), s(arcWidth), s(arcHeight));
	}
	public void drawString(String string, double x, double y)
	{
		gc.drawString(string, sx(x), sy(y));
	}
	public void drawString(String string, double x, double y, boolean isTransparent)
	{
		throw new IllegalStateException("unimplemented");
	}
	public void drawText(String string, double x, double y)
	{
		gc.drawText(string, sx(x), sy(y));
	}
	public void drawText(String string, double x, double y, boolean isTransparent)
	{
		gc.drawText(string, sx(x), sy(y), true);
	}
	public void drawText(String string, double x, double y, int flags)
	{
		throw new IllegalStateException("unimplemented");
	}
	public void fillArc(double x, double y, double width, double height, double startAngle, double arcAngle)
	{
		gc.fillArc(sx(x), sy(y), s(width), s(height), startAngle, arcAngle);
	}
	public void fillGradientRectangle(double x, double y, double width, double height, boolean vertical)
	{
		throw new IllegalStateException("unimplemented");
	}
	public void fillOval(double x, double y, double width, double height)
	{
		gc.fillOval(sx(x), sy(y), s(width), s(height));
	}
	public void fillPath(Path path)
	{
		gc.fillPath(path.translate(imgOffX, imgOffY, z));
	}
	public void fillPolygon(double[] pointArray)
	{
		throw new IllegalStateException("unimplemented");
	}
	public void fillRectangle(double x, double y, double width, double height)
	{
		gc.fillRectangle(sx(x), sy(y), s(width), s(height));
	}
	public void fillRectangle(Rectangle rect)
	{
		throw new IllegalStateException("unimplemented");
	}
	public void fillRoundRectangle(double x, double y, double width, double height, double arcWidth, double arcHeight)
	{
		gc.fillRoundRectangle(sx(x), sy(y), s(width), s(height), s(arcWidth), s(arcHeight));
	}
	public double getAdvanceWidth(char ch)
	{
		throw new IllegalStateException("unimplemented");
	}
	public boolean getAdvanced()
	{
		return gc.getAdvanced();
	}
	public int getAlpha()
	{
		return gc.getAlpha();
	}
	public int getAntialias()
	{
		return gc.getAntialias();
	}
	public Color getBackground()
	{
		return gc.getBackground();
	}
	public Pattern getBackgroundPattern()
	{
		throw new IllegalStateException("unimplemented");
	}
	public double getCharWidth(char ch)
	{
		throw new IllegalStateException("unimplemented");
	}
	public Rectangle getClipping()
	{
		throw new IllegalStateException("unimplemented");
	}
	public void getClipping(Region region)
	{
		throw new IllegalStateException("unimplemented");
	}
	public Device getDevice()
	{
		return gc.getDevice();
	}
	public int getFillRule()
	{
		return gc.getFillRule();
	}
	public Font getFont()
	{
		return gc.getFont().unscale(z);
	}
	public FontMetrics getFontMetrics()
	{
		throw new IllegalStateException("unimplemented");
	}
	public Color getForeground()
	{
		return gc.getForeground();
	}
	public Pattern getForegroundPattern()
	{
		throw new IllegalStateException("unimplemented");
	}
	public GCData getGCData()
	{
		throw new IllegalStateException("unimplemented");
	}
	public int getInterpolation()
	{
		return gc.getInterpolation();
	}
	public LineAttributes getLineAttributes()
	{
		throw new IllegalStateException("unimplemented");
	}
	public int getLineCap()
	{
		return gc.getLineCap();
	}
	public double[] getLineDash()
	{
		throw new IllegalStateException("unimplemented");
	}
	public int getLineJoin()
	{
		return gc.getLineJoin();
	}
	public int getLineStyle()
	{
		return gc.getLineStyle();
	}
	public double getLineWidth()
	{
		return gc.getLineWidth() / z;
	}
	public int getStyle()
	{
		throw new IllegalStateException("unimplemented");
	}
	public int getTextAntialias()
	{
		throw new IllegalStateException("unimplemented");
	}
	public void getTransform(Transform transform)
	{
		throw new IllegalStateException("unimplemented");
	}
	public boolean getXORMode()
	{
		return gc.getXORMode();
	}
	public boolean isClipped()
	{
		return gc.isClipped();
	}
	public boolean isDisposed()
	{
		return gc.isDisposed();
	}
	public void setAdvanced(boolean advanced)
	{
		gc.setAdvanced(true);
	}
	public void setAntialias(int antialias)
	{
		gc.setAntialias(antialias);
	}
	public void setAlpha(int alpha)
	{
		gc.setAlpha(alpha);
	}
	public void setBackground(Color color)
	{
		gc.setBackground(color);
	}
	public void setBackgroundPattern(Pattern pattern)
	{
		if(pattern != null)
		{
			if(!pattern.equals(getBackgroundPattern()))
				throw new IllegalStateException("unimplemented");
		} else
			gc.setBackgroundPattern(null);
	}
	public void setClipping(double x, double y, double width, double height)
	{
		throw new IllegalStateException("unimplemented");
	}
	public void setClipping(Path path)
	{
		gc.setClipping(path.translate(imgOffX, imgOffY, z));
	}
	public void setClipping(Rectangle rect)
	{
		if(rect == null)
			gc.setClipping(rect);
		else
			gc.setClipping(sx(rect.x), sy(rect.y), s(rect.width), s(rect.height));
	}
	public void setClipping(Region region)
	{
		throw new IllegalStateException("unimplemented");
	}
	public void setFillRule(int rule)
	{
		gc.setFillRule(rule);
	}
	public void setFont(Font font)
	{
		gc.setFont(font.scale(z));
	}
	public void setForeground(Color color)
	{
		gc.setForeground(color);
	}
	public void setForegroundPattern(Pattern pattern)
	{
		if(pattern != null)
		{
			if(!pattern.equals(getForegroundPattern()))
				throw new IllegalStateException("unimplemented");
		} else
			gc.setForegroundPattern(null);
	}
	public void setInterpolation(int interpolation)
	{
		gc.setInterpolation(interpolation);
	}
	public void setLineAttributes(LineAttributes attributes)
	{
		throw new IllegalStateException("unimplemented");
	}
	public void setLineCap(int cap)
	{
		gc.setLineCap(cap);
	}
	public void setLineDash(double[] dashes)
	{
		if(dashes != null)
		{
			if(!dashes.equals(getLineDash()))
				throw new IllegalStateException("unimplemented");
		} else
			gc.setLineDash(null);
	}
	public void setLineJoin(int join)
	{
		gc.setLineJoin(join);
	}
	public void setLineStyle(int lineStyle)
	{
		gc.setLineStyle(lineStyle);
	}
	public void setLineWidth(double lineWidth)
	{
		gc.setLineWidth(s(lineWidth));
	}

	@SuppressWarnings("deprecation")
	public void setXORMode(boolean xor)
	{
		gc.setXORMode(xor);
	}
	public void setTextAntialias(int antialias)
	{
		gc.setTextAntialias(antialias);
	}
	public void setTransform(Transform transform)
	{
		if(transform != null)
		{
			Transform transformBuffer = new Transform(getDevice());
			getTransform(transformBuffer);
			float[] elementBuffer1 = new float[6];
			transform.getElements(elementBuffer1);
			float[] elementBuffer2 = new float[6];
			transformBuffer.getElements(elementBuffer2);
			transformBuffer.dispose();
			if(!Arrays.equals(elementBuffer1, elementBuffer2))
				throw new IllegalStateException("unimplemented");
		} else
			gc.setTransform(null);
	}
	public Point stringExtent(String string)
	{
		throw new IllegalStateException("unimplemented");
	}
	public Point textExtent(String string)
	{
		throw new IllegalStateException("unimplemented");
	}
	public Point textExtent(String string, int flags)
	{
		throw new IllegalStateException("unimplemented");
	}
	public void disposeThisLayer()
	{}
}