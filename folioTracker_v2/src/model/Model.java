package model;

import view.UpdaterObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Model extends Observable implements iModel, Serializable{

	private static final long serialVersionUID = 7470920991104452618L;
	List <iFolio> folios= new ArrayList<>();
	
	public Model(){
	}

	@Override
	public boolean addFolio(int folID, Observer observer) {
		Folio newF = new Folio(folID, observer);
		if (folios.contains(newF))
			return false;
		
		folios.add(newF);
		setChanged();
		notifyObservers("New folio has been added");
		return true;
	}
	
	public boolean addFolio(int folID, List<iStock> list, Observer ob) {
		Folio newF = new Folio(folID, list, ob);
		if (folios.contains(newF)){
			System.out.println("Folio id already in the list");
			return false;
		}
		System.out.println(list.size());
		folios.add(newF);
		setChanged();
		notifyObservers("New folio has been added");
		return true;
	}

	@Override
	public boolean removeFolio(int folID) {
		for (int i=0; i<folios.size(); i++){
			if (folios.get(i).getID()==folID){
				folios.remove(i);
				setChanged();
				notifyObservers(new UpdaterObject("del",folID));
				return true;
			}
		}
		return false;
	}

	@Override
	public double getTotalValue(int folID) {
		return getFolio(folID).getTotal();
	}

	@Override
	public boolean addShares(int folID, int numShares, String stockSymbol) {
		setChanged();
		notifyObservers();
		//System.out.println("adding shares to folio "+folID);
		boolean temp = getFolio(folID).addShares(stockSymbol, numShares);
		return temp;
	}


	@Override
	public double getStockValue(int folID, String stockSymbol) {
		return getFolio(folID).getStockValue(stockSymbol);
	}


	public iFolio getFolio(int folID){
		for (int i=0; i<folios.size(); i++){
			if (folios.get(i).getID()==folID){
				return folios.get(i);
			}
		}
		throw new NullPointerException("Found no such folio id: " +folID);
	}

	@Override
	public boolean sellShares(int folID, int numShares, String stockSymbol) {
		setChanged();
		notifyObservers();
		//System.out.println("selling shares to folio "+folID);
		boolean temp = getFolio(folID).sellShares(stockSymbol, numShares);
		return temp;
	}

	
}
