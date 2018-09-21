package view;

import java.util.Observable;
import java.util.Observer;


public class TableData implements Observer{
	private String[] columns = { "Ticker Symbol", "Number of Shares", "Price per Share", "Value of Holding" };
	private Object[][] dataList;
	
	public TableData(Object[][] dataList){
		this.dataList=dataList;
	}
	
	public TableData(){
		dataList=new Object[1][columns.length];
	}
	
	public Object[][] getDataLst(){
		return dataList;
	}
	
	public String[] getColumns(){
		return columns;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	

}
