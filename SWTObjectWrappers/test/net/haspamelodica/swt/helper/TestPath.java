package net.haspamelodica.swt.helper;

import java.util.Arrays;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.PathData;
import org.eclipse.swt.widgets.Display;

public class TestPath
{
	public static void main(String[] args)
	{
		printPathDataPathBased(p ->
		{
			p.addArc(1, 2, 4, 9, 0, -180);
		});
		System.out.println();
		printPathData(d ->
		{
			d.types = new byte[] {SWT.PATH_MOVE_TO, SWT.PATH_CUBIC_TO, SWT.PATH_CUBIC_TO};
			d.points = new float[] {
					5, 6.5f,
					5, 8.98528f, 4.1045694f, 10.1f, 3, 10.1f,
					1.8954318f, 11, 1, 8.985284f, 1, 6.5f};
		});
	}
	private static void printPathDataPathBased(Consumer<Path> setupPath)
	{
		printPathData(outData ->
		{
			Display display = new Display();
			Path path = new Path(display);
			setupPath.accept(path);
			PathData pathData = path.getPathData();
			outData.points = pathData.points;
			outData.types = pathData.types;
			path.dispose();
			display.dispose();
		});
	}
	private static void printPathData(Consumer<PathData> setupData)
	{
		PathData data = new PathData();
		setupData.accept(data);

		System.out.println("points: " + Arrays.toString(data.points));

		byte[] types = data.types;
		System.out.print("types:  ");
		if(types == null)
			System.out.print("null");
		else
		{
			System.out.print('[');
			if(types.length != 0)
			{
				System.out.print(t(types[0]));
				for(int i = 1; i < types.length; i ++)
				{
					System.out.print(", ");
					System.out.print(t(types[i]));
				}
			}
			System.out.print(']');
		}
		System.out.println();
	}
	private static String t(byte b)
	{
		switch(b)
		{
			case SWT.PATH_CLOSE:
				return "PATH_CLOSE";
			case SWT.PATH_CUBIC_TO:
				return "PATH_CUBIC_TO";
			case SWT.PATH_LINE_TO:
				return "PATH_LINE_TO";
			case SWT.PATH_MOVE_TO:
				return "PATH_MOVE_TO";
			case SWT.PATH_QUAD_TO:
				return "PATH_QUAD_TO";
			default:
				return "<unknown: " + b + ">";
		}
	}
}