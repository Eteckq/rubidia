package me.pmilon.RubidiaQuests.pnjs;

import java.util.List;

import me.pmilon.RubidiaCore.chat.RChatFixDisplay;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaQuests.dialogs.PNJDialog;
import me.pmilon.RubidiaQuests.utils.Configs;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

public class PasserPNJ extends DialogerPNJ {

	private String targetName;
	private Location targetLocation;
	
	public PasserPNJ(String uuid, String name, Location loc, int age, List<String> dialogs, boolean fix, String targetName, Location targetLocation) {
		super(uuid, "PASSEUR", "§9§l", name, "§b", PNJType.PASSER, age, loc, dialogs, fix);
		this.targetName = targetName;
		this.targetLocation = targetLocation;
	}

	@Override
	public void onDialogEnd(Player p, PNJDialog dialog) {
		dialog.setKeepDialogOnEnd(true);
		RPlayer rp = RPlayer.get(p);
		p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1, 1);
		
	    TextComponent accept = new TextComponent(("§a[ ✔  Accepter]"));
	    TextComponent refuse = new TextComponent(("§c[ ✘  Refuser]"));
	    ClickEvent acceptEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pass yes " + this.getUniqueId());
	    ClickEvent refuseEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pass no");
	    accept.setClickEvent(acceptEvent);
	    refuse.setClickEvent(refuseEvent);
	    TextComponent answer = new TextComponent("          ");
	    answer.addExtra(accept);
	    answer.addExtra(new TextComponent("   §7§m-§r   "));
	    answer.addExtra(refuse);
	   	rp.getChat().addFixDisplay(new RChatFixDisplay(rp,-1,null).setClosable(false).addLines("",("  §e> Souhaitez-vous allez à " + this.getTargetName() + " ?")).addText(answer).addLine(""));
	}

	@Override
	protected void onSpawn(Villager villager) {
	}

	@Override
	protected void onDelete() {
	}

	public Location getTargetLocation() {
		return targetLocation;
	}

	public void setTargetLocation(Location targetLocation) {
		this.targetLocation = targetLocation;
	}

	@Override
	protected void onSubSave() {
		Configs.getPNJConfig().set("pnjs." + this.getUniqueId() + ".targetName", this.getTargetName());
		Configs.getPNJConfig().set("pnjs." + this.getUniqueId() + ".targetLocation", this.getTargetLocation());
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

}
