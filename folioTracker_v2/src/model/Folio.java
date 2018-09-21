package model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class Folio extends Observable implements iFolio, Serializable{
	
	private static final long serialVersionUID = -5226363049808814083L;
	private final int id;	//unique identifier
	private List <iStock> stocks= new ArrayList<>();
	private DecimalFormat df = new DecimalFormat("#.##");
	
	public Folio(int id, Observer observer) {
		this.id=id;
		this.addObserver(observer);
	}
	
	public Folio(int id, List<iStock>stocks, Observer observer) {
		this.id=id;
		this.stocks=stocks;
		this.addObserver(observer);
		alertObservers();
	}

	@Override
	public boolean addShares(String stockSymbol, int numShares) {
		boolean temp=false;
		if (stocks.contains(new Stock(stockSymbol))){
			temp= (getStock(stockSymbol).addShares(numShares));
		}
		else if (numShares>=0){
			iStock newStock = new Stock(stockSymbol, numShares);
			temp= stocks.add(newStock);
		}
		alertObservers();
		return temp;
	}
	
	@Override
	public boolean sellShares(String stockSymbol, int numShares) {
		boolean temp=false;
		if (stocks.contains(new Stock(stockSymbol))){
			iStock stock = getStock(stockSymbol);
			temp= stock.addShares(-numShares);
			if (stock.getNumShares()<=0)
				stocks.remove(stock);
		}
		else
			return false;
		alertObservers();
		return temp;
	}
	

	private void alertObservers() {
		Object[][] dataList = new Object[stocks.size()][4];
		for (int i=0; i<stocks.size(); i++){
			dataList[i][0]=stocks.get(i).getName();
			dataList[i][1]=stocks.get(i).getNumShares();
			dataList[i][2]=stocks.get(i).getPricePerShare();
			dataList[i][3]=stocks.get(i).getTotalValue();
		}
		setChanged();
		notifyObservers(dataList);
		setChanged();
		notifyObservers(getTotal());
		
	}

	@Override
	public double getTotal() {
		double total = 0;
		for (int i=0; i<stocks.size(); i++){
			total += stocks.get(i).getTotalValue();
		}
		return Double.valueOf(df.format(total));
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof Folio))
				return false;
		if (((Folio)other).getID()==this.getID())
			return true;
		return false;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public double getStockValue(String stockSymbol) {
		return getStock(stockSymbol).getTotalValue();
	}
	
	private iStock getStock(String stockSymbol){
		for (int i=0; i<stocks.size(); i++){
			if (stocks.get(i).getName().equals(stockSymbol)){
				return stocks.get(i);
			}
		}
		throw new NullPointerException("Found no such stock: " +stockSymbol);
	}

	@Override
	public List<iStock> getList() {
		// TODO Auto-generated method stub
		return stocks;
	}

	

}
