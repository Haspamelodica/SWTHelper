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

		if(dstX1 < minDstX1)
		{
			double srcDX = Math.ceil((dstX1 - minDstX1) * invSclX);
			srcX1 -= srcDX;
			dstX1 -= srcDX * sclX;
			if(dstX2 - dstX1 <= 0)
				return null;
		}
		if(dstY1 < minDstY1)
		{
			double srcDY = Math.ceil((dstY1 - minDstY1) * invSclY);
			srcY1 -= srcDY;
			dstY1 -= srcDY * sclY;
			if(dstY2 - dstY1 <= 0)
				return null;
		}
		if(dstX2 > maxDstX2)
		{
			double srcDX = Math.floor((dstX2 - maxDstX2) * invSclX);
			srcX2 -= srcDX;
			dstX2 -= srcDX * sclX;
			if(dstX2 - dstX1 <= 0)
				return null;
		}
		if(dstY2 > maxDstY2)
		{
			double srcDY = Math.floor((dstY2 - maxDstY2) * invSclY);
			srcY2 -= srcDY;
			dstY2 -= srcDY * sclY;
			if(dstY2 - dstY1 <= 0)
				return null;
		}

		rcrInstance.srcX1 = (int) srcX1;
		rcrInstance.srcY1 = (int) srcY1;
		rcrInstance.srcX2 = (int) srcX2;
		rcrInstance.srcY2 = (int) srcY2;

		rcrInstance.dstX1 = dstX1;
		rcrInstance.dstY1 = dstY1;
		rcrInstance.dstX2 = dstX2;
		rcrInstance.dstY2 = dstY2;
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
		xr -= lineWidth / 2;
		yt -= lineWidth / 2;
		xl += lineWidth / 2;
		yb += lineWidth / 2;
		return clipRectangleLineCohenSuterland(x1, y1, x2, y2, xr, yt, xl, yb);
	}
	private static LineClipResult clipRectangleLineCohenSuterland(double x1, double y1, double x2, double y2, double xr, double yt, double xl, double yb)
	{
		for(;;)
		{
			int outCode1 = outCode(x1, y1, xr, xl, yt, yb);
			int outCode2 = outCode(x2, y2, xr, xl, yt, yb);
			if((outCode1 & outCode2) != 0)
				return null;
			if(outCode1 == 0 && outCode2 == 0)
			{
				lcrInstance.x1 = x1;
				lcrInstance.y1 = y1;
				lcrInstance.x2 = x2;
				lcrInstance.y2 = y2;
				return lcrInstance;
			}
			if(outCode1 == 0)
			{
				double tempCoord = x1;
				x1 = x2;
				x2 = tempCoord;
				tempCoord = y1;
				y1 = y2;
				y2 = tempCoord;
				int tempCode = outCode1;
				outCode1 = outCode2;
				outCode2 = tempCode;
			}
			if((outCode1 & OUT_RIGHT) != 0)
			{
				y1 += (y2 - y1) * (xr - x1) / (x2 - x1);
				x1 = xr;
			} else if((outCode1 & OUT_LEFT) != 0)
			{
				y1 += (y2 - y1) * (xl - x1) / (x2 - x1);
				x1 = xl;
			} else if((outCode1 & OUT_TOP) != 0)
			{
				x1 += (x2 - x1) * (yt - y1) / (y2 - y1);
				y1 = yt;
			} else if((outCode1 & OUT_BOTTOM) != 0)
			{
				x1 += (x2 - x1) * (yb - y1) / (y2 - y1);
				y1 = yb;
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