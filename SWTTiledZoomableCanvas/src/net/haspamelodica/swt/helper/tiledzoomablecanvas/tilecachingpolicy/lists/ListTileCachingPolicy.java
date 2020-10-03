package net.haspamelodica.swt.helper.tiledzoomablecanvas.tilecachingpolicy.lists;

import java.util.List;

import net.haspamelodica.swt.helper.swtobjectwrappers.Rectangle;

public interface ListTileCachingPolicy
{
	public List<Rectangle> calculateTileCacheTargetList(double offX, double offY, double zoom);

	@SuppressWarnings("unused")
	public default void setCacheSize(int cacheSize)
	{
		//default: nothing to do
	}

	@SuppressWarnings("unused")
	public default void setScreenSize(double w, double h)
	{
		//default: nothing to do
	}

	@SuppressWarnings("unused")
	public default void setWorldBounds(double x, double y, double w, double h)
	{
		//default: nothing to do
	}
}