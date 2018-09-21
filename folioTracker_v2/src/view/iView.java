package view;

import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public interface iView extends Observer{

	public boolean ShowError(String message);

	public void run();

	public String getUserInNumShares(int ind);

	public String getUserInStock(int ind);
	
	public void update(Observable arg0, Object arg1);
	
	public boolean setAddPanelListener(ActionListener add, ActionListener save, ActionListener load);
	
	public int getSelectedPanel();

	public iPanel addPanel(int id, ActionListener buy, ActionListener sell,
			ActionListener del);
	
	public boolean save(Object o);
	
	public Object load();

	
}
