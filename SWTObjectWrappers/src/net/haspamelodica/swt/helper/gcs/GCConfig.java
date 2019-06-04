package net.haspamelodica.swt.helper.gcs;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Pattern;

import net.haspamelodica.swt.helper.swtobjectwrappers.Font;
import net.haspamelodica.swt.helper.swtobjectwrappers.Rectangle;

public class GCConfig
{
	//private final boolean	advanced;
	private final int		alpha;
	private final int		antialias;
	private final Color		background;
	private final Pattern	backgroundPattern;
	private final int		fillRule;
	private final Font		font;
	private final Color		foreground;
	private final Pattern	foregroundPattern;
	private final int		interpolation;
	private final int		lineCap;
	private final int		lineJoin;
	private final int		lineStyle;
	private final double	lineWidth;
	private final int		textAntialias;
	private final boolean	xorMode;

	public GCConfig(GC gc)
	{
		this(new SWTGC(gc), true);
	}
	public GCConfig(GeneralGC gc)
	{
		this(gc, false);
	}
	private GCConfig(GeneralGC gc, boolean disposeAfterwards)
	{
		//advanced = gc.getAdvanced();
		alpha = gc.getAlpha();
		antialias = gc.getAntialias();
		background = gc.getBackground();
		backgroundPattern = gc.getBackgroundPattern();
		//clipping
		fillRule = gc.getFillRule();
		font = gc.getFont();
		foreground = gc.getForeground();
		foregroundPattern = gc.getForegroundPattern();
		interpolation = gc.getInterpolation();
		lineCap = gc.getLineCap();
		lineJoin = gc.getLineJoin();
		lineStyle = gc.getLineStyle();
		lineWidth = gc.getLineWidth();
		textAntialias = gc.getTextAntialias();
		//transform
		xorMode = gc.getXORMode();
		if(disposeAfterwards)
			gc.disposeThisLayer();
	}
	@SuppressWarnings("deprecation")
	public void reset(GeneralGC gc)
	{
		//gc.setAdvanced(advanced);//don't reset advanced mode
		gc.setAlpha(alpha);
		gc.setAntialias(antialias);
		gc.setBackground(background);
		gc.setBackgroundPattern(backgroundPattern);
		gc.setClipping((Rectangle) null);
		gc.setFillRule(fillRule);
		gc.setFont(font);
		gc.setForeground(foreground);
		gc.setForegroundPattern(foregroundPattern);
		gc.setInterpolation(interpolation);
		gc.setLineCap(lineCap);
		gc.setLineJoin(lineJoin);
		gc.setLineStyle(lineStyle);
		gc.setLineWidth(lineWidth);
		gc.setTextAntialias(textAntialias);
		gc.setTransform(null);
		gc.setXORMode(xorMode);
	}
}