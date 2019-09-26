package net.haspamelodica.swt.helper.input;

import java.text.DecimalFormatSymbols;
import java.util.function.Function;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class NumberInput<N extends Number> extends Input<N>
{
	private String				unitWithLeadingSpace, unit;
	private Function<String, N>	stringToNMapper;
	private Function<N, String>	nTostringMapper;
	private int					precision	= -1;

	public NumberInput(Composite parent, boolean isNonIntegral)
	{
		this(parent, SWT.NONE, isNonIntegral);
	}
	public NumberInput(Composite parent, int style, boolean isNonIntegral)
	{
		super(parent, style);
		super.setStringToTMapper(s ->
		{
			s = s.trim();
			if(unit != null && s.endsWith(unit))
				s = s.substring(0, s.length() - unit.length()).trim();
			s = s.replace(',', '.');
			try
			{
				return stringToNMapper.apply(s);
			} catch(NumberFormatException e)
			{
				return null;//sign for invalid input
			}
		});
		super.setTToStringMapper(n -> nTostringMapper.apply(n) + (unit == null ? "" : unitWithLeadingSpace));
		char decimalSeparator = DecimalFormatSymbols.getInstance().getDecimalSeparator();
		nTostringMapper = n -> precision == -1 ? String.valueOf(n).replace('.', decimalSeparator) : String.format(isNonIntegral ? "%." + precision + "f" : "%d", n);
		setErrorContent("NaN");
	}
	public Input<N> setStringToTMapper(Function<String, N> stringToN)
	{
		stringToNMapper = stringToN;
		return this;
	}
	public Function<String, N> getStringToTMapper()
	{
		return stringToNMapper;
	}
	public Input<N> setTToStringMapper(Function<N, String> nToString)
	{
		nTostringMapper = nToString;
		return this;
	}
	public Function<N, String> getTToStringMapper()
	{
		return nTostringMapper;
	}
	/**
	 * A value of -1 means unbounded precision and use E-notation.
	 */
	public void setPrecision(int precision)
	{
		this.precision = precision;
	}
	public int getPrecision()
	{
		return precision;
	}
	public NumberInput<N> setUnit(String unit)
	{
		unit = unit == null ? "" : unit;
		this.unit = unit;
		unitWithLeadingSpace = "".equals(unit) ? "" : ' ' + unit;
		setErrorContent("NaN" + unitWithLeadingSpace);
		setValue(getValue());//update
		return this;
	}
}