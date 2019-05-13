package net.haspamelodica.swt.helper;

public class ZoomedRegion implements Comparable<ZoomedRegion>
{
	public final int tileWidth;
	public final double	zoom;
	public final double	x, y, w;

	public ZoomedRegion(int tileWidth, double x, double y, double wOrZoom, boolean isZoom)
	{
		this.tileWidth = tileWidth;
		if(isZoom)
		{
			this.zoom = wOrZoom;
			this.w = tileWidth / wOrZoom;
		} else
		{
			this.zoom = tileWidth / wOrZoom;
			this.w = wOrZoom;
		}
		this.x = x;
		this.y = y;
	}
	public boolean contains(double x, double y)
	{
		return (x >= this.x) && (y >= this.y) && x < (this.x + w) && y < (this.y + w);
	}
	public boolean intersects(double x, double y, double width, double height)
	{
		return (x < this.x + this.w) && (y < this.y + this.w) &&
				(x + width > this.x) && (y + height > this.y);
	}
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(zoom);
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
		if(!(obj instanceof ZoomedRegion))
			return false;
		ZoomedRegion other = (ZoomedRegion) obj;
		if(Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if(Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if(Double.doubleToLongBits(zoom) != Double.doubleToLongBits(other.zoom))
			return false;
		return true;
	}
	@Override
	public int compareTo(ZoomedRegion other)
	{
		double dz = zoom - other.zoom;
		if(dz < 0)
			return -1;
		if(dz > 0)
			return 1;
		double dxT = x - other.x;
		if(dxT < 0)
			return -1;
		if(dxT > 0)
			return 1;
		double dyT = y - other.y;
		if(dyT < 0)
			return -1;
		if(dyT > 0)
			return 1;
		return 0;
	}
	@Override
	public String toString()
	{
		return "(" + x + "|" + y + "|" + w + ")";
	}
}