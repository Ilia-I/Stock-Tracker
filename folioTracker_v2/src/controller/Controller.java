package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.patriques.output.AlphaVantageException;
import org.patriques.output.LimitedCallsException;

import model.NoStockFoundException;

import model.iStock;
import model.iModel;
import view.iView;

public class Controller {

	private iModel model;
	private iView view;
	private int i = 0;

	public Controller(iModel model, iView view) {
		this.model = model;
		this.view = view;
		model.addObserver(view);
	}

	public void addListeners() {
		view.setAddPanelListener(new AddFolioListener(), new SaveFolioListener(), new LoadFolioListener());
	}

	public boolean addCertificate() {
		try {
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			Path ksPath = Paths.get(System.getProperty("java.home"), "lib",
					"security", "cacerts");
			keyStore.load(Files.newInputStream(ksPath),
					"changeit".toCharArray());

			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			try (InputStream caInput = new BufferedInputStream(
			// this files is shipped with the application
					AddCertificate.class.getResourceAsStream("DSTRootCAX3.der"))) {
				Certificate crt = cf.generateCertificate(caInput);
				System.out.println("Added Cert for "
						+ ((X509Certificate) crt).getSubjectDN());

				keyStore.setCertificateEntry("DSTRootCAX3", crt);
			}

			TrustManagerFactory tmf = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(keyStore);
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, tmf.getTrustManagers(), null);
			SSLContext.setDefault(sslContext);
			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private int checkUserIn(String s) {
		if (s.isEmpty()) {
			view.ShowError("Please enter non-zero number of shares to buy/sell");
			return -1;
		} else if ((Integer.parseInt(s) < 1)) {
			view.ShowError("You must enter a whole number of shares, greater than 0!\n You entered: "
					+ s);
			return -1;
		}

		return Integer.parseInt(s);
	}
	
	abstract class panelListener{
		private final int id;
		
		public panelListener(int id) {
			this.id = id;
		}
		
		public int getID(){
			return id;
		}
	}
	
	abstract class userInputListener extends panelListener{

		public userInputListener(int id) {
			super(id);
		}
		
		public String getStockName(){
			return view.getUserInStock(getID());
		}
		
		public int getNumShares(){
			return checkUserIn(view.getUserInNumShares(getID()));
			
		}
		
	}

	class BuyListener extends userInputListener implements ActionListener {
		
		public BuyListener(int id) {
			super(id);
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			//System.out.println("listener triggered on folio "+getID());
			int shares = getNumShares();
			String stock = getStockName();
			if (shares<0)
				return;
			if (stock.isEmpty()||stock==null){
				view.ShowError("Stock field should not be empty.");	
				return;
			}
			try{
			if (!model.addShares(getID(), shares, stock)){
				view.ShowError("Stock "+stock+" could not be added");
			}
				
			}
			catch (LimitedCallsException error) {
				view.ShowError("Couldn't retrieve stock information. There is a limit of 5 API calls per minute");
			}
			catch (AlphaVantageException error) {
				view.ShowError("Error when accessing the stock info");
			}
			catch (NoStockFoundException error){
				view.ShowError("No such stock found as "+ stock);
			}

		}

	}

	class SellListener extends userInputListener implements ActionListener {
		
		public SellListener(int id) {
			super(id);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int shares = getNumShares();
			String stock = getStockName();
			if (shares<0)
				return;
			if (stock.isEmpty()||stock==null){
				view.ShowError("Stock field should not be empty.");	
				return;
			}
			if (!model.sellShares(getID(), getNumShares(), getStockName()))
				view.ShowError("Couldn't sell shares: "+ getStockName());
				
		}

	}

	class DeleteListener extends panelListener implements ActionListener {

		public DeleteListener(int id) {
			super(id);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			model.removeFolio(getID());

		}
	}

	class SaveFolioListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int i = view.getSelectedPanel();
			if (i<0)
				view.ShowError("Please open a new folio before saving");
			else
				view.save(model.getFolio(i).getList());
			
		}
	}
	
	class LoadFolioListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object o = view.load();
			if (o instanceof List){
				List list = (List)o;
				if (!list.isEmpty() && list.get(0) instanceof iStock){
					model.addFolio(i, list, view.addPanel(i, new BuyListener(i),
							new SellListener(i), new DeleteListener(i)));
					i++;
				}
				

			}
			else
				view.ShowError("You have selected an invalid file");
			
		}
	}
	
	class AddFolioListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			model.addFolio(i, view.addPanel(i, new BuyListener(i),
					new SellListener(i), new DeleteListener(i)));
			i++;
		}
	}

}
