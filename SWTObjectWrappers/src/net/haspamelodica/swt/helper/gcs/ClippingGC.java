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

	public void drawLine(double x1, double y1, double x2, double y2)
	{
		LineClipResult c = ClippingHelper.clipLineRectangleCohenSutherland(x1, y1, x2, y2, minDstX1, minDstY1, maxDstX2, maxDstY2);
		if(c != null)
			gc.drawLine(c.x1, c.y1, c.x2, c.y2);
	}
	public void fillRectangle(double x, double y, double width, double height)
	{
		RectangleClippingResult c = ClippingHelper.clipRectangleRectangleSrcAsInts(x, y, x + width, y + height,
				x, y, x + width, y + height,
				minDstX1, minDstY1, maxDstX2, maxDstY2);
		if(c != null)
			gc.fillRectangle(c.dstX1, c.dstY1, c.dstX2 - c.dstX1, c.dstY2 - c.dstY1);
	}

	// TODO implement more methods!

	//forwarded; not clipped
	public void copyArea(Image image, double x, double y)
	{
		gc.copyArea(image, x, y);
	}
	public void copyArea(double srcX, double srcY, double width, double height, double destX, double destY)
	{
		gc.copyArea(srcX, srcY, width, height, destX, destY);
	}
	public void copyArea(double srcX, double srcY, double width, double height, double destX, double destY, boolean paint)
	{
		gc.copyArea(srcX, srcY, width, height, destX, destY, paint);
	}
	public void drawArc(double x, double y, double width, double height, double startAngle, double arcAngle)
	{
		gc.drawArc(x, y, width, height, startAngle, arcAngle);
	}
	public void drawFocus(double x, double y, double width, double height)
	{
		gc.drawFocus(x, y, width, height);
	}
	public void drawImage(Image image, double x, double y)
	{
		gc.drawImage(image, x, y);
	}
	public void drawImage(Image image, double srcX, double srcY, double srcWidth, double srcHeight, double destX, double destY, double destWidth, double destHeight)
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
	public void drawOval(double x, double y, double width, double height)
	{
		gc.drawOval(x, y, width, height);
	}
	public void drawPath(Path path)
	{
		gc.drawPath(path);
	}
	public void drawPoint(double x, double y)
	{
		gc.drawPoint(x, y);
	}
	public void drawPolygon(double[] pointArray)
	{
		gc.drawPolygon(pointArray);
	}
	public void drawPolyline(double[] pointArray)
	{
		gc.drawPolyline(pointArray);
	}
	public void drawRectangle(double x, double y, double width, double height)
	{
		gc.drawRectangle(x, y, width, height);
	}
	public void drawRectangle(Rectangle rect)
	{
		gc.drawRectangle(rect);
	}
	public void drawRoundRectangle(double x, double y, double width, double height, double arcWidth, double arcHeight)
	{
		gc.drawRoundRectangle(x, y, width, height, arcWidth, arcHeight);
	}
	public void drawString(String string, double x, double y)
	{
		gc.drawString(string, x, y);
	}
	public void drawString(String string, double x, double y, boolean isTransparent)
	{
		gc.drawString(string, x, y, isTransparent);
	}
	public void drawText(String string, double x, double y)
	{
		gc.drawText(string, x, y);
	}
	public void drawText(String string, double x, double y, boolean isTransparent)
	{
		gc.drawText(string, x, y, isTransparent);
	}
	public void drawText(String string, double x, double y, int flags)
	{
		gc.drawText(string, x, y, flags);
	}
	public void fillArc(double x, double y, double width, double height, double startAngle, double arcAngle)
	{
		gc.fillArc(x, y, width, height, startAngle, arcAngle);
	}
	public void fillGradientRectangle(double x, double y, double width, double height, boolean vertical)
	{
		gc.fillGradientRectangle(x, y, width, height, vertical);
	}
	public void fillOval(double x, double y, double width, double height)
	{
		gc.fillOval(x, y, width, height);
	}
	public void fillPath(Path path)
	{
		gc.fillPath(path);
	}
	public void fillPolygon(double[] pointArray)
	{
		gc.fillPolygon(pointArray);
	}
	public void fillRectangle(Rectangle rect)
	{
		gc.fillRectangle(rect);
	}
	public void fillRoundRectangle(double x, double y, double width, double height, double arcWidth, double arcHeight)
	{
		gc.fillRoundRectangle(x, y, width, height, arcWidth, arcHeight);
	}
	public double getAdvanceWidth(char ch)
	{
		return gc.getAdvanceWidth(ch);
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
		return gc.getBackgroundPattern();
	}
	public double getCharWidth(char ch)
	{
		return gc.getCharWidth(ch);
	}
	public Rectangle getClipping()
	{
		return gc.getClipping();
	}
	public void getClipping(Region region)
	{
		gc.getClipping(region);
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
		return gc.getFont();
	}
	public FontMetrics getFontMetrics()
	{
		return gc.getFontMetrics();
	}
	public Color getForeground()
	{
		return gc.getForeground();
	}
	public Pattern getForegroundPattern()
	{
		return gc.getForegroundPattern();
	}
	public GCData getGCData()
	{
		return gc.getGCData();
	}
	public int getInterpolation()
	{
		return gc.getInterpolation();
	}
	public LineAttributes getLineAttributes()
	{
		return gc.getLineAttributes();
	}
	public int getLineCap()
	{
		return gc.getLineCap();
	}
	public double[] getLineDash()
	{
		return gc.getLineDash();
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
		return gc.getLineWidth();
	}
	public int getStyle()
	{
		return gc.getStyle();
	}
	public int getTextAntialias()
	{
		return gc.getTextAntialias();
	}
	public void getTransform(Transform transform)
	{
		gc.getTransform(transform);
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
		gc.setAdvanced(advanced);
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
		gc.setBackgroundPattern(pattern);
	}
	public void setClipping(double x, double y, double width, double height)
	{
		gc.setClipping(x, y, width, height);
	}
	public void setClipping(Path path)
	{
		gc.setClipping(path);
	}
	public void setClipping(Rectangle rect)
	{
		gc.setClipping(rect);
	}
	public void setClipping(Region region)
	{
		gc.setClipping(region);
	}
	public void setFillRule(int rule)
	{
		gc.setFillRule(rule);
	}
	public void setFont(Font font)
	{
		gc.setFont(font);
	}
	public void setForeground(Color color)
	{
		gc.setForeground(color);
	}
	public void setForegroundPattern(Pattern pattern)
	{
		gc.setForegroundPattern(pattern);
	}
	public void setInterpolation(int interpolation)
	{
		gc.setInterpolation(interpolation);
	}
	public void setLineAttributes(LineAttributes attributes)
	{
		gc.setLineAttributes(attributes);
	}
	public void setLineCap(int cap)
	{
		gc.setLineCap(cap);
	}
	public void setLineDash(double[] dashes)
	{
		gc.setLineDash(dashes);
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
		gc.setLineWidth(lineWidth);
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
		gc.setTransform(transform);
	}
	public Point stringExtent(String string)
	{
		return gc.stringExtent(string);
	}
	public Point textExtent(String string)
	{
		return gc.textExtent(string);
	}
	public Point textExtent(String string, int flags)
	{
		return gc.textExtent(string, flags);
	}
	public void disposeThisLayer()
	{}
}