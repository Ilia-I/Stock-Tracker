package view;

import java.util.Observer;

import javax.swing.JComponent;

public interface iPanel extends Observer{
	
	public JComponent getPanel();
	
	public int getID();
	
	public String getStockSymbol();
	
	public String getStockShares();

}
