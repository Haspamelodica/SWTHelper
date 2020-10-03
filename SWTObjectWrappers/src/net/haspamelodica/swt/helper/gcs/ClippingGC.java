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

import net.haspamelodica.swt.helper.ClippingHelper;
import net.haspamelodica.swt.helper.ClippingHelper.LineClipResult;
import net.haspamelodica.swt.helper.ClippingHelper.RectangleClippingResult;
import net.haspamelodica.swt.helper.swtobjectwrappers.Font;
import net.haspamelodica.swt.helper.swtobjectwrappers.Path;
import net.haspamelodica.swt.helper.swtobjectwrappers.Point;
import net.haspamelodica.swt.helper.swtobjectwrappers.Rectangle;

public class ClippingGC implements GeneralGC
{
	private final GeneralGC	gc;
	private final double	minDstX1, minDstY1, maxDstX2, maxDstY2;

	public ClippingGC(GeneralGC gc, double x, double y, double w, double h)
	{
		this.gc = gc;
		this.minDstX1 = x;
		this.minDstY1 = y;
		this.maxDstX2 = x + w;
		this.maxDstY2 = y + h;
	}

	// TODO implement more methods!

	@Override
	public void copyArea(Image image, double x, double y)
	{
		gc.copyArea(image, x, y);
	}
	@Override
	public void copyArea(double srcX, double srcY, double width, double height, double destX, double destY)
	{
		gc.copyArea(srcX, srcY, width, height, destX, destY);
	}
	@Override
	public void copyArea(double srcX, double srcY, double width, double height, double destX, double destY, boolean paint)
	{
		gc.copyArea(srcX, srcY, width, height, destX, destY, paint);
	}
	@Override
	public void drawArc(double x, double y, double width, double height, double startAngle, double arcAngle)
	{
		gc.drawArc(x, y, width, height, startAngle, arcAngle);
	}
	@Override
	public void drawFocus(double x, double y, double width, double height)
	{
		gc.drawFocus(x, y, width, height);
	}
	@Override
	public void drawImage(Image image, double x, double y)
	{
		gc.drawImage(image, x, y);
	}
	@Override
	public void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, double destX, double destY, double destWidth, double destHeight)
	{
		RectangleClippingResult c = ClippingHelper.clipRectangleRectangleSrcAsInts(
				srcX, srcY, srcX + srcWidth, srcY + srcHeight,
				destX, destY, destX + destWidth, destY + destHeight,
				minDstX1, minDstY1, maxDstX2, maxDstY2);
		if(c != null)
			gc.drawImage(image,
					c.srcX1, c.srcY1, c.srcX2 - c.srcX1, c.srcY2 - c.srcY1,
					c.dstX1, c.dstY1, c.dstX2 - c.dstX1, c.dstY2 - c.dstY1);
	}
	@Override
	public void drawLine(double x1, double y1, double x2, double y2)
	{
		LineClipResult c = ClippingHelper.clipLineRectangleCohenSutherland(x1, y1, x2, y2, minDstX1, minDstY1, maxDstX2, maxDstY2, getLineWidth());
		if(c != null)
			gc.drawLine(c.x1, c.y1, c.x2, c.y2);
	}
	@Override
	public void drawOval(double x, double y, double width, double height)
	{
		gc.drawOval(x, y, width, height);
	}
	@Override
	public void drawPath(Path path)
	{
		gc.drawPath(path);
	}
	@Override
	public void drawPoint(double x, double y)
	{
		gc.drawPoint(x, y);
	}
	@Override
	public void drawPolygon(double[] pointArray)
	{
		gc.drawPolygon(pointArray);
	}
	@Override
	public void drawPolyline(double[] pointArray)
	{
		gc.drawPolyline(pointArray);
	}
	@Override
	public void drawRectangle(double x, double y, double width, double height)
	{
		//TODO clip better
		double lineWidth = getLineWidth();
		if(x >= minDstX1 - lineWidth / 2 || y >= minDstY1 - lineWidth / 2 || x + width <= maxDstX2 + lineWidth / 2 || y + height <= maxDstY2 + lineWidth / 2)
			gc.drawRectangle(x, y, width, height);
	}
	@Override
	public void drawRectangle(Rectangle rect)
	{
		//avoid duplication of clipping code
		drawRectangle(rect.x, rect.y, rect.width, rect.height);
	}
	@Override
	public void drawRoundRectangle(double x, double y, double width, double height, double arcWidth, double arcHeight)
	{
		gc.drawRoundRectangle(x, y, width, height, arcWidth, arcHeight);
	}
	@Override
	public void drawString(String string, double x, double y)
	{
		gc.drawString(string, x, y);
	}
	@Override
	public void drawString(String string, double x, double y, boolean isTransparent)
	{
		gc.drawString(string, x, y, isTransparent);
	}
	@Override
	public void drawText(String string, double x, double y)
	{
		gc.drawText(string, x, y);
	}
	@Override
	public void drawText(String string, double x, double y, boolean isTransparent)
	{
		gc.drawText(string, x, y, isTransparent);
	}
	@Override
	public void drawText(String string, double x, double y, int flags)
	{
		gc.drawText(string, x, y, flags);
	}
	@Override
	public void fillArc(double x, double y, double width, double height, double startAngle, double arcAngle)
	{
		gc.fillArc(x, y, width, height, startAngle, arcAngle);
	}
	@Override
	public void fillGradientRectangle(double x, double y, double width, double height, boolean vertical)
	{
		gc.fillGradientRectangle(x, y, width, height, vertical);
	}
	@Override
	public void fillOval(double x, double y, double width, double height)
	{
		gc.fillOval(x, y, width, height);
	}
	@Override
	public void fillPath(Path path)
	{
		gc.fillPath(path);
	}
	@Override
	public void fillPolygon(double[] pointArray)
	{
		gc.fillPolygon(pointArray);
	}
	@Override
	public void fillRectangle(double x, double y, double width, double height)
	{
		double xClipped = x;
		double yClipped = y;
		double widthClipped = width;
		double heightClipped = height;

		if(xClipped < minDstX1)
		{
			widthClipped -= minDstX1 - xClipped;
			xClipped = minDstX1;
		}
		if(yClipped < minDstY1)
		{
			heightClipped -= minDstY1 - yClipped;
			yClipped = minDstY1;
		}
		if(widthClipped > maxDstX2 - xClipped)
			widthClipped = maxDstX2 - xClipped;
		if(heightClipped > maxDstY2 - yClipped)
			heightClipped = maxDstY2 - yClipped;

		if(widthClipped > 0 && heightClipped > 0)
			gc.fillRectangle(xClipped, yClipped, widthClipped, heightClipped);
	}
	@Override
	public void fillRectangle(Rectangle rect)
	{
		gc.fillRectangle(rect);
	}
	@Override
	public void fillRoundRectangle(double x, double y, double width, double height, double arcWidth, double arcHeight)
	{
		gc.fillRoundRectangle(x, y, width, height, arcWidth, arcHeight);
	}
	@Override
	public double getAdvanceWidth(char ch)
	{
		return gc.getAdvanceWidth(ch);
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
		return gc.getBackgroundPattern();
	}
	@Override
	public double getCharWidth(char ch)
	{
		return gc.getCharWidth(ch);
	}
	@Override
	public Rectangle getClipping()
	{
		return gc.getClipping();
	}
	@Override
	public void getClipping(Region region)
	{
		gc.getClipping(region);
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
		return gc.getFont();
	}
	@Override
	public FontMetrics getFontMetrics()
	{
		return gc.getFontMetrics();
	}
	@Override
	public Color getForeground()
	{
		return gc.getForeground();
	}
	@Override
	public Pattern getForegroundPattern()
	{
		return gc.getForegroundPattern();
	}
	@Override
	public GCData getGCData()
	{
		return gc.getGCData();
	}
	@Override
	public int getInterpolation()
	{
		return gc.getInterpolation();
	}
	@Override
	public LineAttributes getLineAttributes()
	{
		return gc.getLineAttributes();
	}
	@Override
	public int getLineCap()
	{
		return gc.getLineCap();
	}
	@Override
	public double[] getLineDash()
	{
		return gc.getLineDash();
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
		return gc.getLineWidth();
	}
	@Override
	public int getStyle()
	{
		return gc.getStyle();
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
		gc.setAdvanced(advanced);
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
		gc.setBackgroundPattern(pattern);
	}
	@Override
	public void setClipping(double x, double y, double width, double height)
	{
		gc.setClipping(x, y, width, height);
	}
	@Override
	public void setClipping(Path path)
	{
		gc.setClipping(path);
	}
	@Override
	public void setClipping(Rectangle rect)
	{
		gc.setClipping(rect);
	}
	@Override
	public void setClipping(Region region)
	{
		gc.setClipping(region);
	}
	@Override
	public void setFillRule(int rule)
	{
		gc.setFillRule(rule);
	}
	@Override
	public void setFont(Font font)
	{
		gc.setFont(font);
	}
	@Override
	public void setForeground(Color color)
	{
		gc.setForeground(color);
	}
	@Override
	public void setForegroundPattern(Pattern pattern)
	{
		gc.setForegroundPattern(pattern);
	}
	@Override
	public void setInterpolation(int interpolation)
	{
		gc.setInterpolation(interpolation);
	}
	@Override
	public void setLineAttributes(LineAttributes attributes)
	{
		gc.setLineAttributes(attributes);
	}
	@Override
	public void setLineCap(int cap)
	{
		gc.setLineCap(cap);
	}
	@Override
	public void setLineDash(double[] dashes)
	{
		gc.setLineDash(dashes);
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
		gc.setLineWidth(lineWidth);
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
		gc.setTransform(transform);
	}
	@Override
	public Point stringExtent(String string)
	{
		return gc.stringExtent(string);
	}
	@Override
	public Point textExtent(String string)
	{
		return gc.textExtent(string);
	}
	@Override
	public Point textExtent(String string, int flags)
	{
		return gc.textExtent(string, flags);
	}
	@Override
	public void disposeThisLayer()
	{
		//nothing to do
	}
}