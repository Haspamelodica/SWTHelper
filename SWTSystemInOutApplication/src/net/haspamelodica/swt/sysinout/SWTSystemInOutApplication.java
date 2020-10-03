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

@SuppressWarnings("synthetic-access")
public abstract class SWTSystemInOutApplication
{
	private static final boolean	ECHO_SYSTEM_MESSAGES_TO_REAL_SYSERR	= false;
	private static final boolean	ECHO_SYSOUT_TO_REAL_SYSOUT			= false;
	private static final boolean	ECHO_SYSERR_TO_REAL_SYSERR			= false;
	private static final boolean	ECHO_SYSIN_TO_REAL_SYSOUT			= false;

	private ByteQueue	userInputBuffer	= new ByteQueue();
	private ByteList	outputBuffer	= new ByteList();
	private boolean		changed			= true;

	private Display				display;
	private StyledText			output;
	private List<StyleRange>	ranges	= new ArrayList<>();
	private Color				sysinForegroundColor;
	private Color				sysinBackgroundColor;
	private Color				syserrForegroundColor;
	private Color				syserrBackgroundColor;
	private Color				systemMessagesForegroundColor;
	private Color				systemMessagesBackgroundColor;

	private PrintStream	realSystemOut;
	private PrintStream	realSystemErr;
	@SuppressWarnings("unused")
	private InputStream	realSystemIn;

	public void startApplication()
	{
		Shell shell = initGUI();
		setSysInAndOut();
		new Thread(() ->
		{
			try
			{
				run();
				printSystemMessage("Application exited");
			} catch(Throwable e)
			{
				printSystemMessage("Application crashed:");
				e.printStackTrace();
			}
		}, "ApplicationThread").start();
		runGUILoop(shell);
		display.dispose();
		System.exit(0);
	}
	public abstract void run() throws Throwable;
	private Shell initGUI()
	{
		display = new Display();
		sysinForegroundColor = display.getSystemColor(SWT.COLOR_GREEN);
		sysinBackgroundColor = display.getSystemColor(SWT.COLOR_WHITE);
		syserrForegroundColor = display.getSystemColor(SWT.COLOR_RED);
		syserrBackgroundColor = display.getSystemColor(SWT.COLOR_WHITE);
		systemMessagesForegroundColor = display.getSystemColor(SWT.COLOR_BLUE);
		systemMessagesBackgroundColor = display.getSystemColor(SWT.COLOR_WHITE);
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
			if(!display.readAndDispatch())
				display.sleep();
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
				inputText.setText("");
				byte[] bytes = text.getBytes();
				userInputBuffer.offer(bytes);
				synchronized(outputBuffer)
				{
					int start = outputBuffer.size();
					outputBuffer.add(bytes);
					ranges.add(new StyleRange(start, text.length(), sysinForegroundColor, sysinBackgroundColor));
				}
				changed = true;
				redrawOutputIfNeeded();
			}
		});
	}
	private void setSysInAndOut()
	{
		saveRealSystemInErrOut();
		setFakeSystemOut();
		setFakeSystemErr();
		setFakeSystemIn();
	}
	private void saveRealSystemInErrOut()
	{
		realSystemOut = System.out;
		realSystemErr = System.err;
		realSystemIn = System.in;
	}
	@SuppressWarnings("resource") //the created stream neither should nor needs to be closed
	private void setFakeSystemOut()
	{
		System.setOut(createFakeSystemOut());
	}
	@SuppressWarnings("resource") //the created stream neither should nor needs to be closed
	private void setFakeSystemErr()
	{
		System.setErr(createFakeSystemErr());
	}
	@SuppressWarnings("resource") //the created stream neither should nor needs to be closed
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
				printSysoutByte(b);
			}
		});
	}
	private PrintStream createFakeSystemErr()
	{
		return new PrintStream(new OutputStream()
		{
			@Override
			public void write(int b) throws IOException
			{
				printSyserrByte(b);
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
				return readSysinByte();
			}
			@Override
			public int read(byte[] b, int off, int len) throws IOException
			{
				return readSysinByteArr(b, off, len);
			}
		};
	}
	private void printSysoutByte(int b)
	{
		synchronized(outputBuffer)
		{
			if(ECHO_SYSOUT_TO_REAL_SYSOUT)
			{
				realSystemOut.write(b);
				realSystemOut.flush();
			}
			outputBuffer.add((byte) b);
		}
		changed = true;
		execSWTSafe(() -> redrawOutputIfNeeded());
	}
	private void printSyserrByte(int b)
	{
		synchronized(outputBuffer)
		{
			if(ECHO_SYSERR_TO_REAL_SYSERR)
			{
				realSystemErr.write(b);
				realSystemErr.flush();
			}
			int start = outputBuffer.size();
			outputBuffer.add((byte) b);
			ranges.add(new StyleRange(start, 1, syserrForegroundColor, syserrBackgroundColor));
		}
		changed = true;
		execSWTSafe(() -> redrawOutputIfNeeded());
	}
	private void printSystemMessage(String message)
	{
		String messageWithCRLF = message + "\r\n";
		synchronized(outputBuffer)
		{
			if(ECHO_SYSTEM_MESSAGES_TO_REAL_SYSERR)
			{
				realSystemErr.print(messageWithCRLF);
				realSystemErr.flush();
			}
			int start = outputBuffer.size();
			outputBuffer.add(messageWithCRLF.getBytes());
			ranges.add(new StyleRange(start, messageWithCRLF.length(), systemMessagesForegroundColor, systemMessagesBackgroundColor));
		}
		changed = true;
		execSWTSafe(() -> redrawOutputIfNeeded());
	}
	private int readSysinByte()
	{
		int retVal = -1;
		do
			try
			{
				retVal = userInputBuffer.pollBlocking() & 0xFF;
			} catch(@SuppressWarnings("unused") InterruptedException e)
			{
				//nothing to do; retVal still is -1
			}
		while(retVal == -1);
		if(ECHO_SYSIN_TO_REAL_SYSOUT)
		{
			realSystemOut.write(retVal);
			realSystemOut.flush();
		}
		return retVal;
	}
	private int readSysinByteArr(byte[] b, int off, int len)
	{
		for(;;)
			try
			{
				int polled = userInputBuffer.pollSemiBlocking(b, off, len);
				if(ECHO_SYSIN_TO_REAL_SYSOUT)
				{
					realSystemOut.write(b, off, polled);
					realSystemOut.flush();
				}
				return polled;
			} catch(@SuppressWarnings("unused") InterruptedException e)
			{
				//nothing to do; try again
			}
	}
	private void redrawOutputIfNeeded()
	{
		if(changed)
		{
			changed = false;
			synchronized(outputBuffer)
			{
				output.setText(new String(outputBuffer.array(), 0, outputBuffer.size()));
				output.setStyleRanges(ranges.toArray(new StyleRange[0]));
			}
		}
	}
	private void execSWTSafe(Runnable r)
	{
		display.asyncExec(r);
	}

	public static <X extends Throwable> void run(MainMethod<X> main, String... args)
	{
		new SWTSystemInOutApplication()
		{
			@Override
			public void run() throws Throwable
			{
				main.main(args);
			}
		}.startApplication();
	}

	public static interface MainMethod<X extends Throwable>
	{
		public void main(String... args) throws X;
	}
}