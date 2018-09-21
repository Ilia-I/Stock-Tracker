package model;

public class NoStockFoundException extends RuntimeException{
	
	private static final long serialVersionUID = -6173419481052749645L;

	public NoStockFoundException(String message, Exception e) { 
		 super(message, e); 
		 }
	 
	 public NoStockFoundException(String message) { 
		 super(message); 
		 }

}
