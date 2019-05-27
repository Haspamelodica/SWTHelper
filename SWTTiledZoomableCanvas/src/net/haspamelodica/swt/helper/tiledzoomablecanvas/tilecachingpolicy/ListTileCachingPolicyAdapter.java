package net.haspamelodica.swt.helper.tiledzoomablecanvas.tilecachingpolicy;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import net.haspamelodica.swt.helper.swtobjectwrappers.Rectangle;
import net.haspamelodica.swt.helper.tiledzoomablecanvas.tilecachingpolicy.lists.ListTileCachingPolicy;

public class ListTileCachingPolicyAdapter implements TileCachingPolicy
{
	private ListTileCachingPolicy	listPolicy;
	private boolean					updateNecessary;
	private double					lastX, lastY, lastZoom;
	private int						cacheSize;
	private List<Rectangle>		target;
	private Queue<Rectangle>		toFreeBestOrder;

	public ListTileCachingPolicyAdapter(ListTileCachingPolicy listPolicy)
	{
		this.listPolicy = listPolicy;
		updateNecessary = true;
		toFreeBestOrder = new LinkedList<>();
	}

	public Rectangle getNextTileToRender(double offX, double offY, double zoom, Set<Rectangle> cachedTilePositions)
	{
		updateTargetListIfNecessary(offX, offY, zoom, cachedTilePositions);
		for(Rectangle t : target)
			if(!cachedTilePositions.contains(t))
				return t;
		return null;
	}

	public Set<Rectangle> getTilesToFree(double offX, double offY, double zoom, Set<Rectangle> cachedTilePositions)
	{
		updateTargetListIfNecessary(offX, offY, zoom, cachedTilePositions);
		if(cachedTilePositions.size() < cacheSize)
			return new HashSet<>();
		Set<Rectangle> suitableToFree = new HashSet<>();
		Rectangle toFree = toFreeBestOrder.poll();
		if(toFree != null)
			suitableToFree.add(toFree);
		return suitableToFree;
	}
	private void updateTargetListIfNecessary(double offX, double offY, double zoom, Set<Rectangle> cachedTilePositions)
	{
		if(updateNecessary || lastX != offX || lastY != offY || lastZoom != zoom)
		{
			if(target != null)
				toFreeBestOrder.addAll(target);
			updateTargetList(offX, offY, zoom);
			updateNecessary = false;
			lastX = offX;
			lastY = offY;
			lastZoom = zoom;
		}
		if(target != null)
		{
			cachedTilePositions
					.stream()
					.filter(t -> !toFreeBestOrder.contains(t))
					.filter(t -> !target.contains(t))
					.forEach(toFreeBestOrder::add);
			toFreeBestOrder.removeAll(target);
			toFreeBestOrder.retainAll(cachedTilePositions);
		}
	}
	public void setCacheSize(int cacheSize)
	{
		this.cacheSize = cacheSize;
		listPolicy.setCacheSize(cacheSize);
		updateNecessary = true;
	}
	public void setScreenSize(double w, double h)
	{
		listPolicy.setScreenSize(w, h);
		updateNecessary = true;
	}
	public void setWorldBounds(double x, double y, double w, double h)
	{
		listPolicy.setWorldBounds(x, y, w, h);
		updateNecessary = true;
	}
	private void updateTargetList(double offX, double offY, double zoom)
	{
		target = listPolicy.calculateTileCacheTargetList(offX, offY, zoom);
	}
}