package net.haspamelodica.swt.helper.swtobjectwrappers;

import org.eclipse.swt.SWT;

public class Rectangle
{
	/**
	 * the x coordinate of the rectangle
	 */
	public double x;

	/**
	 * the y coordinate of the rectangle
	 */
	public double y;

	/**
	 * the width of the rectangle
	 */
	public double width;

	/**
	 * the height of the rectangle
	 */
	public double height;

	/**
	 * Construct a new instance of this class given the
	 * x, y, width and height values.
	 *
	 * @param x      the x coordinate of the origin of the rectangle
	 * @param y      the y coordinate of the origin of the rectangle
	 * @param width  the width of the rectangle
	 * @param height the height of the rectangle
	 */
	public Rectangle(double x, double y, double width, double height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	public Rectangle(org.eclipse.swt.graphics.Rectangle rect)
	{
		this(rect.x, rect.y, rect.width, rect.height);
	}

	/**
	 * Destructively replaces the x, y, width and height values
	 * in the receiver with ones which represent the union of the
	 * rectangles specified by the receiver and the given rectangle.
	 * <p>
	 * The union of two rectangles is the smallest single rectangle
	 * that completely covers both of the areas covered by the two
	 * given rectangles.
	 * </p>
	 *
	 * @param     rect                     the rectangle to merge with the receiver
	 *
	 * @exception IllegalArgumentException
	 *                                         <ul>
	 *                                         <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
	 *                                         </ul>
	 */
	public void add(Rectangle rect)
	{
		if(rect == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		double left = x < rect.x ? x : rect.x;
		double top = y < rect.y ? y : rect.y;
		double lhs = x + width;
		double rhs = rect.x + rect.width;
		double right = lhs > rhs ? lhs : rhs;
		lhs = y + height;
		rhs = rect.y + rect.height;
		double bottom = lhs > rhs ? lhs : rhs;
		x = left;
		y = top;
		width = right - left;
		height = bottom - top;
	}

	/**
	 * Returns <code>true</code> if the point specified by the
	 * arguments is inside the area specified by the receiver,
	 * and <code>false</code> otherwise.
	 *
	 * @param  x the x coordinate of the point to test for containment
	 * @param  y the y coordinate of the point to test for containment
	 * @return   <code>true</code> if the rectangle contains the point and <code>false</code> otherwise
	 */
	public boolean contains(double x, double y)
	{
		return (x >= this.x) && (y >= this.y) && x < (this.x + width) && y < (this.y + height);
	}

	/**
	 * Returns <code>true</code> if the given point is inside the
	 * area specified by the receiver, and <code>false</code>
	 * otherwise.
	 *
	 * @param     pt                       the point to test for containment
	 * @return                             <code>true</code> if the rectangle contains the point and <code>false</code> otherwise
	 *
	 * @exception IllegalArgumentException
	 *                                         <ul>
	 *                                         <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
	 *                                         </ul>
	 */
	public boolean contains(Point pt)
	{
		if(pt == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		return contains(pt.x, pt.y);
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(height);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(width);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Rectangle other = (Rectangle) obj;
		if(Double.doubleToLongBits(height) != Double.doubleToLongBits(other.height))
			return false;
		if(Double.doubleToLongBits(width) != Double.doubleToLongBits(other.width))
			return false;
		if(Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if(Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

	/**
	 * Destructively replaces the x, y, width and height values
	 * in the receiver with ones which represent the intersection of the
	 * rectangles specified by the receiver and the given rectangle.
	 *
	 * @param     rect                     the rectangle to intersect with the receiver
	 *
	 * @exception IllegalArgumentException
	 *                                         <ul>
	 *                                         <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
	 *                                         </ul>
	 *
	 *                                         since 3.0
	 */
	public void intersect(Rectangle rect)
	{
		if(rect == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if(this == rect)
			return;
		double left = x > rect.x ? x : rect.x;
		double top = y > rect.y ? y : rect.y;
		double lhs = x + width;
		double rhs = rect.x + rect.width;
		double right = lhs < rhs ? lhs : rhs;
		lhs = y + height;
		rhs = rect.y + rect.height;
		double bottom = lhs < rhs ? lhs : rhs;
		x = right < left ? 0 : left;
		y = bottom < top ? 0 : top;
		width = right < left ? 0 : right - left;
		height = bottom < top ? 0 : bottom - top;
	}

	/**
	 * Returns a new rectangle which represents the intersection
	 * of the receiver and the given rectangle.
	 * <p>
	 * The intersection of two rectangles is the rectangle that
	 * covers the area which is contained within both rectangles.
	 * </p>
	 *
	 * @param     rect                     the rectangle to intersect with the receiver
	 * @return                             the intersection of the receiver and the argument
	 *
	 * @exception IllegalArgumentException
	 *                                         <ul>
	 *                                         <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
	 *                                         </ul>
	 */
	public Rectangle intersection(Rectangle rect)
	{
		if(rect == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if(this == rect)
			return new Rectangle(x, y, width, height);
		double left = x > rect.x ? x : rect.x;
		double top = y > rect.y ? y : rect.y;
		double lhs = x + width;
		double rhs = rect.x + rect.width;
		double right = lhs < rhs ? lhs : rhs;
		lhs = y + height;
		rhs = rect.y + rect.height;
		double bottom = lhs < rhs ? lhs : rhs;
		return new Rectangle(
				right < left ? 0 : left,
				bottom < top ? 0 : top,
				right < left ? 0 : right - left,
				bottom < top ? 0 : bottom - top);
	}

	/**
	 * Returns <code>true</code> if the rectangle described by the
	 * arguments intersects with the receiver and <code>false</code>
	 * otherwise.
	 * <p>
	 * Two rectangles intersect if the area of the rectangle
	 * representing their intersection is not empty.
	 * </p>
	 *
	 * @param     x                        the x coordinate of the origin of the rectangle
	 * @param     y                        the y coordinate of the origin of the rectangle
	 * @param     width                    the width of the rectangle
	 * @param     height                   the height of the rectangle
	 * @return                             <code>true</code> if the rectangle intersects with the receiver, and <code>false</code> otherwise
	 *
	 * @exception IllegalArgumentException
	 *                                         <ul>
	 *                                         <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
	 *                                         </ul>
	 *
	 * @see                                #intersection(Rectangle)
	 * @see                                #isEmpty()
	 *
	 * @since                              3.0
	 */
	public boolean intersects(double x, double y, double width, double height)
	{
		return (x < this.x + this.width) && (y < this.y + this.height) &&
				(x + width > this.x) && (y + height > this.y);
	}

	/**
	 * Returns <code>true</code> if the given rectangle intersects
	 * with the receiver and <code>false</code> otherwise.
	 * <p>
	 * Two rectangles intersect if the area of the rectangle
	 * representing their intersection is not empty.
	 * </p>
	 *
	 * @param     rect                     the rectangle to test for intersection
	 * @return                             <code>true</code> if the rectangle intersects with the receiver, and <code>false</code> otherwise
	 *
	 * @exception IllegalArgumentException
	 *                                         <ul>
	 *                                         <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
	 *                                         </ul>
	 *
	 * @see                                #intersection(Rectangle)
	 * @see                                #isEmpty()
	 */
	public boolean intersects(Rectangle rect)
	{
		if(rect == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		return rect == this || intersects(rect.x, rect.y, rect.width, rect.height);
	}

	/**
	 * Returns <code>true</code> if the receiver does not cover any
	 * area in the (x, y) coordinate plane, and <code>false</code> if
	 * the receiver does cover some area in the plane.
	 * <p>
	 * A rectangle is considered to <em>cover area</em> in the
	 * (x, y) coordinate plane if both its width and height are
	 * non-zero.
	 * </p>
	 *
	 * @return <code>true</code> if the receiver is empty, and <code>false</code> otherwise
	 */
	public boolean isEmpty()
	{
		return (width <= 0) || (height <= 0);
	}

	/**
	 * Returns a string containing a concise, human-readable
	 * description of the receiver.
	 *
	 * @return a string representation of the rectangle
	 */
	@Override
	public String toString()
	{
		return "Rectangle {" + x + ", " + y + ", " + width + ", " + height + "}"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}

	/**
	 * Returns a new rectangle which represents the union of
	 * the receiver and the given rectangle.
	 * <p>
	 * The union of two rectangles is the smallest single rectangle
	 * that completely covers both of the areas covered by the two
	 * given rectangles.
	 * </p>
	 *
	 * @param     rect                     the rectangle to perform union with
	 * @return                             the union of the receiver and the argument
	 *
	 * @exception IllegalArgumentException
	 *                                         <ul>
	 *                                         <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
	 *                                         </ul>
	 *
	 * @see                                #add(Rectangle)
	 */
	public Rectangle union(Rectangle rect)
	{
		if(rect == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		double left = x < rect.x ? x : rect.x;
		double top = y < rect.y ? y : rect.y;
		double lhs = x + width;
		double rhs = rect.x + rect.width;
		double right = lhs > rhs ? lhs : rhs;
		lhs = y + height;
		rhs = rect.y + rect.height;
		double bottom = lhs > rhs ? lhs : rhs;
		return new Rectangle(left, top, right - left, bottom - top);
	}

	public org.eclipse.swt.graphics.Rectangle toSWTRectanlge()
	{
		return new org.eclipse.swt.graphics.Rectangle((int) x, (int) y, (int) width, (int) height);
	}

}