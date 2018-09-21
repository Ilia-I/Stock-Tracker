package model;

import java.util.List;
import java.util.Observer;

public interface iModel {
	public void addObserver(Observer o);
	public boolean addFolio(int folID, Observer observer);
	public boolean removeFolio(int folID);
	public double getTotalValue(int folID);
	public boolean addShares(int folID,int numShares, String stockSymbol);
	public boolean sellShares(int folID,int numShares, String stockSymbol);
	public double getStockValue(int folID, String stockSymbol);
	public iFolio getFolio(int folID);
	public boolean addFolio(int folID, List<iStock> list, Observer ob);


}
