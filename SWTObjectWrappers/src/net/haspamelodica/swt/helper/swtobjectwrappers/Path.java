package net.haspamelodica.swt.helper.swtobjectwrappers;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.PathData;

public class Path
{
	private static final byte	CLOSE		= SWT.PATH_CLOSE;
	private static final byte	CUBIC_TO	= SWT.PATH_CUBIC_TO;
	private static final byte	LINE_TO		= SWT.PATH_LINE_TO;
	private static final byte	MOVE_TO		= SWT.PATH_MOVE_TO;
	private static final byte	QUAD_TO		= SWT.PATH_QUAD_TO;

	private static final int	POINTS_LENGTH_OVERHEAD	= 20;
	private static final int	TYPES_LENGTH_OVERHEAD	= 5;

	private double[]	points;
	private byte[]		types;
	private int			pointsCount;
	private int			typesCount;
	private boolean		dontEditPoints;
	private boolean		dontEditTypes;


	public Path()
	{
		this(new double[2 + POINTS_LENGTH_OVERHEAD], new byte[1 + TYPES_LENGTH_OVERHEAD], 0, 0, false, false);
		moveToWithoutCheck(0, 0);
	}
	public Path(double[] points, byte[] types, int pointsCount, int typesCount, boolean dontEditPoints, boolean dontEditTypes)
	{
		this.points = points;
		this.types = types;
		this.pointsCount = pointsCount;
		this.typesCount = typesCount;
		this.dontEditPoints = dontEditPoints;
		this.dontEditTypes = dontEditTypes;
	}

