package net.haspamelodica.swt.helper.tiledzoomablecanvas.tilecachingpolicy.lists;

import java.util.List;

import net.haspamelodica.swt.helper.swtobjectwrappers.Rectangle;

public interface ListTileCachingPolicy
{
	public List<Rectangle> calculateTileCacheTargetList(double offX, double offY, double zoom);

	public default void setCacheSize(int cacheSize)
	{}

	public default void setScreenSize(double w, double h)
	{}

	public default void setWorldBounds(double x, double y, double w, double h)
	{}
}