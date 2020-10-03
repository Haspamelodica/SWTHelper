package net.haspamelodica.swt.helper.tiledzoomablecanvas.tilecachingpolicy;

import java.util.Set;

import net.haspamelodica.swt.helper.swtobjectwrappers.Rectangle;

public interface TileCachingPolicy
{
	public Rectangle getNextTileToRender(double offX, double offY, double zoom, Set<Rectangle> cachedTilePositions);

	public Set<Rectangle> getTilesToFree(double offX, double offY, double zoom, Set<Rectangle> cachedTilePositions);

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