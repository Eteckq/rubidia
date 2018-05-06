package me.pmilon.RubidiaQuests.pnjs;

import java.util.List;

import me.pmilon.RubidiaQuests.dialogs.DialogType;
import me.pmilon.RubidiaQuests.dialogs.PNJDialog;
import me.pmilon.RubidiaQuests.utils.Configs;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public abstract class DialogerPNJ extends PNJHandler {

	private List<String> dialogs;
	public DialogerPNJ(String uuid, String title, String titlePrefix, String name, String namePrefix, PNJType type, int age,
			Location loc, List<String> dialogs, boolean fix) {
		super(uuid, title, titlePrefix, name, namePrefix, type, loc, age, fix);
		this.dialogs = dialogs;
	}

	public List<String> getDialogs() {
		return dialogs;
	}

	public void setDialogs(List<String> dialogs) {
		this.dialogs = dialogs;
	}

	@Override
	protected void onSave() {
		Configs.getPNJConfig().set("pnjs." + this.getUniqueId() + ".dialogs", this.getDialogs());
		onSubSave();
	}

	@Override
	protected void onRightClick(final PlayerInteractEntityEvent e, final Player p, Villager villager) {
		final PNJDialog dialog = new PNJDialog(p, this, villager, this.getDialogs(), DialogType.NONDIALOG, null, false, true);
		Runnable runnable = new Runnable(){
			public void run(){
				onDialogEnd(p, dialog);
			}
		};
		dialog.setDialogEnd(runnable);
		dialog.start();
	}
	
	public abstract void onDialogEnd(final Player p, PNJDialog dialog);
	
	protected abstract void onSubSave();

}
