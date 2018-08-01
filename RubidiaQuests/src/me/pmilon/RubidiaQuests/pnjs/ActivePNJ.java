package me.pmilon.RubidiaQuests.pnjs;

import java.util.Arrays;

import me.pmilon.RubidiaQuests.dialogs.DialogType;
import me.pmilon.RubidiaQuests.dialogs.PNJDialog;
import me.pmilon.RubidiaQuests.utils.Configs;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public abstract class ActivePNJ extends PNJHandler {

	private String dialog;
	public ActivePNJ(String uuid, String title, String titlePrefix,
			String name, String namePrefix, PNJType type, Location loc,
			int age, boolean fix, String dialog) {
		super(uuid, title, titlePrefix, name, namePrefix, type, loc, age, fix);
		this.dialog = dialog;
	}

	@Override
	protected void onRightClick(PlayerInteractEntityEvent e, final Player p, Villager villager) {
		if(this.getDialog() != null){
			if(!this.getDialog().isEmpty()){
				p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1, 1);
				PNJDialog dialog = new PNJDialog(p, this, villager, Arrays.asList(this.getDialog().replaceAll("%rapid", "")), DialogType.PREDIALOG, new Runnable(){
					public void run(){
						onTalk(p);
					}
				}, false, true);
				dialog.start();
				return;
			}
		}
		onTalk(p);
	}

	@Override
	protected void onSave() {
		Configs.getPNJConfig().set("pnjs." + this.getUniqueId() + ".dialog", this.getDialog());
		onSubSave();
	}

	protected abstract void onDelete();
	
	protected abstract void onTalk(Player player);
	
	protected abstract void onSubSave();

	public String getDialog() {
		return dialog;
	}

	public void setDialog(String dialog) {
		this.dialog = dialog;
	}

}
