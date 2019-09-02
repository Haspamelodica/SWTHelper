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

public class TextImprovingGC implements GeneralGC
{
	private final GeneralGC	gc;
	private final Transform	transformBuf;
	private final float[]	floatsBuf;

	private Font lastFont;
	//TODO remove "= 1"
	private double coordScaleFactor = 1;

	public TextImprovingGC(GeneralGC gc)
	{
		this.gc = gc;
		transformBuf = new Transform(gc.getDevice());
		floatsBuf = new float[6];
	}

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
		gc.drawImage(image, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight);
	}
	@Override
	public void drawLine(double x1, double y1, double x2, double y2)
	{
		gc.drawLine(x1, y1, x2, y2);
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
		gc.drawRectangle(x, y, width, height);
	}
	@Override
	public void drawRectangle(Rectangle rect)
	{
		gc.drawRectangle(rect);
	}
	@Override
	public void drawRoundRectangle(double x, double y, double width, double height, double arcWidth, double arcHeight)
	{
		gc.drawRoundRectangle(x, y, width, height, arcWidth, arcHeight);
	}
	@Override
	public void drawString(String string, double x, double y)
	{
		preTextOp();
		gc.drawString(string, coordScaleFactor * x, coordScaleFactor * y);
		postTextOp();
	}
	@Override
	public void drawString(String string, double x, double y, boolean isTransparent)
	{
		preTextOp();
		gc.drawString(string, coordScaleFactor * x, coordScaleFactor * y, isTransparent);
		postTextOp();
	}
	@Override
	public void drawText(String string, double x, double y)
	{
		preTextOp();
		gc.drawText(string, coordScaleFactor * x, coordScaleFactor * y);
		postTextOp();
	}
	@Override
	public void drawText(String string, double x, double y, boolean isTransparent)
	{
		preTextOp();
		gc.drawText(string, coordScaleFactor * x, coordScaleFactor * y, isTransparent);
		postTextOp();
	}
	@Override
	public void drawText(String string, double x, double y, int flags)
	{
		preTextOp();
		gc.drawText(string, coordScaleFactor * x, coordScaleFactor * y, flags);
		postTextOp();
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
		gc.fillRectangle(x, y, width, height);
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
		preTextOp();
		double advanceWidth = gc.getAdvanceWidth(ch) / coordScaleFactor;
		postTextOp();
		return advanceWidth;
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
		preTextOp();
		double charWidth = gc.getCharWidth(ch) / coordScaleFactor;
		postTextOp();
		return charWidth;
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
	@SuppressWarnings("deprecation")
	@Override
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
		preTextOp();
		Point stringExtent = gc.stringExtent(string);
		stringExtent.x /= coordScaleFactor;
		stringExtent.y /= coordScaleFactor;
		postTextOp();
		return stringExtent;
	}
	@Override
	public Point textExtent(String string)
	{
		preTextOp();
		Point textExtent = gc.textExtent(string);
		textExtent.x /= coordScaleFactor;
		textExtent.y /= coordScaleFactor;
		postTextOp();
		return textExtent;
	}
	@Override
	public Point textExtent(String string, int flags)
	{
		return gc.textExtent(string, flags);
	}
	@Override
	public void disposeThisLayer()
	{
		transformBuf.dispose();
	}
	private void preTextOp()
	{
		lastFont = gc.getFont();
		double targetFontHeight = lastFont.getHeight();
		//Font heights apparently aren't linear.
		//Because of this, texts rendered with different font heights (on the SWT side) differ
		//even with applying a transform that should compensate for the difference in size.
		//This is why we use a fixed font height (for small target font heights).
		//For large target font heights, this method introduces rounding errors.
		double integerFontHeight = Math.max(50, Math.round(targetFontHeight)); //Math.round(targetFontHeight);
		coordScaleFactor = integerFontHeight / targetFontHeight;
		float transformScaleFactor = (float) (targetFontHeight / integerFontHeight);
		gc.getTransform(transformBuf);
		transformBuf.getElements(floatsBuf);
		transformBuf.scale(transformScaleFactor, transformScaleFactor);
		gc.setTransform(transformBuf);
		gc.setFont(new Font(lastFont.getName(), integerFontHeight, lastFont.getStyle()));
	}
	private void postTextOp()
	{
		transformBuf.setElements(floatsBuf[0], floatsBuf[1], floatsBuf[2], floatsBuf[3], floatsBuf[4], floatsBuf[5]);
		gc.setTransform(transformBuf);
		gc.setFont(lastFont);
	}
}