package net.haspamelodica.swt.helper.gcs;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

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

public class LoggingGC implements GeneralGC
{
	private static int nextGCID;

	private final GeneralGC	gc;
	private final int		id;

	public LoggingGC(GeneralGC gc)
	{
		this.gc = gc;
		this.id = nextGCID ++;
	}
	@Override
	public void copyArea(Image image, double x, double y)
	{
		methodStart("copyArea", image, x, y);
		gc.copyArea(image, x, y);
		methodStop("copyArea");
	}
	@Override
	public void copyArea(double srcX, double srcY, double width, double height, double destX, double destY)
	{
		methodStart("copyArea", srcX, srcY, width, height, destX, destY);
		gc.copyArea(srcX, srcY, width, height, destX, destY);
		methodStop("copyArea");
	}
	@Override
	public void copyArea(double srcX, double srcY, double width, double height, double destX, double destY, boolean paint)
	{
		methodStart("copyArea", srcX, srcY, width, height, destX, destY, paint);
		gc.copyArea(srcX, srcY, width, height, destX, destY, paint);
		methodStop("copyArea");
	}
	@Override
	public void drawArc(double x, double y, double width, double height, double startAngle, double arcAngle)
	{
		methodStart("drawArc", x, y, width, height, startAngle, arcAngle);
		gc.drawArc(x, y, width, height, startAngle, arcAngle);
		methodStop("drawArc");
	}
	@Override
	public void drawFocus(double x, double y, double width, double height)
	{
		methodStart("drawFocus", x, y, width, height);
		gc.drawFocus(x, y, width, height);
		methodStop("drawFocus");
	}
	@Override
	public void drawImage(Image image, double x, double y)
	{
		methodStart("drawImage", image, x, y);
		gc.drawImage(image, x, y);
		methodStop("drawImage");
	}
	@Override
	public void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, double destX, double destY, double destWidth, double destHeight)
	{
		methodStart("drawImage", image, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight);
		gc.drawImage(image, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight);
		methodStop("drawImage");
	}
	@Override
	public void drawLine(double x1, double y1, double x2, double y2)
	{
		methodStart("drawLine", x1, y1, x2, y2);
		gc.drawLine(x1, y1, x2, y2);
		methodStop("drawLine");
	}
	@Override
	public void drawOval(double x, double y, double width, double height)
	{
		methodStart("drawOval", x, y, width, height);
		gc.drawOval(x, y, width, height);
		methodStop("drawOval");
	}
	@Override
	public void drawPath(Path path)
	{
		methodStart("drawPath", path);
		gc.drawPath(path);
		methodStop("drawPath");
	}
	@Override
	public void drawPoint(double x, double y)
	{
		methodStart("drawPoint", x, y);
		gc.drawPoint(x, y);
		methodStop("drawPoint");
	}
	@Override
	public void drawPolygon(double[] pointArray)
	{
		methodStart("drawPolygon", pointArray);
		gc.drawPolygon(pointArray);
		methodStop("drawPolygon");
	}
	@Override
	public void drawPolyline(double[] pointArray)
	{
		methodStart("drawPolyline", pointArray);
		gc.drawPolyline(pointArray);
		methodStop("drawPolyline");
	}
	@Override
	public void drawRectangle(double x, double y, double width, double height)
	{
		methodStart("drawRectangle", x, y, width, height);
		gc.drawRectangle(x, y, width, height);
		methodStop("drawRectangle");
	}
	@Override
	public void drawRectangle(Rectangle rect)
	{
		methodStart("drawRectangle", rect);
		gc.drawRectangle(rect);
		methodStop("drawRectangle");
	}
	@Override
	public void drawRoundRectangle(double x, double y, double width, double height, double arcWidth, double arcHeight)
	{
		methodStart("drawRoundRectangle", x, y, width, height, arcWidth, arcHeight);
		gc.drawRoundRectangle(x, y, width, height, arcWidth, arcHeight);
		methodStop("drawRoundRectangle");
	}
	@Override
	public void drawString(String string, double x, double y)
	{
		methodStart("drawString", string, x, y);
		gc.drawString(string, x, y);
		methodStop("drawString");
	}
	@Override
	public void drawString(String string, double x, double y, boolean isTransparent)
	{
		methodStart("drawString", string, x, y, isTransparent);
		gc.drawString(string, x, y, isTransparent);
		methodStop("drawString");
	}
	@Override
	public void drawText(String string, double x, double y)
	{
		methodStart("drawText", string, x, y);
		gc.drawText(string, x, y);
		methodStop("drawText");
	}
	@Override
	public void drawText(String string, double x, double y, boolean isTransparent)
	{
		methodStart("drawText", string, x, y, isTransparent);
		gc.drawText(string, x, y, isTransparent);
		methodStop("drawText");
	}
	@Override
	public void drawText(String string, double x, double y, int flags)
	{
		methodStart("drawText", string, x, y, flags);
		gc.drawText(string, x, y, flags);
		methodStop("drawText");
	}
	@Override
	public void fillArc(double x, double y, double width, double height, double startAngle, double arcAngle)
	{
		methodStart("fillArc", x, y, width, height, startAngle, arcAngle);
		gc.fillArc(x, y, width, height, startAngle, arcAngle);
		methodStop("fillArc");
	}
	@Override
	public void fillGradientRectangle(double x, double y, double width, double height, boolean vertical)
	{
		methodStart("fillGradientRectangle", x, y, width, height, vertical);
		gc.fillGradientRectangle(x, y, width, height, vertical);
		methodStop("fillGradientRectangle");
	}
	@Override
	public void fillOval(double x, double y, double width, double height)
	{
		methodStart("fillOval", x, y, width, height);
		gc.fillOval(x, y, width, height);
		methodStop("fillOval");
	}
	@Override
	public void fillPath(Path path)
	{
		methodStart("fillPath", path);
		gc.fillPath(path);
		methodStop("fillPath");
	}
	@Override
	public void fillPolygon(double[] pointArray)
	{
		methodStart("fillPolygon", pointArray);
		gc.fillPolygon(pointArray);
		methodStop("fillPolygon");
	}
	@Override
	public void fillRectangle(double x, double y, double width, double height)
	{
		methodStart("fillRectangle", x, y, width, height);
		gc.fillRectangle(x, y, width, height);
		methodStop("fillRectangle");
	}
	@Override
	public void fillRectangle(Rectangle rect)
	{
		methodStart("fillRectangle", rect);
		gc.fillRectangle(rect);
		methodStop("fillRectangle");
	}
	@Override
	public void fillRoundRectangle(double x, double y, double width, double height, double arcWidth, double arcHeight)
	{
		methodStart("fillRoundRectangle", x, y, width, height, arcWidth, arcHeight);
		gc.fillRoundRectangle(x, y, width, height, arcWidth, arcHeight);
		methodStop("fillRoundRectangle");
	}
	@Override
	public double getAdvanceWidth(char ch)
	{
		methodStart("getAdvanceWidth", ch);
		double result = gc.getAdvanceWidth(ch);
		methodStop("getAdvanceWidth");
		return result;
	}
	@Override
	public boolean getAdvanced()
	{
		methodStart("getAdvanced");
		boolean result = gc.getAdvanced();
		methodStop("getAdvanced");
		return result;
	}
	@Override
	public int getAlpha()
	{
		methodStart("getAlpha");
		int result = gc.getAlpha();
		methodStop("getAlpha");
		return result;
	}
	@Override
	public int getAntialias()
	{
		methodStart("getAntialias");
		int result = gc.getAntialias();
		methodStop("getAntialias");
		return result;
	}
	@Override
	public Color getBackground()
	{
		methodStart("getBackground");
		Color result = gc.getBackground();
		methodStop("getBackground");
		return result;
	}
	@Override
	public Pattern getBackgroundPattern()
	{
		methodStart("getBackgroundPattern");
		Pattern result = gc.getBackgroundPattern();
		methodStop("getBackgroundPattern");
		return result;
	}
	@Override
	public double getCharWidth(char ch)
	{
		methodStart("getCharWidth", ch);
		double result = gc.getCharWidth(ch);
		methodStop("getCharWidth");
		return result;
	}
	@Override
	public Rectangle getClipping()
	{
		methodStart("getClipping");
		Rectangle result = gc.getClipping();
		methodStop("getClipping");
		return result;
	}
	@Override
	public void getClipping(Region region)
	{
		methodStart("getClipping", region);
		gc.getClipping(region);
		methodStop("getClipping");
	}
	@Override
	public Device getDevice()
	{
		methodStart("getDevice");
		Device result = gc.getDevice();
		methodStop("getDevice");
		return result;
	}
	@Override
	public int getFillRule()
	{
		methodStart("getFillRule");
		int result = gc.getFillRule();
		methodStop("getFillRule");
		return result;
	}
	@Override
	public Font getFont()
	{
		methodStart("getFont");
		Font result = gc.getFont();
		methodStop("getFont");
		return result;
	}
	@Override
	public FontMetrics getFontMetrics()
	{
		methodStart("getFontMetrics");
		FontMetrics result = gc.getFontMetrics();
		methodStop("getFontMetrics");
		return result;
	}
	@Override
	public Color getForeground()
	{
		methodStart("getForeground");
		Color result = gc.getForeground();
		methodStop("getForeground");
		return result;
	}
	@Override
	public Pattern getForegroundPattern()
	{
		methodStart("getForegroundPattern");
		Pattern result = gc.getForegroundPattern();
		methodStop("getForegroundPattern");
		return result;
	}
	@Override
	public GCData getGCData()
	{
		methodStart("getGCData");
		GCData result = gc.getGCData();
		methodStop("getGCData");
		return result;
	}
	@Override
	public int getInterpolation()
	{
		methodStart("getInterpolation");
		int result = gc.getInterpolation();
		methodStop("getInterpolation");
		return result;
	}
	@Override
	public LineAttributes getLineAttributes()
	{
		methodStart("getLineAttributes");
		LineAttributes result = gc.getLineAttributes();
		methodStop("getLineAttributes");
		return result;
	}
	@Override
	public int getLineCap()
	{
		methodStart("getLineCap");
		int result = gc.getLineCap();
		methodStop("getLineCap");
		return result;
	}
	@Override
	public double[] getLineDash()
	{
		methodStart("getLineDash");
		double[] result = gc.getLineDash();
		methodStop("getLineDash");
		return result;
	}
	@Override
	public int getLineJoin()
	{
		methodStart("getLineJoin");
		int result = gc.getLineJoin();
		methodStop("getLineJoin");
		return result;
	}
	@Override
	public int getLineStyle()
	{
		methodStart("getLineStyle");
		int result = gc.getLineStyle();
		methodStop("getLineStyle");
		return result;
	}
	@Override
	public double getLineWidth()
	{
		methodStart("getLineWidth");
		double result = gc.getLineWidth();
		methodStop("getLineWidth");
		return result;
	}
	@Override
	public int getStyle()
	{
		methodStart("getStyle");
		int result = gc.getStyle();
		methodStop("getStyle");
		return result;
	}
	@Override
	public int getTextAntialias()
	{
		methodStart("getTextAntialias");
		int result = gc.getTextAntialias();
		methodStop("getTextAntialias");
		return result;
	}
	@Override
	public void getTransform(Transform transform)
	{
		methodStart("getTransform", transform);
		gc.getTransform(transform);
		methodStop("getTransform");
	}
	@Override
	public boolean getXORMode()
	{
		methodStart("getXORMode");
		boolean result = gc.getXORMode();
		methodStop("getXORMode");
		return result;
	}
	@Override
	public boolean isClipped()
	{
		methodStart("isClipped");
		boolean result = gc.isClipped();
		methodStop("isClipped");
		return result;
	}
	@Override
	public boolean isDisposed()
	{
		methodStart("isDisposed");
		boolean result = gc.isDisposed();
		methodStop("isDisposed");
		return result;
	}
	@Override
	public void setAdvanced(boolean advanced)
	{
		methodStart("setAdvanced", advanced);
		gc.setAdvanced(advanced);
		methodStop("setAdvanced");
	}
	@Override
	public void setAntialias(int antialias)
	{
		methodStart("setAntialias", antialias);
		gc.setAntialias(antialias);
		methodStop("setAntialias");
	}
	@Override
	public void setAlpha(int alpha)
	{
		methodStart("setAlpha", alpha);
		gc.setAlpha(alpha);
		methodStop("setAlpha");
	}
	@Override
	public void setBackground(Color color)
	{
		methodStart("setBackground", color);
		gc.setBackground(color);
		methodStop("setBackground");
	}
	@Override
	public void setBackgroundPattern(Pattern pattern)
	{
		methodStart("setBackgroundPattern", pattern);
		gc.setBackgroundPattern(pattern);
		methodStop("setBackgroundPattern");
	}
	@Override
	public void setClipping(double x, double y, double width, double height)
	{
		methodStart("setClipping", x, y, width, height);
		gc.setClipping(x, y, width, height);
		methodStop("setClipping");
	}
	@Override
	public void setClipping(Path path)
	{
		methodStart("setClipping", path);
		gc.setClipping(path);
		methodStop("setClipping");
	}
	@Override
	public void setClipping(Rectangle rect)
	{
		methodStart("setClipping", rect);
		gc.setClipping(rect);
		methodStop("setClipping");
	}
	@Override
	public void setClipping(Region region)
	{
		methodStart("setClipping", region);
		gc.setClipping(region);
		methodStop("setClipping");
	}
	@Override
	public void setFillRule(int rule)
	{
		methodStart("setFillRule", rule);
		gc.setFillRule(rule);
		methodStop("setFillRule");
	}
	@Override
	public void setFont(Font font)
	{
		methodStart("setFont", font);
		gc.setFont(font);
		methodStop("setFont");
	}
	@Override
	public void setForeground(Color color)
	{
		methodStart("setForeground", color);
		gc.setForeground(color);
		methodStop("setForeground");
	}
	@Override
	public void setForegroundPattern(Pattern pattern)
	{
		methodStart("setForegroundPattern", pattern);
		gc.setForegroundPattern(pattern);
		methodStop("setForegroundPattern");
	}
	@Override
	public void setInterpolation(int interpolation)
	{
		methodStart("setInterpolation", interpolation);
		gc.setInterpolation(interpolation);
		methodStop("setInterpolation");
	}
	@Override
	public void setLineAttributes(LineAttributes attributes)
	{
		methodStart("setLineAttributes", attributes);
		gc.setLineAttributes(attributes);
		methodStop("setLineAttributes");
	}
	@Override
	public void setLineCap(int cap)
	{
		methodStart("setLineCap", cap);
		gc.setLineCap(cap);
		methodStop("setLineCap");
	}
	@Override
	public void setLineDash(double[] dashes)
	{
		methodStart("setLineDash", dashes);
		gc.setLineDash(dashes);
		methodStop("setLineDash");
	}
	@Override
	public void setLineJoin(int join)
	{
		methodStart("setLineJoin", join);
		gc.setLineJoin(join);
		methodStop("setLineJoin");
	}
	@Override
	public void setLineStyle(int lineStyle)
	{
		methodStart("setLineStyle", lineStyle);
		gc.setLineStyle(lineStyle);
		methodStop("setLineStyle");
	}
	@Override
	public void setLineWidth(double lineWidth)
	{
		methodStart("setLineWidth", lineWidth);
		gc.setLineWidth(lineWidth);
		methodStop("setLineWidth");
	}
	@SuppressWarnings("deprecation")
	@Override
	public void setXORMode(boolean xor)
	{
		methodStart("setXORMode", xor);
		gc.setXORMode(xor);
		methodStop("setXORMode");
	}
	@Override
	public void setTextAntialias(int antialias)
	{
		methodStart("setTextAntialias", antialias);
		gc.setTextAntialias(antialias);
		methodStop("setTextAntialias");
	}
	@Override
	public void setTransform(Transform transform)
	{
		methodStart("setTransform", transform);
		gc.setTransform(transform);
		methodStop("setTransform");
	}
	@Override
	public Point stringExtent(String string)
	{
		methodStart("stringExtent", string);
		Point result = gc.stringExtent(string);
		methodStop("stringExtent");
		return result;
	}
	@Override
	public Point textExtent(String string)
	{
		methodStart("textExtent", string);
		Point result = gc.textExtent(string);
		methodStop("textExtent");
		return result;
	}
	@Override
	public Point textExtent(String string, int flags)
	{
		methodStart("textExtent", string, flags);
		Point result = gc.textExtent(string, flags);
		methodStop("textExtent");
		return result;
	}
	@Override
	public void disposeThisLayer()
	{
		//nothing to do
	}

	private void methodStart(String name, Object... params)
	{
		System.out.println(Arrays
				.stream(params)
				.map(o ->
				{
					if(o instanceof boolean[])
						return Arrays.toString((boolean[]) o);
					else if(o instanceof byte[])
						return Arrays.toString((byte[]) o);
					else if(o instanceof char[])
						return Arrays.toString((char[]) o);
					else if(o instanceof double[])
						return Arrays.toString((double[]) o);
					else if(o instanceof float[])
						return Arrays.toString((float[]) o);
					else if(o instanceof int[])
						return Arrays.toString((int[]) o);
					else if(o instanceof long[])
						return Arrays.toString((long[]) o);
					else if(o instanceof Object[])
						return Arrays.toString((Object[]) o);
					else
						return Objects.toString(o);
				})
				.collect(Collectors.joining(", ", "GC #" + id + ": " + name + '(', ")")));
	}
	private void methodStop(@SuppressWarnings("unused") String name)
	{
		//do nothing; maybe introduce timing analysis later
	}
}