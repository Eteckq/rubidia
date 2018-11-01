package me.pmilon.RubidiaCore.RManager;

public enum Mastery {

	VAGRANT(0,0,"Vagabond"),
	ADVENTURER(1,15,"Aventurier"),
	ASPIRANT(2,40,"Aspirant"),
	MASTER(3,60,"Maître"),
	HERO(4,120,"Héros");
	
	private final int id;
	private final int level;
	private final String name;
	private Mastery(int id,int level, String name){
		this.id = id;
		this.level = level;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public int getLevel() {
		return level;
	}

	public int getId() {
		return id;
	}
	
}
