package model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import org.patriques.AlphaVantageConnector;
import org.patriques.BatchStockQuotes;
import org.patriques.output.quote.BatchStockQuotesResponse;
import org.patriques.output.quote.data.StockQuote;

public class Stock implements iStock, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1242804553591349235L;
	private DecimalFormat df;
	private String symbol;
	private int numShares;
	private double stockPrice;
	private double value;

	public Stock(String symbol, int numShares) {
		df = new DecimalFormat("#.##"); // round to 2 significant digits
		this.symbol = symbol;
		this.numShares = numShares;
		refresh();
	}

	public Stock(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public boolean addShares(int shares) {
		if (numShares + shares < 0)
			return false;
		numShares += shares;
		updateValue();
		return true;
	}

	@Override
	public int getNumShares() {
		return numShares;
	}

	@Override
	public double getTotalValue() {
		return value;
	}

	@Override
	public double getPricePerShare() {
		return stockPrice;
	}

	@Override
	public String getName() {
		return symbol;
	}

	private void updateValue() {
		value = Double.valueOf(df.format(numShares * stockPrice));
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof Stock))
			return false;
		return (((Stock) other).getName().equals(this.getName()));
	}

	private boolean refresh() {
		String apiKey = "ZQNM5AY2ETSDDETS";
		int timeout = 3000;
		AlphaVantageConnector apiConnector = new AlphaVantageConnector(apiKey,
				timeout);

		BatchStockQuotes batchStockQuotes = new BatchStockQuotes(apiConnector);
		BatchStockQuotesResponse response = batchStockQuotes.quote(symbol);

		List<StockQuote> stockData = response.getStockQuotes();
		if (stockData.isEmpty())
			throw new NoStockFoundException("No such stock");
		
		stockData.forEach(stock -> {
			stockPrice = stock.getPrice();
		});
		updateValue(); // make value is updated

		return true;
	}

}
