package net.haspamelodica.swt.helper.gcs;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
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

public class SWTGC implements GeneralGC
{
	private final GC gc;

	private org.eclipse.swt.graphics.Font	currentSWTFont;
	private Font							currentFont;

	public SWTGC(GC gc)
	{
		this.gc = gc;
	}
	public void copyArea(Image image, double x, double y)
	{
		gc.copyArea(image, (int) x, (int) y);
	}
	public void copyArea(double srcX, double srcY, double width, double height, double destX, double destY)
	{
		gc.copyArea((int) srcX, (int) srcY, (int) Math.ceil(width), (int) Math.ceil(height), (int) destX, (int) destY);
	}
	public void copyArea(double srcX, double srcY, double width, double height, double destX, double destY, boolean paint)
	{
		gc.copyArea((int) srcX, (int) srcY, (int) Math.ceil(width), (int) Math.ceil(height), (int) destX, (int) destY, paint);
	}
	public void drawArc(double x, double y, double width, double height, double startAngle, double arcAngle)
	{
		gc.drawArc((int) x, (int) y, (int) Math.ceil(width), (int) Math.ceil(height), (int) startAngle, (int) Math.ceil(arcAngle));
	}
	public void drawFocus(double x, double y, double width, double height)
	{
		gc.drawFocus((int) x, (int) y, (int) Math.ceil(width), (int) Math.ceil(height));
	}
	public void drawImage(Image image, double x, double y)
	{
		gc.drawImage(image, (int) x, (int) y);
	}
	public void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, double destX, double destY, double destWidth, double destHeight)
	{
		gc.drawImage(image, srcX, srcY, srcWidth, srcHeight, (int) destX, (int) destY, (int) Math.ceil(destWidth), (int) Math.ceil(destHeight));
	}
	public void drawLine(double x1, double y1, double x2, double y2)
	{
		gc.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	}
	public void drawOval(double x, double y, double width, double height)
	{
		gc.drawOval((int) x, (int) y, (int) Math.ceil(width), (int) Math.ceil(height));
	}
	public void drawPath(Path path)
	{
		gc.drawPath(path.toSWTPath(getDevice()));
	}
	public void drawPoint(double x, double y)
	{
		gc.drawPoint((int) x, (int) y);
	}
	public void drawPolygon(double[] pointArray)
	{
		gc.drawPolygon(dsToIs(pointArray));
	}
	public void drawPolyline(double[] pointArray)
	{
		gc.drawPolyline(dsToIs(pointArray));
	}
	public void drawRectangle(double x, double y, double width, double height)
	{
		gc.drawRectangle((int) x, (int) y, (int) Math.ceil(width), (int) Math.ceil(height));
	}
	public void drawRectangle(Rectangle rect)
	{
		gc.drawRectangle(rect.toSWTRectanlge());
	}
	public void drawRoundRectangle(double x, double y, double width, double height, double arcWidth, double arcHeight)
	{
		gc.drawRoundRectangle((int) x, (int) y, (int) Math.ceil(width), (int) Math.ceil(height), (int) Math.ceil(arcWidth), (int) Math.ceil(arcHeight));
	}
	public void drawString(String string, double x, double y)
	{
		gc.drawString(string, (int) x, (int) y);
	}
	public void drawString(String string, double x, double y, boolean isTransparent)
	{
		gc.drawString(string, (int) x, (int) y, isTransparent);
	}
	public void drawText(String string, double x, double y)
	{
		gc.drawText(string, (int) x, (int) y);
	}
	public void drawText(String string, double x, double y, boolean isTransparent)
	{
		gc.drawText(string, (int) x, (int) y, isTransparent);
	}
	public void drawText(String string, double x, double y, int flags)
	{
		gc.drawText(string, (int) x, (int) y, flags);
	}
	public void fillArc(double x, double y, double width, double height, double startAngle, double arcAngle)
	{
		gc.fillArc((int) x, (int) y, (int) Math.ceil(width), (int) Math.ceil(height), (int) startAngle, (int) Math.ceil(arcAngle));
	}
	public void fillGradientRectangle(double x, double y, double width, double height, boolean vertical)
	{
		gc.fillGradientRectangle((int) x, (int) y, (int) Math.ceil(width), (int) Math.ceil(height), vertical);
	}
	public void fillOval(double x, double y, double width, double height)
	{
		gc.fillOval((int) x, (int) y, (int) Math.ceil(width), (int) Math.ceil(height));
	}
	public void fillPath(Path path)
	{
		gc.fillPath(path.toSWTPath(getDevice()));
	}
	public void fillPolygon(double[] pointArray)
	{
		gc.fillPolygon(dsToIs(pointArray));
	}
	public void fillRectangle(double x, double y, double width, double height)
	{
		gc.fillRectangle((int) x, (int) y, (int) Math.ceil(width), (int) Math.ceil(height));
	}
	public void fillRectangle(Rectangle rect)
	{
		gc.fillRectangle(rect.toSWTRectanlge());
	}
	public void fillRoundRectangle(double x, double y, double width, double height, double arcWidth, double arcHeight)
	{
		gc.fillRoundRectangle((int) x, (int) y, (int) Math.ceil(width), (int) Math.ceil(height), (int) Math.ceil(arcWidth), (int) Math.ceil(arcHeight));
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
		return new Rectangle(gc.getClipping());
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
		org.eclipse.swt.graphics.Font swtFont = gc.getFont();
		if(swtFont != currentSWTFont)
			disposeCurrentFont();
		Font font = new Font(swtFont);
		return font;
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
		return isToDs(gc.getLineDash());
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
		gc.setClipping((int) x, (int) y, (int) Math.ceil(width), (int) Math.ceil(height));
	}
	public void setClipping(Path path)
	{
		gc.setClipping(path.toSWTPath(getDevice()));
	}
	public void setClipping(Rectangle rect)
	{
		gc.setClipping(rect == null ? null : rect.toSWTRectanlge());
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
		disposeCurrentFont();
		org.eclipse.swt.graphics.Font swtFont = font.toSWTFont(getDevice());
		gc.setFont(swtFont);
		currentSWTFont = swtFont;
		currentFont = font;
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
		gc.setLineDash(dsToIs(dashes));
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
		gc.setLineWidth((int) lineWidth);
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
		return new Point(gc.stringExtent(string));
	}
	public Point textExtent(String string)
	{
		return new Point(gc.textExtent(string));
	}
	public Point textExtent(String string, int flags)
	{
		return new Point(gc.textExtent(string, flags));
	}
	private int[] dsToIs(double[] ds)
	{
		int[] is = new int[ds.length];
		for(int i = 0; i < ds.length; i ++)
			is[i] = (int) ds[i];
		return is;
	}
	private double[] isToDs(int[] is)
	{
		double[] ds = new double[is.length];
		for(int i = 0; i < is.length; i ++)
			ds[i] = is[i];
		return ds;
	}
	private void disposeCurrentFont()
	{
		if(currentSWTFont != null)
		{
			currentFont.disposeSWTFont(currentSWTFont);
			currentSWTFont = null;
			currentFont = null;
		}
	}
	public void disposeThisLayer()
	{
		disposeCurrentFont();
	}
}