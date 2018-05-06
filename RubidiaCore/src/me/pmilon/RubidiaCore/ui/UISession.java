package me.pmilon.RubidiaCore.ui;

public class UISession {

	private final String identifier;
	private final UIHandler uihandler;
	
	public UISession(UIHandler uihandler){
		this.uihandler = uihandler;
		this.identifier = uihandler.getUniqueId();
	}
	
	public String getIdentifier(){
		return this.identifier;
	}
	
	public UIHandler getUIHandler(){
		return this.uihandler;
	}
	
}
