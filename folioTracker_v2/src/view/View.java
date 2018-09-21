package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

public class View implements iView, Observer{
	
	private JFrame frame;
	private JMenuItem addFolio, saveFolio, loadFolio, exit;
	private JTabbedPane tabbedPane;
	private List <iPanel> panels= new ArrayList<>();
	
	public View(){
	}
	
	public void run(){
		initialiseFrame();
		initialiseMenu();
		initialiseTabbedPane();
		
		
		frame.setVisible(true);
	}
	
	private void initialiseTabbedPane() {
		tabbedPane = new JTabbedPane();
		frame.add(tabbedPane);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setBackground(Color.LIGHT_GRAY);
	}
	
	@Override
	public iPanel addPanel(int id, ActionListener buy, ActionListener sell, ActionListener del) {
		iPanel panel = new Panel(id, buy, sell, del);
		if (!panels.contains(panel)){
			panels.add(panel);
			tabbedPane.addTab(buildTitle(id), panel.getPanel());
			tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
			System.out.println("added panel " +id);
			return panel;
		}
		
		ShowError("Tried to add already existing panel, id: " +id);
		return null;
	}
	
	private String buildTitle(int i){
		return ("Panel "+i);
	}

	private void initialiseFrame(){
		frame = new JFrame("Stock Tracker");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(600, 350));
	}
	
	private void initialiseMenu(){
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		
		addFolio = new JMenuItem("Add Folio");
		menu.add(addFolio);
		
		saveFolio = new JMenuItem("Save");
		menu.add(saveFolio);
		
		loadFolio = new JMenuItem("Load");
		menu.add(loadFolio);
		
		
		exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				System.exit(0);
			}
		});
		
		menu.add(exit);
		menuBar.add(menu);
		
		frame.setJMenuBar(menuBar);
	}
	
	@Override
	public boolean ShowError(String message) {
		JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
		return true;
	}

	public String getUserInNumShares(int ind) {
		return panels.get(ind).getStockShares();
	}

	public String getUserInStock(int ind) {
		return panels.get(ind).getStockSymbol();
	}
	
	public int getSelectedPanel(){
		return tabbedPane.getSelectedIndex();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof UpdaterObject){
			UpdaterObject updater = (UpdaterObject)arg1;
			switch(updater.getCommand()){
			case "del":
				for (int i=0; i<tabbedPane.getTabCount();i++){
					if (tabbedPane.getTitleAt(i).equals(buildTitle(((UpdaterObject) arg1).getPanelID())))
						tabbedPane.remove(i);
				}
				break;
			
			}
		}
	}

	@Override
	public boolean setAddPanelListener(ActionListener add, ActionListener save, ActionListener load) {
		addFolio.addActionListener(add);
		addFolio.doClick();	//trigger once on startup
		saveFolio.addActionListener(save);
		loadFolio.addActionListener(load);
		return true;
	}

	@Override
	public boolean save(Object o) {
		JFileChooser fileChooser = new JFileChooser();
	    int retval = fileChooser.showSaveDialog(frame);
	    if (retval == JFileChooser.APPROVE_OPTION) {
	    	File file = fileChooser.getSelectedFile();
	        if (file == null) {
	          return false;
	        }
	        
	        try {
	        	FileOutputStream fos = new FileOutputStream(file);
	        	ObjectOutputStream oos = new ObjectOutputStream(fos);
	        	oos.writeObject(o);
	        	oos.close();
	        	return true;
	        	
	        } catch (Exception e) {
	          e.printStackTrace();
	        }
	      }
		return false;
	}

	@Override
	public Object load() {
		JFileChooser fileChooser = new JFileChooser();
		Object loaded = new Object();
	    int retval = fileChooser.showSaveDialog(frame);
	    if (retval == JFileChooser.APPROVE_OPTION) {
	    	File file = fileChooser.getSelectedFile();
	        if (file == null) {
	        }
	        
	        try {
	        	FileInputStream fis = new FileInputStream(file);
	        	ObjectInputStream ois = new ObjectInputStream(fis);
	        	loaded = ois.readObject();
	        	ois.close();
	        	
	        } catch (Exception e) {
	          e.printStackTrace();
	        }
	      }
		return loaded;
	}


}
