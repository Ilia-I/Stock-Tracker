package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Panel implements iPanel, Observer {

	private JButton deleteButt, btnBuy, btnSell;
	private JTextField tickerTextField;
	private JNumberTextField sharesTextField;
	private JComponent panel;
	private JTable table;
	private final int id;
	private JTextField profitField;
	private DefaultTableModel model;

	public Panel(int id, ActionListener buy, ActionListener sell, ActionListener del){
		this.id = id;
		makePanel();
		setBuyListener(buy);
		setSellListener(sell);
		setDeleteListener(del);
	}


	private void makePanel(){
		panel = new JPanel(false);
		panel.setLayout(new BorderLayout());
		panel.add(makeTickerPanel(), BorderLayout.PAGE_START);
		panel.add(makeTablePanel(), BorderLayout.CENTER);
		panel.add(makeButtonPanel(), BorderLayout.PAGE_END);
	}
	
	public JComponent getPanel() {
		return panel;
	}

	private Component makeTablePanel() {
		JPanel bigPan = new JPanel(false);
		JPanel tablePanel = new JPanel(false);
		bigPan.setLayout(new BorderLayout());
		TableData td = new TableData();
		model = new DefaultTableModel(td.getDataLst(),td.getColumns());
		table = new JTable(model);
		
		table.setName("table");
		table.setEnabled(false);
		table.getTableHeader().setReorderingAllowed(false);
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.PAGE_AXIS));
		tablePanel.add(table.getTableHeader());
		tablePanel.add(table);
		tablePanel.setBackground(Color.WHITE);
		bigPan.add(tablePanel, BorderLayout.CENTER);
		bigPan.add(makeProfitField(0.0), BorderLayout.PAGE_END);
		return bigPan;
	}
	
	private Component makeButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));

		deleteButt = new JButton("Delete");

		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 5));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(deleteButt);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.setBackground(Color.LIGHT_GRAY);
		
		return buttonPanel;
	}

	private JComponent makeProfitField(double profit){
		String pr = "Your profit so far : " + profit; 
		profitField = new JTextField(pr);
		profitField.setHorizontalAlignment(JTextField.CENTER);
		profitField.setEnabled(false);
		return profitField;
	}

	private Component makeTickerPanel() {
		JPanel panel = new JPanel(false);
		JLabel lblTickerSymbol = new JLabel("Ticker Symbol:");
		panel.add(lblTickerSymbol);

		tickerTextField = new JTextField();
		panel.add(tickerTextField);
		tickerTextField.setColumns(10);

		JLabel lblNumberOfShares = new JLabel("Number of Shares:");
		panel.add(lblNumberOfShares);

		sharesTextField = new JNumberTextField();
		panel.add(sharesTextField);
		sharesTextField.setColumns(10);

		btnBuy = new JButton("Buy");
		panel.add(btnBuy);

		btnSell = new JButton("Sell");
		panel.add(btnSell);

		return panel;
	}
	
	private void setBuyListener(ActionListener list){
		btnBuy.addActionListener(list);
	}
	
	private void setSellListener(ActionListener list) {
		btnSell.addActionListener(list);
	}
	
	private void setDeleteListener(ActionListener list){
		deleteButt.addActionListener(list);
	}
	


	@Override
	public int getID() {
		return id;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof iPanel))
				return false;

		return (((iPanel) other).getID()==this.getID());
	}


	@Override
	public void update(Observable arg0, Object arg1) {
		//System.out.println("update called on panel " +id);
		if (arg1 instanceof Object[][]){
			Object[][] dataList = (Object[][]) arg1;
			TableData td = new TableData(dataList);
			model.setDataVector(td.getDataLst(), td.getColumns());
			model.fireTableDataChanged();
		}
		else if(arg1 instanceof Double){
			profitField.setText("Total value : " + arg1);
			
		}
		else
			System.out.println("wrong object type");
		//table.repaint();
		
	}


	@Override
	public String getStockSymbol() {
		return tickerTextField.getText();
	}


	@Override
	public String getStockShares() {
		return sharesTextField.getText();
	}



	
}