	//	/**
	//	 * Adds to the receiver a circular or elliptical arc that lies within
	//	 * the specified rectangular area.
	//	 * <p>
	//	 * The resulting arc begins at <code>startAngle</code> and extends
	//	 * for <code>arcAngle</code> degrees.
	//	 * Angles are interpreted such that 0 degrees is at the 3 o'clock
	//	 * position. A positive value indicates a counter-clockwise rotation
	//	 * while a negative value indicates a clockwise rotation.
	//	 * </p>
	//	 * <p>
	//	 * The center of the arc is the center of the rectangle whose origin
	//	 * is (<code>x</code>, <code>y</code>) and whose size is specified by the
	//	 * <code>width</code> and <code>height</code> arguments.
	//	 * </p>
	//	 * <p>
	//	 * The resulting arc covers an area <code>width + 1</code> pixels wide
	//	 * by <code>height + 1</code> pixels tall.
	//	 * </p>
	//	 *
	//	 * @param     x            the x coordinate of the upper-left corner of the arc
	//	 * @param     y            the y coordinate of the upper-left corner of the arc
	//	 * @param     width        the width of the arc
	//	 * @param     height       the height of the arc
	//	 * @param     startAngle   the beginning angle
	//	 * @param     arcAngle     the angular extent of the arc, relative to the start angle
	//	 *
	//	 * @exception SWTException
	//	 *                             <ul>
	//	 *                             <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
	//	 *                             </ul>
	//	 */
	//	public void addArc(double x, double y, double width, double height, double startAngle, double arcAngle)
	//	{
	//		throw new IllegalStateException("unimplemented");
	//	}
	/**
	 * Adds to the receiver the path described by the parameter.
	 *
	 * @param     path                     the path to add to the receiver
	 *
	 * @exception IllegalArgumentException
	 *                                         <ul>
	 *                                         <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
	 *                                         <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
	 *                                         </ul>
	 * @exception SWTException
	 *                                         <ul>
	 *                                         <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
	 *                                         </ul>
	 */
	public void addPath(Path path)
	{
		ensureFreePointsAndTypesSize(path.pointsCount, path.typesCount);
		System.arraycopy(path.points, 0, points, pointsCount, path.pointsCount);
		System.arraycopy(path.types, 0, types, typesCount, path.typesCount);
		pointsCount += path.pointsCount;
		typesCount += path.typesCount;
	}
	/**
	 * Adds to the receiver the rectangle specified by x, y, width and height.
	 *
	 * @param     x            the x coordinate of the rectangle to add
	 * @param     y            the y coordinate of the rectangle to add
	 * @param     width        the width of the rectangle to add
	 * @param     height       the height of the rectangle to add
	 *
	 * @exception SWTException
	 *                             <ul>
	 *                             <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
	 *                             </ul>
	 */
	public void addRectangle(double x, double y, double width, double height)
	{
		if(endsWithMoveTo())
			ensureFreePointsAndTypesSize(8, 4);
		else
		{
			ensureFreePointsAndTypesSize(10, 5);
			moveTo(x, y);
		}
		lineTo(x + width, y);
		lineTo(x + width, y + height);
		lineTo(x, y + height);
		lineTo(x, y);
	}
	//
	//	/**
	//	 * Adds to the receiver the pattern of glyphs generated by drawing
	//	 * the given string using the given font starting at the point (x, y).
	//	 *
	//	 * @param     string                   the text to use
	//	 * @param     x                        the x coordinate of the starting point
	//	 * @param     y                        the y coordinate of the starting point
	//	 * @param     font                     the font to use
	//	 *
	//	 * @exception IllegalArgumentException
	//	 *                                         <ul>
	//	 *                                         <li>ERROR_NULL_ARGUMENT - if the font is null</li>
	//	 *                                         <li>ERROR_INVALID_ARGUMENT - if the font has been disposed</li>
	//	 *                                         </ul>
	//	 * @exception SWTException
	//	 *                                         <ul>
	//	 *                                         <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
	//	 *                                         </ul>
	//	 */
	//	public void addString(String string, double x, double y, Font font)
	//	{
	//		throw new IllegalStateException("unimplemented");
	//	}
	/**
	 * Closes the current sub path by adding to the receiver a line
	 * from the current point of the path back to the starting point
	 * of the sub path.
	 *
	 * @exception SWTException
	 *                             <ul>
	 *                             <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
	 *                             </ul>
	 */
	public void close()
	{
		ensureFreePointsAndTypesSize(0, 1);
		appendType(CLOSE);
	}
	//	/**
	//	 * Returns <code>true</code> if the specified point is contained by
	//	 * the receiver and false otherwise.
	//	 * <p>
	//	 * If outline is <code>true</code>, the point (x, y) checked for containment in
	//	 * the receiver's outline. If outline is <code>false</code>, the point is
	//	 * checked to see if it is contained within the bounds of the (closed) area
	//	 * covered by the receiver.
	//	 *
	//	 * @param     x                        the x coordinate of the point to test for containment
	//	 * @param     y                        the y coordinate of the point to test for containment
	//	 * @param     gc                       the GC to use when testing for containment
	//	 * @param     outline                  controls whether to check the outline or contained area of the path
	//	 * @return                             <code>true</code> if the path contains the point and <code>false</code> otherwise
	//	 *
	//	 * @exception IllegalArgumentException
	//	 *                                         <ul>
	//	 *                                         <li>ERROR_NULL_ARGUMENT - if the gc is null</li>
	//	 *                                         <li>ERROR_INVALID_ARGUMENT - if the gc has been disposed</li>
	//	 *                                         </ul>
	//	 * @exception SWTException
	//	 *                                         <ul>
	//	 *                                         <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
	//	 *                                         </ul>
	//	 */
	//	public boolean contains(double x, double y, GeneralGC gc, boolean outline)
	//	{
	//		throw new IllegalStateException("unimplemented");
	//	}
	/**
	 * Adds to the receiver a cubic bezier curve based on the parameters.
	 *
	 * @param     cx1          the x coordinate of the first control point of the spline
	 * @param     cy1          the y coordinate of the first control of the spline
	 * @param     cx2          the x coordinate of the second control of the spline
	 * @param     cy2          the y coordinate of the second control of the spline
	 * @param     x            the x coordinate of the end point of the spline
	 * @param     y            the y coordinate of the end point of the spline
	 *
	 * @exception SWTException
	 *                             <ul>
	 *                             <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
	 *                             </ul>
	 */
	public void cubicTo(double cx1, double cy1, double cx2, double cy2, double x, double y)
	{
		ensureFreePointsAndTypesSize(6, 1);
		appendPoint(cx1, cy1);
		appendPoint(cx2, cy2);
		appendPoint(x, y);
		appendType(CUBIC_TO);
	}
	//	/**
	//	 * Replaces the first four elements in the parameter with values that
	//	 * describe the smallest rectangle that will completely contain the
	//	 * receiver (i.e. the bounding box).
	//	 *
	//	 * @param     bounds                   the array to hold the result
	//	 *
	//	 * @exception IllegalArgumentException
	//	 *                                         <ul>
	//	 *                                         <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
	//	 *                                         <li>ERROR_INVALID_ARGUMENT - if the parameter is too small to hold the bounding box</li>
	//	 *                                         </ul>
	//	 * @exception SWTException
	//	 *                                         <ul>
	//	 *                                         <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
	//	 *                                         </ul>
	//	 */
	//	public void getBounds(double[] bounds)
	//	{
	//		throw new IllegalStateException("unimplemented");
	//	}
	/**
	 * Replaces the first two elements in the parameter with values that
	 * describe the current point of the path.
	 *
	 * @param     point                    the array to hold the result
	 *
	 * @exception IllegalArgumentException
	 *                                         <ul>
	 *                                         <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
	 *                                         <li>ERROR_INVALID_ARGUMENT - if the parameter is too small to hold the end point</li>
	 *                                         </ul>
	 * @exception SWTException
	 *                                         <ul>
	 *                                         <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
	 *                                         </ul>
	 */
	public void getCurrentPoint(double[] point)
	{
		point[0] = points[0];
		point[1] = points[1];
	}
	/**
	 * Adds to the receiver a line from the current point to
	 * the point specified by (x, y).
	 *
	 * @param     x            the x coordinate of the end of the line to add
	 * @param     y            the y coordinate of the end of the line to add
	 *
	 * @exception SWTException
	 *                             <ul>
	 *                             <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
	 *                             </ul>
	 */
	public void lineTo(double x, double y)
	{
		ensureFreePointsAndTypesSize(2, 1);
		appendPoint(x, y);
		appendType(LINE_TO);
	}
	/**
	 * Sets the current point of the receiver to the point
	 * specified by (x, y). Note that this starts a new
	 * sub path.
	 *
	 * @param     x            the x coordinate of the new end point
	 * @param     y            the y coordinate of the new end point
	 *
	 * @exception SWTException
	 *                             <ul>
	 *                             <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
	 *                             </ul>
	 */
	public void moveTo(double x, double y)
	{
		if(endsWithMoveTo())
		{
			points[pointsCount - 2] = x;
			points[pointsCount - 1] = y;
		} else
		{
			moveToWithoutCheck(x, y);
		}
	}
	private void moveToWithoutCheck(double x, double y)
	{
		ensureFreePointsAndTypesSize(2, 1);
		appendPoint(x, y);
		appendType(MOVE_TO);
	}
	/**
	 * Adds to the receiver a quadratic curve based on the parameters.
	 *
	 * @param     cx           the x coordinate of the control point of the spline
	 * @param     cy           the y coordinate of the control point of the spline
	 * @param     x            the x coordinate of the end point of the spline
	 * @param     y            the y coordinate of the end point of the spline
	 *
	 * @exception SWTException
	 *                             <ul>
	 *                             <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
	 *                             </ul>
	 */
	public void quadTo(double cx, double cy, double x, double y)
	{
		ensureFreePointsAndTypesSize(4, 1);
		appendPoint(cx, cy);
		appendPoint(x, y);
		appendType(QUAD_TO);
	}

