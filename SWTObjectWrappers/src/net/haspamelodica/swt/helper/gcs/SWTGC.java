package net.haspamelodica.swt.helper.gcs;

import java.util.Arrays;

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

	private double		currentLineWidth;
	private double[]	currentLineDashes;
	private int[]		currentLineDashesInts;

	public SWTGC(GC gc)
	{
		this.gc = gc;
	}
	@Override
	public void copyArea(Image image, double x, double y)
	{
		gc.copyArea(image, (int) x, (int) y);
	}
	@Override
	public void copyArea(double srcX, double srcY, double width, double height, double destX, double destY)
	{
		gc.copyArea((int) srcX, (int) srcY, (int) Math.ceil(width), (int) Math.ceil(height), (int) destX, (int) destY);
	}
	@Override
	public void copyArea(double srcX, double srcY, double width, double height, double destX, double destY, boolean paint)
	{
		gc.copyArea((int) srcX, (int) srcY, (int) Math.ceil(width), (int) Math.ceil(height), (int) destX, (int) destY, paint);
	}
	@Override
	public void drawArc(double x, double y, double width, double height, double startAngle, double arcAngle)
	{
		gc.drawArc((int) x, (int) y, (int) Math.ceil(width), (int) Math.ceil(height), (int) startAngle, (int) Math.ceil(arcAngle));
	}
	@Override
	public void drawFocus(double x, double y, double width, double height)
	{
		gc.drawFocus((int) x, (int) y, (int) Math.ceil(width), (int) Math.ceil(height));
	}
	@Override
	public void drawImage(Image image, double x, double y)
	{
		gc.drawImage(image, (int) x, (int) y);
	}
	@Override
	public void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, double destX, double destY, double destWidth, double destHeight)
	{
		gc.drawImage(image, srcX, srcY, srcWidth, srcHeight, (int) destX, (int) destY, (int) Math.ceil(destWidth), (int) Math.ceil(destHeight));
	}
	@Override
	public void drawLine(double x1, double y1, double x2, double y2)
	{
		gc.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	}
	@Override
	public void drawOval(double x, double y, double width, double height)
	{
		gc.drawOval((int) x, (int) y, (int) Math.ceil(width), (int) Math.ceil(height));
	}
	@Override
	public void drawPath(Path path)
	{
		gc.drawPath(path.toSWTPath(getDevice()));
	}
	@Override
	public void drawPoint(double x, double y)
	{
		gc.drawPoint((int) x, (int) y);
	}
	@Override
	public void drawPolygon(double[] pointArray)
	{
		gc.drawPolygon(dsToIs(pointArray));
	}
	@Override
	public void drawPolyline(double[] pointArray)
	{
		gc.drawPolyline(dsToIs(pointArray));
	}
	@Override
	public void drawRectangle(double x, double y, double width, double height)
	{
		gc.drawRectangle((int) x, (int) y, (int) Math.ceil(width), (int) Math.ceil(height));
	}
	@Override
	public void drawRectangle(Rectangle rect)
	{
		gc.drawRectangle(rect.toSWTRectanlge());
	}
	@Override
	public void drawRoundRectangle(double x, double y, double width, double height, double arcWidth, double arcHeight)
	{
		gc.drawRoundRectangle((int) x, (int) y, (int) Math.ceil(width), (int) Math.ceil(height), (int) Math.ceil(arcWidth), (int) Math.ceil(arcHeight));
	}
	@Override
	public void drawString(String string, double x, double y)
	{
		gc.drawString(string, (int) x, (int) y);
	}
	@Override
	public void drawString(String string, double x, double y, boolean isTransparent)
	{
		gc.drawString(string, (int) x, (int) y, isTransparent);
	}
	@Override
	public void drawText(String string, double x, double y)
	{
		gc.drawText(string, (int) x, (int) y);
	}
	@Override
	public void drawText(String string, double x, double y, boolean isTransparent)
	{
		gc.drawText(string, (int) x, (int) y, isTransparent);
	}
	@Override
	public void drawText(String string, double x, double y, int flags)
	{
		gc.drawText(string, (int) x, (int) y, flags);
	}
	@Override
	public void fillArc(double x, double y, double width, double height, double startAngle, double arcAngle)
	{
		gc.fillArc((int) x, (int) y, (int) Math.ceil(width), (int) Math.ceil(height), (int) startAngle, (int) Math.ceil(arcAngle));
	}
	@Override
	public void fillGradientRectangle(double x, double y, double width, double height, boolean vertical)
	{
		gc.fillGradientRectangle((int) x, (int) y, (int) Math.ceil(width), (int) Math.ceil(height), vertical);
	}
	@Override
	public void fillOval(double x, double y, double width, double height)
	{
		gc.fillOval((int) x, (int) y, (int) Math.ceil(width), (int) Math.ceil(height));
	}
	@Override
	public void fillPath(Path path)
	{
		gc.fillPath(path.toSWTPath(getDevice()));
	}
	@Override
	public void fillPolygon(double[] pointArray)
	{
		gc.fillPolygon(dsToIs(pointArray));
	}
	@Override
	public void fillRectangle(double x, double y, double width, double height)
	{
		gc.fillRectangle((int) x, (int) y, (int) Math.ceil(width), (int) Math.ceil(height));
	}
	@Override
	public void fillRectangle(Rectangle rect)
	{
		gc.fillRectangle(rect.toSWTRectanlge());
	}
	@Override
	public void fillRoundRectangle(double x, double y, double width, double height, double arcWidth, double arcHeight)
	{
		gc.fillRoundRectangle((int) x, (int) y, (int) Math.ceil(width), (int) Math.ceil(height), (int) Math.ceil(arcWidth), (int) Math.ceil(arcHeight));
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
		return new Rectangle(gc.getClipping());
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
		org.eclipse.swt.graphics.Font swtFont = gc.getFont();
		if(swtFont != currentSWTFont)
			disposeCurrentFont();
		return currentFont == null ? new Font(swtFont) : currentFont;
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
		int[] actualDashesInts = gc.getLineDash();
		if(!Arrays.equals(actualDashesInts, currentLineDashesInts))
		{
			currentLineDashesInts = actualDashesInts;
			currentLineDashes = isToDs(actualDashesInts);
		}
		return currentLineDashes;
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
		int actualLineWidth = gc.getLineWidth();
		if(actualLineWidth != (int) currentLineWidth)
			currentLineWidth = actualLineWidth;
		return currentLineWidth;
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
		gc.setClipping((int) x, (int) y, (int) Math.ceil(width), (int) Math.ceil(height));
	}
	@Override
	public void setClipping(Path path)
	{
		gc.setClipping(path.toSWTPath(getDevice()));
	}
	@Override
	public void setClipping(Rectangle rect)
	{
		gc.setClipping(rect == null ? null : rect.toSWTRectanlge());
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
		disposeCurrentFont();
		org.eclipse.swt.graphics.Font swtFont = font.toSWTFont(getDevice());
		gc.setFont(swtFont);
		currentSWTFont = swtFont;
		currentFont = font;
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
		currentLineDashes = dashes;
		int[] dashesInts = dsToIs(dashes);
		if(dashesInts != null)
			for(int i = 0; i < dashesInts.length; i ++)
				if(dashesInts[i] <= 0)
					dashesInts[i] = 1;
		currentLineDashesInts = dashesInts;
		gc.setLineDash(dashesInts);
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
		currentLineWidth = lineWidth;
		gc.setLineWidth((int) lineWidth);
	}
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
		return new Point(gc.stringExtent(string));
	}
	@Override
	public Point textExtent(String string)
	{
		return new Point(gc.textExtent(string));
	}
	@Override
	public Point textExtent(String string, int flags)
	{
		return new Point(gc.textExtent(string, flags));
	}
	private static int[] dsToIs(double[] ds)
	{
		if(ds == null)
			return null;
		int[] is = new int[ds.length];
		for(int i = 0; i < ds.length; i ++)
			is[i] = (int) ds[i];
		return is;
	}
	private static double[] isToDs(int[] is)
	{
		if(is == null)
			return null;
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
	@Override
	public void disposeThisLayer()
	{
		disposeCurrentFont();
	}
}