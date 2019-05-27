package net.haspamelodica.swt.helper.gcs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

import net.haspamelodica.swt.helper.swtobjectwrappers.Font;
import net.haspamelodica.swt.helper.swtobjectwrappers.Rectangle;

public class GCDefaultConfig
{
	//private final boolean	gcDefaultAdvanced;
	private final Color		gcDefaultBackground;
	private final int		gcDefaultFillRule;
	private final Font		gcDefaultFont;
	private final Color		gcDefaultForeground;
	private final int		gcDefaultLineCap;
	private final int		gcDefaultLineJoin;
	private final int		gcDefaultLineStyle;
	private final double	gcDefaultLineWidth;
	private final boolean	gcDefaultXORMode;

	public GCDefaultConfig(GC gc)
	{
		this(new SWTGC(gc), true);
	}
	public GCDefaultConfig(GeneralGC gc)
	{
		this(gc, false);
	}
	private GCDefaultConfig(GeneralGC gc, boolean disposeAfterwards)
	{
		//gcDefaultAdvanced = gc.getAdvanced();
		gcDefaultBackground = gc.getBackground();
		gcDefaultFillRule = gc.getFillRule();
		gcDefaultFont = gc.getFont();
		gcDefaultForeground = gc.getForeground();
		gcDefaultLineCap = gc.getLineCap();
		gcDefaultLineJoin = gc.getLineJoin();
		gcDefaultLineStyle = gc.getLineStyle();
		gcDefaultLineWidth = gc.getLineWidth();
		gcDefaultXORMode = gc.getXORMode();
		if(disposeAfterwards)
			gc.disposeThisLayer();
	}
	@SuppressWarnings("deprecation")
	public void reset(GeneralGC gc)
	{
		//gc.setAdvanced(gcDefaultAdvanced);//don't reset advanced mode
		gc.setAlpha(255);
		gc.setAntialias(SWT.DEFAULT);
		gc.setBackground(gcDefaultBackground);
		gc.setBackgroundPattern(null);
		gc.setClipping((Rectangle) null);
		gc.setFillRule(gcDefaultFillRule);
		gc.setFont(gcDefaultFont);
		gc.setForeground(gcDefaultForeground);
		gc.setForegroundPattern(null);
		gc.setInterpolation(SWT.DEFAULT);
		gc.setLineCap(gcDefaultLineCap);
		gc.setLineJoin(gcDefaultLineJoin);
		gc.setLineStyle(gcDefaultLineStyle);
		gc.setLineWidth(gcDefaultLineWidth);
		gc.setTextAntialias(SWT.DEFAULT);
		gc.setTransform(null);
		gc.setXORMode(gcDefaultXORMode);
	}
}