	private void ensureFreePointsAndTypesSize(int pointsPlus, int typesPlus)
	{
		ensurePointsAndTypesSize(pointsCount + pointsPlus, typesCount + typesPlus);
	}
	private void ensurePointsAndTypesSize(int pointsCount, int typesCount)
	{
		if(dontEditPoints || pointsCount != points.length)
			points = Arrays.copyOf(points, pointsCount + POINTS_LENGTH_OVERHEAD);
		if(dontEditTypes || typesCount != types.length)
			types = Arrays.copyOf(types, typesCount + TYPES_LENGTH_OVERHEAD);
		dontEditPoints = false;
		dontEditTypes = false;
	}
	private void appendType(byte type)
	{
		types[typesCount ++] = type;
	}
	private void appendPoint(double x, double y)
	{
		points[pointsCount ++] = x;
		points[pointsCount ++] = y;
	}
	private boolean endsWithMoveTo()
	{
		return types[typesCount - 1] == MOVE_TO;
	}

	/**
	 * This Path is backed by the given arrays exactly until a path modifying method is called. (for example lineTo())
	 */
	public boolean setPointsAndTypes(double[] points, byte[] types)
	{
		int expectedPointsCount = 0;
		for(int i = 0; i < types.length; i ++)
		{
			switch(types[i])
			{
				case CLOSE:
					break;
				case CUBIC_TO:
					expectedPointsCount += 6;
					break;
				case LINE_TO:
					expectedPointsCount += 2;
					break;
				case MOVE_TO:
					expectedPointsCount += 2;
					break;
				case QUAD_TO:
					expectedPointsCount += 4;
					break;
				default:
					return false;
			}
		}
		if(expectedPointsCount != points.length)
			return false;
		this.points = points;
		this.types = types;
		pointsCount = points.length;
		typesCount = types.length;
		dontEditPoints = true;
		dontEditTypes = true;
		if(pointsCount == 0)
			moveToWithoutCheck(0, 0);
		return true;
	}

	public Path translate(double x, double y, double z)
	{
		return translate(x, y, z, z);
	}
	public Path translate(double x, double y, double zx, double zy)
	{
		double[] pointsTranslated = new double[pointsCount + POINTS_LENGTH_OVERHEAD];
		for(int i = 0; i < pointsCount; i += 2)
		{
			pointsTranslated[i] = points[i] * zx - x;
			pointsTranslated[i + 1] = points[i + 1] * zy - y;
		}
		return new Path(pointsTranslated, types, pointsCount, typesCount, false, true);
	}
	public Path untranslate(double x, double y, double z)
	{
		return translate(-x / z, -y / z, 1 / z);
	}

	public org.eclipse.swt.graphics.Path toSWTPath(Device device)
	{
		PathData data = new PathData();
		float[] pointsF = new float[pointsCount];
		for(int i = 0; i < pointsCount; i ++)
			pointsF[i] = (float) points[i];
		data.points = pointsF;
		if(types.length != typesCount)
			types = Arrays.copyOf(types, typesCount);
		data.types = types;
		return new org.eclipse.swt.graphics.Path(device, data);
	}
	@SuppressWarnings("static-method") //maybe we later want to do something instance-dependent
	public void disposeSWTPath(org.eclipse.swt.graphics.Path swtPath)
	{
		swtPath.dispose();
	}
}