package model;

public interface iStock {
	
	public boolean addShares(int shares);
	public int getNumShares();
	public double getTotalValue();
	public double getPricePerShare();
	public String getName();
	

}
