package controller;

import java.io.IOException;

import view.View;
import view.iView;
import model.iModel;
import model.Model;

public class Main {
	
	public static void main(String[] args) throws IOException {
		
		iModel model = new Model();
		
		iView gui = new View();
		gui.run();
		
		Controller controller = new Controller(model,gui);
		controller.addCertificate();
		controller.addListeners();
		
	}

}
