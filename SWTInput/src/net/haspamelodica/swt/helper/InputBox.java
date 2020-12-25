package net.haspamelodica.swt.helper;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import net.haspamelodica.swt.helper.input.FloatInput;
import net.haspamelodica.swt.helper.input.Input;
import net.haspamelodica.swt.helper.input.IntegerInput;
import net.haspamelodica.swt.helper.input.StringInput;

/**
 * Wrapper around an user input mechanism (e.g. a {@link Input}) presenting the input mechanism in a dialog with title and explaining text.
 * <p>
 *  You may specify the user input mechanism as {@link Input} class or
 *  as implementation of the {@link #initInput(Composite, Object, Consumer)} method.
 */
public abstract class InputBox<O>
{
	public final static InputBox<String>	textSingleInput;
	public final static InputBox<String>	textMultiInput;
	public final static InputBox<Integer>	intInput;
	public final static InputBox<Float>		floatInput;
	static
	{
		textSingleInput = createInputBased(StringInput::new);
		textMultiInput = create((parent, hint) ->
		{
			parent.setLayout(new GridLayout());
			Text input = new Text(parent, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
			input.setMessage(hint);
			GridData inputData = new GridData(SWT.FILL, SWT.FILL, true, true);
			inputData.widthHint = 200;
			inputData.heightHint = 100;
			input.setLayoutData(inputData);
			return input::getText;
		});
		intInput = createInputBased(IntegerInput::new);
		floatInput = createInputBased(FloatInput::new);
	}

	/**
	 * Convenience method to open a dialog obtaining a string value from the user.
	 */
	public static String textSingleInput(Shell parent, String title, String message, String hint)
	{
		return textSingleInput.input(parent, title, message, hint);
	}

	/**
	 * Convenience method to open a dialog obtaining a string value from the user.
	 */
	public static String textMultiInput(Shell parent, String title, String message, String hint)
	{
		return textMultiInput.input(parent, title, message, hint);
	}
	
	/**
	 * Convenience method to open a dialog obtaining an integer value from the user.
	 */
	public static Integer intInput(Shell parent, String title, String message, Integer hint)
	{
		return intInput.input(parent, title, message, hint);
	}

	
	/**
	 * Convenience method to open a dialog obtaining a float value from the user.
	 */
	public static Float floatInput(Shell parent, String title, String message, Float hint)
	{
		return floatInput.input(parent, title, message, hint);
	}
	
	/**
	 * Convenience method to create an {@link InputBox} instance from an implementation 
	 * of the {@link #initInput(Composite, Object, Consumer)} method, i.e. from an explicit 
	 * implementation of the input mechanism to embed.
	 * 
	 * See {@link #initInput(Composite, Object, Consumer)} for more details on the 
	 * requirements for a proper input mechanism implementation.
	 */
	public static <O> InputBox<O> create(BiFunction<Composite, O, Supplier<O>> initInput)
	{
		return new InputBox<O>()
		{
			@Override
			protected Supplier<O> initInput(Composite parent, O hint, Consumer<O> confirm)
			{
				return initInput.apply(parent, hint);
			}
		};
	}
	
	
	/**
	 * Convenience method to create an {@link InputBox} instance from a given {@link Input}
	 * class. 
	 * 
	 * @param initInput The constructor of the required Input class.
	 */
	public static <O> InputBox<O> createInputBased(Function<Composite, Input<O>> initInput)
	{
		return new InputBox<O>()
		{
			@Override
			protected Supplier<O> initInput(Composite parent, O hint, Consumer<O> confirm)
			{
				parent.setLayout(new GridLayout());
				Input<O> input = initInput.apply(parent);
				input.setValue(hint);
				input.addManualConfirmListener(confirm);
				GridData inputData = new GridData(SWT.FILL, SWT.FILL, true, true);
				inputData.widthHint = 200;
				input.setLayoutData(inputData);
				return input::getValue;
			}
		};
	}

	/**
	 * Open the dialog.
	 * 
	 * @param parent The parent shell for the dialog.
	 * @param title The title of the dialog.
	 * @param message The explaining message in the dialog.
	 * @param hint The default value set in the input control. 
	 * @return The value given by the user via the input control.
	 */
	public O input(Shell parent, String title, String message, O hint)
	{
		@SuppressWarnings("unchecked")
		O[] userInputArr = (O[]) new Object[1];
		Shell dialogShell = initInputShell(title, message, hint, parent, userInputArr);
		openAsMessageBox(parent, dialogShell);
		return userInputArr[0];
	}
	
	
	private Shell initInputShell(String title, String message, O hint, Shell parentShell, O[] userInputArr)
	{
		Shell dialogShell = new Shell(parentShell, SWT.BORDER | SWT.CLOSE | SWT.RESIZE);
		dialogShell.setText(title);
		initInputShellContents(message, hint, userInputArr, dialogShell);
		dialogShell.pack();
		moveDialogShellCenterToParentShellCenterClipped(parentShell, dialogShell);
		return dialogShell;
	}
	private void initInputShellContents(String message, O hint, O[] userInputArr, Composite parent)
	{
		parent.setLayout(new GridLayout(2, false));
		Label msgLabel = new Label(parent, SWT.NONE);
		Composite inputParent = new Composite(parent, SWT.NONE);
		inputParent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		Consumer<O> confirm = o ->
		{
			userInputArr[0] = o;
			parent.dispose();
		};
		Supplier<O> input = initInput(inputParent, hint, confirm);
		Button ok = new Button(parent, SWT.PUSH);
		Button cancel = new Button(parent, SWT.PUSH);
		msgLabel.setText(message);
		ok.setText("OK");
		ok.addListener(SWT.Selection, e -> confirm.accept(input.get()));
		cancel.setText("Cancel");
		cancel.addListener(SWT.Selection, e -> parent.dispose());
		msgLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
	}
	
	/**
	 * Create the input mechanism to be embedded in the dialog. 
	 * 
	 * Consider {@link #create(BiFunction)} or {@link #createInputBased(Function)}  
	 * instead of implementing a subclass of your own.  
	 * 
	 * @param parent The composite where the mechanism might embed required controls. 
	 * @param hint The default value to be presented initially by the input mechanism. 
	 * @param confirm A method that the input mechanism may call if the user confirms a value inside the input mechanism. The method must return the value given by the user.
	 * Confirming a value there will keep the value as valid result even if the user cancels the dialog later. 
	 * @return A method to obtain the value from the input mechanism. 
	 * This is called if the user closes the wrapping dialog with 'OK'. It will replace the value set by any previous calls to the confirm method.
	 */
	protected abstract Supplier<O> initInput(Composite parent, O hint, Consumer<O> confirm);

	private static void openAsMessageBox(Shell parent, Shell dialogShell)
	{
		Display display = parent.getDisplay();
		parent.setEnabled(false);
		dialogShell.open();
		while(!dialogShell.isDisposed())
			if(!display.readAndDispatch())
				display.sleep();
		parent.setEnabled(true);
	}
	private static void moveDialogShellCenterToParentShellCenterClipped(Shell parentShell, Shell dialogShell)
	{
		Rectangle displayBounds = parentShell.getDisplay().getBounds();
		Rectangle parentBounds = parentShell.getBounds();
		Point dialogSize = dialogShell.getSize();
		int displayW = displayBounds.width;
		int displayH = displayBounds.height;
		int parentX = parentBounds.x;
		int parentW = parentBounds.width;
		int parentY = parentBounds.y;
		int parentH = parentBounds.height;
		int dialogW = dialogSize.x;
		int dialogH = dialogSize.y;
		int dialogX = parentX + parentW / 2 - dialogW / 2;
		int dialogY = parentY + parentH / 2 - dialogH / 2;
		dialogX = Math.min(dialogX, displayW - dialogW);
		dialogY = Math.min(dialogY, displayH - dialogH);
		dialogX = Math.max(dialogX, 0);
		dialogY = Math.max(dialogY, 0);
		dialogShell.setLocation(dialogX, dialogY);
	}
}