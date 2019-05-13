package net.haspamelodica.swt.helper.swtobjectwrappers;

import org.eclipse.swt.graphics.Device;

public class Font
{
	private final String	name;
	private final double	height;
	private final int		style;

	public Font(String name, double height, int style)
	{
		this.name = name;
		this.height = height;
		this.style = style;
	}
	public Font(org.eclipse.swt.graphics.Font font)
	{
		this(font.getFontData()[0].getName(), font.getFontData()[0].getHeight(), font.getFontData()[0].getStyle());
	}

	public String getName()
	{
		return name;
	}
	public double getHeight()
	{
		return height;
	}
	public int getStyle()
	{
		return style;
	}

	public Font scale(double z)
	{
		return new Font(name, height * z, style);
	}
	public Font unscale(double z)
	{
		return new Font(name, height / z, style);
	}
	public org.eclipse.swt.graphics.Font toSWTFont(Device dev)
	{
		return new org.eclipse.swt.graphics.Font(dev, name, Math.max((int) height, 1), style);
	}
	public void disposeSWTFont(org.eclipse.swt.graphics.Font swtFont)
	{
		swtFont.dispose();
	}
}