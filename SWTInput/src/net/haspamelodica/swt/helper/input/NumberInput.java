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
			String trimmed = s.trim();
			if(unit != null && trimmed.endsWith(unit))
				trimmed = trimmed.substring(0, trimmed.length() - unit.length()).trim();
			trimmed = trimmed.replace(',', '.');
			try
			{
				return stringToNMapper.apply(trimmed);
			} catch(@SuppressWarnings("unused") NumberFormatException e)
			{
				return null;//sign for invalid input
			}
		});
		super.setTToStringMapper(n -> nTostringMapper.apply(n) + (unit == null ? "" : unitWithLeadingSpace));
		char decimalSeparator = DecimalFormatSymbols.getInstance().getDecimalSeparator();
		nTostringMapper = n -> precision == -1 ? String.valueOf(n).replace('.', decimalSeparator) : String.format(isNonIntegral ? "%." + precision + "f" : "%d", n);
		setErrorContent("NaN");
	}
	@Override
	public Input<N> setStringToTMapper(Function<String, N> stringToN)
	{
		stringToNMapper = stringToN;
		return this;
	}
	@Override
	public Function<String, N> getStringToTMapper()
	{
		return stringToNMapper;
	}
	@Override
	public Input<N> setTToStringMapper(Function<N, String> nToString)
	{
		nTostringMapper = nToString;
		return this;
	}
	@Override
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
		String unitNotNull = unit == null ? "" : unit;
		this.unit = unitNotNull;
		unitWithLeadingSpace = "".equals(unitNotNull) ? "" : ' ' + unitNotNull;
		setErrorContent("NaN" + unitWithLeadingSpace);
		setValue(getValue());//update
		return this;
	}
}