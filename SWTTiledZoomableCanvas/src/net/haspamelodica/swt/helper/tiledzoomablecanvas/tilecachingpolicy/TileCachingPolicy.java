package net.haspamelodica.swt.helper.tiledzoomablecanvas.tilecachingpolicy;

import java.util.Set;

import net.haspamelodica.swt.helper.ZoomedRegion;

public interface TileCachingPolicy
{
	public ZoomedRegion getNextTileToRender(double offX, double offY, double zoom, Set<ZoomedRegion> cachedTilePositions);

	public Set<ZoomedRegion> getTilesToFree(double offX, double offY, double zoom, Set<ZoomedRegion> cachedTilePositions);

	public default void setCacheSize(int cacheSize)
	{}

	public default void setScreenSize(double w, double h)
	{}

	public default void setWorldBounds(double x, double y, double w, double h)
	{}
}