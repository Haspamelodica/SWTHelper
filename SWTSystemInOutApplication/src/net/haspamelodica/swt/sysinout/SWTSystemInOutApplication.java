package net.haspamelodica.swt.sysinout;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public abstract class SWTSystemInOutApplication
{
	private StringBuilder	userInputBuffer	= new StringBuilder();
	private StringBuilder	outputBuffer	= new StringBuilder();
	private boolean			changed			= true;

	private Display				display;
	private StyledText			output;
	private List<StyleRange>	ranges	= new ArrayList<StyleRange>();
	private Color				userInputForegroundColor;
	private Color				userInputBackgroundColor;

	@SuppressWarnings("unused")
	private PrintStream	realSystemOut;
	@SuppressWarnings("unused")
	private InputStream	realSystemIn;

	public void startApplication()
	{
		Shell shell = initGUI();
		setSysInAndOut();
		new Thread(() ->
		{
			run();
			System.exit(0);
		}, "ApplicationThread").start();
		runGUILoop(shell);
		display.dispose();
		System.exit(0);
	}
	public abstract void run();
	private Shell initGUI()
	{
		display = new Display();
		userInputForegroundColor = display.getSystemColor(SWT.COLOR_GREEN);
		userInputBackgroundColor = display.getSystemColor(SWT.COLOR_WHITE);
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		createOutput(shell);
		createUserInputText(shell);
		shell.open();
		return shell;
	}
	private void runGUILoop(Shell shell)
	{
		while(!shell.isDisposed())
		{
			if(!display.readAndDispatch())
			{
				display.sleep();
			}
		}
	}
	private void createOutput(Shell shell)
	{
		output = new StyledText(shell, SWT.BORDER | SWT.V_SCROLL);
		output.setEditable(false);
		output.setAlwaysShowScrollBars(false);
		output.setFont(new Font(display, "Consolas", 10, SWT.NONE));
		output.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		output.setSize(1000, 300);
	}
	private void createUserInputText(Shell shell)
	{
		Text inputText = new Text(shell, SWT.BORDER);
		inputText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		inputText.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{
				String text = inputText.getText() + "\r\n";
				userInputBuffer.append(text);
				outputBuffer.append(text);
				changed = true;
				int start = output.getText().length();
				int length = text.length();
				ranges.add(new StyleRange(start, length, userInputForegroundColor, userInputBackgroundColor));
				redrawOutputIfNeeded();
				inputText.setText("");
			}
		});
	}
	private void setSysInAndOut()
	{
		saveRealSystemInOut();
		setFakeSystemOut();
		setFakeSystemIn();
	}
	private void saveRealSystemInOut()
	{
		realSystemOut = System.out;
		realSystemIn = System.in;
	}
	private void setFakeSystemOut()
	{
		System.setOut(createFakeSystemOut());
	}
	private void setFakeSystemIn()
	{
		System.setIn(createFakeSystemIn());
	}
	private PrintStream createFakeSystemOut()
	{
		return new PrintStream(new OutputStream()
		{
			@Override
			public void write(int b) throws IOException
			{
				//				realSystemOut.write(b);
				//				realSystemOut.flush();
				outputBuffer.append((char) b);
				changed = true;
				execSWTSafe(() -> redrawOutputIfNeeded());
			}

		});
	}
	private InputStream createFakeSystemIn()
	{
		return new InputStream()
		{
			@Override
			public int read() throws IOException
			{
				while(userInputBuffer.length() == 0)
				{
					sleep();
				}
				int retVal = userInputBuffer.charAt(0);
				userInputBuffer.deleteCharAt(0);
				//				realSystemOut.write(retVal);
				//				realSystemOut.flush();
				return retVal;
			}
			private void sleep()
			{
				try
				{
					Thread.sleep(1);
				} catch(InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			@Override
			public int read(byte[] b, int off, int len) throws IOException
			{
				while(userInputBuffer.length() == 0)
				{
					sleep();
				}
				len = Math.min(len, userInputBuffer.length());
				for(int i = 0; i < len; i ++)
				{
					b[i + off] = (byte) userInputBuffer.charAt(i);
				}
				userInputBuffer.delete(0, len);
				return len;
			}
		};
	}
	private void redrawOutputIfNeeded()
	{
		if(changed)
		{
			changed = false;
			output.setText(outputBuffer.toString());
			output.setStyleRanges(ranges.toArray(new StyleRange[0]));
		}
	}
	private void execSWTSafe(Runnable r)
	{
		display.asyncExec(r);
	}
}