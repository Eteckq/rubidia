package me.pmilon.RubidiaQuests.quests;

import org.bukkit.Material;

public enum ObjectiveType {

	GET(Material.NETHER_STAR, "Apporter "),
	MINE(Material.IRON_PICKAXE, "Collecter "),
	CRAFT(Material.WORKBENCH, "Forger "),
	KILL(Material.IRON_SWORD, "Tuer "),
	DISCOVER(Material.MAP, "Aller "),
	TALK(Material.FEATHER, "Parler � "),
	TIME(Material.WATCH, "Terminer en "),
	FISH(Material.FISHING_ROD, "P�cher "),
	LEASH(Material.LEASH, "Amener "),
	FOLLOW(Material.COMPASS, "Accompagner "),
	SIDE_QUEST(Material.BOOK, "Parler � ");
	
	private final Material display;
	private final String toDo;
	private ObjectiveType(Material display, String toDo){
		this.display = display;
		this.toDo = toDo;
	}
	
	public Material getDisplay() {
		return display;
	}
	
	public String getToDo() {
		return toDo;
	}
	
}
