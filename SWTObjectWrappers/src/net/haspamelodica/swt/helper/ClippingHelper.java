package net.haspamelodica.swt.helper;

public class ClippingHelper
{
	private ClippingHelper()
	{}
	private final static RectangleClippingResult rcrInstance = new RectangleClippingResult();
	public static RectangleClippingResult clipRectangleRectangleSrcAsInts(double srcX1, double srcY1, double srcX2, double srcY2, double dstX1, double dstY1, double dstX2, double dstY2, double minDstX1, double minDstY1, double maxDstX2, double maxDstY2)
	{
		double sclX = (dstX2 - dstX1) / (srcX2 - srcX1);
		double sclY = (dstY2 - dstY1) / (srcY2 - srcY1);
		double invSclX = 1 / sclX;
		double invSclY = 1 / sclY;

		double srcX1Clipped = srcX1;
		double srcY1Clipped = srcY1;
		double srcX2Clipped = srcX2;
		double srcY2Clipped = srcY2;
		double dstX1Clipped = dstX1;
		double dstY1Clipped = dstY1;
		double dstX2Clipped = dstX2;
		double dstY2Clipped = dstY2;

		if(dstX1Clipped < minDstX1)
		{
			double srcDX = Math.ceil((dstX1Clipped - minDstX1) * invSclX);
			srcX1Clipped -= srcDX;
			dstX1Clipped -= srcDX * sclX;
			if(dstX2Clipped - dstX1Clipped <= 0)
				return null;
		}
		if(dstY1Clipped < minDstY1)
		{
			double srcDY = Math.ceil((dstY1Clipped - minDstY1) * invSclY);
			srcY1Clipped -= srcDY;
			dstY1Clipped -= srcDY * sclY;
			if(dstY2Clipped - dstY1Clipped <= 0)
				return null;
		}
		if(dstX2Clipped > maxDstX2)
		{
			double srcDX = Math.floor((dstX2Clipped - maxDstX2) * invSclX);
			srcX2Clipped -= srcDX;
			dstX2Clipped -= srcDX * sclX;
			if(dstX2Clipped - dstX1Clipped <= 0)
				return null;
		}
		if(dstY2Clipped > maxDstY2)
		{
			double srcDY = Math.floor((dstY2Clipped - maxDstY2) * invSclY);
			srcY2Clipped -= srcDY;
			dstY2Clipped -= srcDY * sclY;
			if(dstY2Clipped - dstY1Clipped <= 0)
				return null;
		}

		rcrInstance.srcX1 = (int) srcX1Clipped;
		rcrInstance.srcY1 = (int) srcY1Clipped;
		rcrInstance.srcX2 = (int) srcX2Clipped;
		rcrInstance.srcY2 = (int) srcY2Clipped;

		rcrInstance.dstX1 = dstX1Clipped;
		rcrInstance.dstY1 = dstY1Clipped;
		rcrInstance.dstX2 = dstX2Clipped;
		rcrInstance.dstY2 = dstY2Clipped;
		return rcrInstance;
	}
	public static class RectangleClippingResult
	{
		public int	srcX1;
		public int	srcY1;
		public int	srcX2;
		public int	srcY2;

		public double	dstX1;
		public double	dstY1;
		public double	dstX2;
		public double	dstY2;
	}
	private final static LineClipResult	lcrInstance	= new LineClipResult();
	private static final int			OUT_RIGHT	= 1;
	private static final int			OUT_LEFT	= 2;
	private static final int			OUT_TOP		= 4;
	private static final int			OUT_BOTTOM	= 8;
	public static LineClipResult clipLineRectangleCohenSutherland(double x1, double y1, double x2, double y2, double xr, double yt, double xl, double yb, double lineWidth)
	{
		//TODO
		double xrLW = xr - lineWidth / 2;
		double ytLW = yt - lineWidth / 2;
		double xlLW = xl + lineWidth / 2;
		double ybLW = yb + lineWidth / 2;
		return clipRectangleLineCohenSuterland(x1, y1, x2, y2, xrLW, ytLW, xlLW, ybLW);
	}
	private static LineClipResult clipRectangleLineCohenSuterland(double x1, double y1, double x2, double y2, double xr, double yt, double xl, double yb)
	{
		double x1Clipped = x1;
		double y1Clipped = y1;
		double x2Clipped = x2;
		double y2Clipped = y2;

		for(;;)
		{
			int outCode1 = outCode(x1Clipped, y1Clipped, xr, xl, yt, yb);
			int outCode2 = outCode(x2Clipped, y2Clipped, xr, xl, yt, yb);
			if((outCode1 & outCode2) != 0)
				return null;
			if(outCode1 == 0 && outCode2 == 0)
			{
				lcrInstance.x1 = x1Clipped;
				lcrInstance.y1 = y1Clipped;
				lcrInstance.x2 = x2Clipped;
				lcrInstance.y2 = y2Clipped;
				return lcrInstance;
			}
			if(outCode1 == 0)
			{
				double tempCoord = x1Clipped;
				x1Clipped = x2Clipped;
				x2Clipped = tempCoord;
				tempCoord = y1Clipped;
				y1Clipped = y2Clipped;
				y2Clipped = tempCoord;
				int tempCode = outCode1;
				outCode1 = outCode2;
				outCode2 = tempCode;
			}
			if((outCode1 & OUT_RIGHT) != 0)
			{
				y1Clipped += (y2Clipped - y1Clipped) * (xr - x1Clipped) / (x2Clipped - x1Clipped);
				x1Clipped = xr;
			} else if((outCode1 & OUT_LEFT) != 0)
			{
				y1Clipped += (y2Clipped - y1Clipped) * (xl - x1Clipped) / (x2Clipped - x1Clipped);
				x1Clipped = xl;
			} else if((outCode1 & OUT_TOP) != 0)
			{
				x1Clipped += (x2Clipped - x1Clipped) * (yt - y1Clipped) / (y2Clipped - y1Clipped);
				y1Clipped = yt;
			} else if((outCode1 & OUT_BOTTOM) != 0)
			{
				x1Clipped += (x2Clipped - x1Clipped) * (yb - y1Clipped) / (y2Clipped - y1Clipped);
				y1Clipped = yb;
			}
		}
	}
	private static int outCode(double x, double y, double xr, double xl, double yt, double yb)
	{
		int outCode = 0;
		if(x < xr)
			outCode |= OUT_RIGHT;
		else if(x > xl)
			outCode |= OUT_LEFT;
		if(y < yt)
			outCode |= OUT_TOP;
		else if(y > yb)
			outCode |= OUT_BOTTOM;
		return outCode;
	}
	public static class LineClipResult
	{
		public double	x1;
		public double	y1;
		public double	x2;
		public double	y2;
	}
}