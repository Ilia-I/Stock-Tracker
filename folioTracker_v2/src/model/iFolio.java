package model;

import java.util.List;

public interface iFolio {
	
	public boolean addShares(String stockSymbol, int numShares);
	public boolean sellShares(String stockSymbol, int numShares);
	public double getTotal();
	public int getID();
	public double getStockValue(String stockSymbol);
	public List<iStock> getList();

}
