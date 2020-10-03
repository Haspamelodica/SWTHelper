package net.haspamelodica.swt.helper.input;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class IntegerInput extends NumberInput<Integer>
{
	public IntegerInput(Composite parent)
	{
		this(parent, SWT.NONE);
	}
	public IntegerInput(Composite parent, int style)
	{
		super(parent, style, false);
		setStringToTMapper(Integer::valueOf);
	}
}