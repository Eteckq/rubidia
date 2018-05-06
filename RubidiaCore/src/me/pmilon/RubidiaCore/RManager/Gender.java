package me.pmilon.RubidiaCore.RManager;

public enum Gender {
	
	MALE("Man","Homme"),
	FEMALE("Woman","Femme"),
	UNKNOWN("Unknown","Inconnu");
	
	private final String en;
	private final String fr;
	private Gender(String en, String fr){
		this.en = en;
		this.fr = fr;
	}
	
	public String getEn() {
		return en;
	}

	public String getFr() {
		return fr;
	}
	
}
