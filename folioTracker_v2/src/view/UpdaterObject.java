package view;

public class UpdaterObject{
	String command;
	int panelID;
	
	public UpdaterObject(String command, int panelID){
		this.command=command;
		this.panelID=panelID;
	}
	
	public String getCommand(){
		return command;
	}

	public int getPanelID(){
		return panelID;
	}
}
