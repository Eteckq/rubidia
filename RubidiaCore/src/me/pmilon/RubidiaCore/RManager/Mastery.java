package me.pmilon.RubidiaCore.RManager;

public enum Mastery {

	VAGRANT(0,0,"Vagrant","Vagabond"),
	ADVENTURER(1,15,"Adventurer","Aventurier"),
	ASPIRANT(2,40,"Aspirant","Aspirant"),
	MASTER(3,60,"Master","Maître"),
	HERO(4,120,"Hero","Héros");
	
	private final int id;
	private final int level;
	private final String nameEN;
	private final String nameFR;
	private Mastery(int id,int level, String nameEN, String nameFR){
		this.id = id;
		this.level = level;
		this.nameEN = nameEN;
		this.nameFR = nameFR;
	}
	
	public String getNameEN() {
		return nameEN;
	}
	
	public String getNameFR() {
		return nameFR;
	}

	public int getLevel() {
		return level;
	}

	public int getId() {
		return id;
	}
	
}
