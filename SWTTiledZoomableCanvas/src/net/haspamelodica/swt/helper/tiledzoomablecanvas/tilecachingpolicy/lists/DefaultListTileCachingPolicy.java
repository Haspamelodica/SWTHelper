package net.haspamelodica.swt.helper.tiledzoomablecanvas.tilecachingpolicy.lists;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import net.haspamelodica.swt.helper.swtobjectwrappers.Rectangle;

public class DefaultListTileCachingPolicy implements ListTileCachingPolicy
{
	private final int	tileWidth;
	private Rectangle	world;
	private double		xW, yW, wW, hW;
	private double		gW, gH, maxWH;

	public DefaultListTileCachingPolicy(int tileWidth)
	{
		this.tileWidth = tileWidth;
	}
	//TilePosition screen = new TilePosition(-offX / zoom, -offY / zoom, Math.max(w, h) / zoom, false);
	public List<Rectangle> calculateTileCacheTargetList(double offX, double offY, double zoom)
	{
		List<Rectangle> target = new ArrayList<>();

		if(world == null)
			return target;

		target.add(world);

		double scrX = Math.max(xW, -offX / zoom);
		double scrY = Math.max(yW, -offY / zoom);
		double scrW = Math.min(xW - scrX + wW, gW / zoom);
		double scrH = Math.min(yW - scrY + hW, gH / zoom);
		double srcCX = scrX + scrW / 2;
		double srcCY = scrY + scrH / 2;
		addGridAlignedTiles(target, scrX, scrY, scrW, scrH, srcCX, srcCY, maxWH / zoom);
		addGridAlignedTiles(target, scrX, scrY, scrW, scrH, srcCX, srcCY, tileWidth / zoom);

		return target;
	}
	public void addGridAlignedTiles(List<Rectangle> tiles, double x1, double y1, double w, double h, double xC, double yC, double tileWidth)
	{
		double tX = x1 / tileWidth;
		double tY = y1 / tileWidth;
		if(!Double.isInfinite(tX) && !Double.isNaN(tX) && !Double.isInfinite(tY) && !Double.isNaN(tY))
		{
			List<Rectangle> newTiles = new ArrayList<>();
			BigDecimal xBD = new BigDecimal(tX);
			BigDecimal yBD = new BigDecimal(tY);
			BigDecimal tileX1 = bigIntVal(xBD);
			BigDecimal tileY1 = bigIntVal(yBD);
			BigDecimal tileX2 = xBD.add(new BigDecimal(w / tileWidth));
			BigDecimal tileY2 = yBD.add(new BigDecimal(h / tileWidth));
			for(BigDecimal tileX = tileX1; tileX.compareTo(tileX2) <= 0; tileX = tileX.add(BigDecimal.ONE))
				for(BigDecimal tileY = tileY1; tileY.compareTo(tileY2) <= 0; tileY = tileY.add(BigDecimal.ONE))
					newTiles.add(new Rectangle(tileX.doubleValue() * tileWidth, tileY.doubleValue() * tileWidth, tileWidth, tileWidth));
			double halfTW = tileWidth / 2;
			newTiles.sort((t1, t2) ->
			{
				double t1dX = t1.x + halfTW - xC;
				double t1dY = t1.y + halfTW - yC;
				double t2dX = t2.x + halfTW - xC;
				double t2dY = t2.y + halfTW - yC;

				double t1d = t1dX * t1dX + t1dY * t1dY;
				double t2d = t2dX * t2dX + t2dY * t2dY;

				return (int) Math.signum(t1d - t2d);
			});
			tiles.addAll(newTiles);
		}
	}
	private BigDecimal bigIntVal(BigDecimal dBD)
	{
		int setPrecision = dBD.precision() - dBD.scale();
		if(setPrecision <= 0)
			return dBD.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ONE.negate() : BigDecimal.ZERO;
		return dBD.round(new MathContext(setPrecision, RoundingMode.FLOOR));
	}
	public void setWorldBounds(double x, double y, double w, double h)
	{
		this.xW = x;
		this.yW = y;
		this.wW = w;
		this.hW = h;
		double maxWH = Math.max(w, h);
		world = new Rectangle(x, y, maxWH, maxWH);
	}
	public void setScreenSize(double w, double h)
	{
		this.gW = w;
		this.gH = h;
		maxWH = Math.max(w, h);
	}
}