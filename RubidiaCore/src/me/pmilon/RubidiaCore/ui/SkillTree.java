package me.pmilon.RubidiaCore.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.Mastery;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.abilities.RAbility;
import me.pmilon.RubidiaCore.packets.WrapperPlayServerSetSlot;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.Note.Tone;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class SkillTree extends UIHandler {

	RClass rClass = rp.getRClass().equals(RClass.VAGRANT) ? RClass.PALADIN : rp.getRClass();
	List<RAbility> abilities = RAbility.getAvailable(rClass);//to get all available
	List<RAbility> leveledUp = RAbility.getAvailable(rp);//to get all abilities with enough level
	private boolean canClick = true;
	
	public SkillTree(Player p) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), 9, ("Arbre des compétences"));
	}

	@Override
	public String getType(){
		return "SKILLTREE_MENU";
	}
	
	@Override
	protected boolean openWindow() {
		getMenu().setItem(0, this.getSkp());
		if(rp.getRClass().equals(RClass.VAGRANT))abilities = RAbility.getAvailable(rClass);
		for(RAbility ability : abilities){
			getMenu().setItem(ability.getIndex(), this.getAbility(ability));
		}
		return this.getHolder().openInventory(getMenu()) != null;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
		if(e.getCurrentItem() != null){
			if(!e.getCurrentItem().getType().equals(Material.AIR)){
				e.setCancelled(true);
				int slot = e.getRawSlot();
				if(!rp.getRClass().equals(RClass.VAGRANT)){
					if(slot > 0){
						if(canClick){
							canClick = false;
							new BukkitTask(Core.instance){
								public void run(){
									canClick = true;
								}

								@Override
								public void onCancel() {
									// TODO Auto-generated method stub
									
								}
							}.runTaskLater(6);
							int amount = 1;
							if(e.isShiftClick())amount = 5;
							if(rp.getSkillPoints() >= amount){
								RAbility ability = RAbility.valueOf(rClass.toString() + "_" + slot);
								if(ability.getSettings().getLevelMax() < rp.getAbLevel(slot)+amount){
									amount = ability.getSettings().getLevelMax()-rp.getAbLevel(slot);
								}
								if(amount == 0){
									rp.sendMessage("§cVous avez déjà atteint le niveau maximal pour cette capacité.");
									this.getHolder().playSound(this.getHolder().getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
								}else{
									if(rp.isAtLeast(ability.getMastery())){
										if(e.isShiftClick()){
											this.doMegaUpSound();
										}else{
											this.doUpSound();
										}
										rp.setAbLevel(slot, rp.getAbLevel(slot)+amount);
										rp.setSkillPoints(rp.getSkillPoints()-amount);
										leveledUp = RAbility.getAvailable(rp);
										getMenu().setItem(slot, this.getAbility(ability));
										getMenu().setItem(0, this.getSkp());
										if(rp.getSkillPoints() < 1){
											for(RAbility ability1 : abilities){
												if(slot != ability1.getIndex())getMenu().setItem(ability1.getIndex(), this.getAbility(ability1));
											}
										}
										if(ability.getSettings().getLevelMax() == rp.getAbLevel(slot))rp.sendMessage("§aVous avez atteint le niveau maximal pour cette capacité.");
									}else{
										if(ability.getMastery().equals(Mastery.MASTER))rp.sendMessage("§cCette compétence est bloquée ! Utilisez-la au niveau " + Mastery.MASTER.getLevel() + " en devenant maître !");
										else if(ability.getMastery().equals(Mastery.HERO))rp.sendMessage("§cCette compétence est bloquée ! Utilisez-la au niveau " + Mastery.HERO.getLevel() + " en devenant héros !");
									}
								}
							}else{
								this.getHolder().playSound(this.getHolder().getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
								rp.sendMessage("§cVous n'avez pas assez de points de compétence !");
							}
						}
					}
				}else{
					if(slot == 0){
						if(rClass.equals(RClass.PALADIN))rClass = RClass.RANGER;
						else if(rClass.equals(RClass.RANGER))rClass = RClass.MAGE;
						else if(rClass.equals(RClass.MAGE))rClass = RClass.ASSASSIN;
						else if(rClass.equals(RClass.ASSASSIN))rClass = RClass.PALADIN;
						rp.sendMessage("§cPrésentation des compétences des " + rClass.toString().toLowerCase() + "s.");
						this.close(false);
						Core.uiManager.requestUI(this);
					}else{
						rp.sendMessage("§cVous êtes vagabond ! Ces compétences ne sont qu'en présentation.");
					}
				}
			}
		}
	}
	
	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}
	
	private ItemStack getSkp(){
		ItemStack item = new ItemStack(rp.getSkillPoints() > 0 ? Material.BOOK : Material.WRITABLE_BOOK, rp.getSkillPoints() > 64  || rp.getSkillPoints() < 1 ? 1 : rp.getSkillPoints());
		ItemMeta meta = item.getItemMeta();
		String color = (rp.getSkillPoints() > 0 ? "§2" : "§4");
		String ccolor = (rp.getSkillPoints() > 0 ? "§a" : "§c");
		meta.setDisplayName(ccolor + "§l" + rp.getSkillPoints() + color + " " + ("point" + (rp.getSkillPoints() > 1 ? "s" : "") + " de compétences"));
		meta.setLore(Arrays.asList("§7" + ("Les points de compétences sont gagnés à chaque niveau."), "§7" + ("Ils vous permettent d'augmenter le niveau d'une des"), "§7" + ("compétences suivantes, modifiant ses dégâts et son coût."), "", rp.getRClass().equals(RClass.VAGRANT) ? ("§6Présentation des compétences des " + rClass.toString().toLowerCase() + "s.") : ("§8Clic gauche pour augmenter d'un niveau."), rp.getRClass().equals(RClass.VAGRANT) ? ("§e§lCliquez pour changer de classe présentée.") : ("§8Sneak + clic gauche pour augmenter de 5 niveaux.")));
		item.setItemMeta(meta);
		return item;
	}
	
	private ItemStack getAbility(RAbility ability){
		boolean has = leveledUp.contains(ability);
		double max = ability.getRClass().equals(RClass.RANGER) ? 385.0 : 60.0;
		ItemStack item = new ItemStack(rp.isAtLeast(ability.getMastery()) ? ability.getRClass().getBaseWeapon() : Material.BARRIER, rp.getAbLevel(ability.getIndex()) < 1 ? 1 : rp.getAbLevel(ability.getIndex()));
		ItemMeta meta = item.getItemMeta();
		((Damageable) meta).setDamage((int) (has ? ability.getRClass().getBaseWeapon().getMaxDurability()*(1.0004-(8-ability.getIndex())/max) : ability.getRClass().getBaseWeapon().getMaxDurability()*(1.0004-(16-ability.getIndex())/max)));
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
		String color = has ? "§2" : "§4";
		String ccolor = has ? "§a" : "§c";
		meta.setDisplayName(color + (ability.getMastery().equals(Mastery.MASTER) ? ("§l[MAITRE] ") : (ability.getMastery().equals(Mastery.HERO) ? ("§l[HEROS] ") : "")) + ccolor + ability.getName() + color + (ability.isPassive() ? " - PASSIVE" : " - ACTIVE"));
		List<String> lore = new ArrayList<String>();
		List<String> description = ability.getDescription();
		for(int i = 0;i < (description.size()/2);i++){
			lore.add("§7" + (description.get(i+(description.size()/2))));
		}
		String keystroke = "";
		if(ability.isPassive()){
			keystroke = "Passive";
		}else{
			String seq = ability.getSequence();
			String[] part = seq.split(",");
			if(part.length > 1){
				if(part[1].contains("SN") && !part[1].contains("!SN"))keystroke = "Sneak + ";
				if(part[1].contains("SP") && !part[1].contains("!SP"))keystroke = "Sprint + ";
			}
			String[] clicks = part[0].split("");
			if(clicks.length > 0)keystroke += ("Clic ");
			for(int i = 0;i < clicks.length;i++){
				if(clicks[i].equals("D"))keystroke += ("Droit");
				else if(clicks[i].equals("G"))keystroke += ("Gauche");
				if(i != clicks.length-1)keystroke += "/";
			}
			if(clicks.length > 0)keystroke += ("");
		}
		double damages = Utils.round(ability.getDamages(rp)*(ability.isPassive() ? 1 : rp.getAbilityDamagesFactor()), 2);
		double upDamages = Utils.round(((rp.getAbilityLevel(ability.getIndex())+1)*ability.getSettings().getDamagesPerLevel()+ability.getSettings().getDamagesMin())*(ability.isPassive() ? 1 : rp.getAbilityDamagesFactor()), 2);
		double cost = Utils.round(ability.getVigorCost(rp), 2);
		double upCost = Utils.round(((rp.getAbLevel(ability.getIndex())+1)*ability.getSettings().getVigorPerLevel()+ability.getSettings().getVigorMin()), 2);
		String cdamColor = damages-upDamages > 0 ? "§c" : "§a";
		String ddamColor = damages-upDamages > 0 ? "§4" : "§2";
		String ccostColor = cost-upCost < 0 ? "§c" : "§a";
		String dcostColor = cost-upCost < 0 ? "§4" : "§2";
		lore.addAll(Arrays.asList("", "§6§lUtilisation : §7" + keystroke,
				"§6§lCoût : §7" + cost + " EP" + (rp.getSkillPoints() > 0 && ability.getSettings().getLevelMax() > rp.getAbLevel(ability.getIndex()) ? " §e§l>>§a " + ccostColor + upCost + " " + dcostColor + "EP" : ""),
				"§6§l" + (ability.getSuppInfo().isEmpty() ? "Dégâts" : ability.getSuppInfo()) + " : §7" + damages + ability.getUnit() + (rp.getSkillPoints() > 0 && ability.getSettings().getLevelMax() > rp.getAbLevel(ability.getIndex()) ? " §e§l>>§a " + cdamColor + upDamages + " " + ddamColor + ability.getUnit() : ""),
				"", "§e§lNiveau " + rp.getAbLevel(ability.getIndex()) + "/" + ability.getSettings().getLevelMax()));
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	private void doUpSound(){
		final Player player = this.getHolder();
		player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(0, Tone.D));
		new BukkitTask(Core.instance){
			public void run(){
				player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(0, Tone.F));
			}

			@Override
			public void onCancel() {
			}
		}.runTaskLater(2);
	}

	private void doMegaUpSound() {
		final Player player = this.getHolder();
		player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(0, Tone.D));
		new BukkitTask(Core.instance){
			public void run(){
				player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(0, Tone.G));
				new BukkitTask(Core.instance){
					public void run(){
						player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(0, Tone.A));
						new BukkitTask(Core.instance){
							public void run(){
								player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Tone.B));
							}

							@Override
							public void onCancel() {
							}
						}.runTaskLater(2);
					}

					@Override
					public void onCancel() {
					}
				}.runTaskLater(2);
			}

			@Override
			public void onCancel() {
			}
		}.runTaskLater(2);
	}
	
	@Override
	public void onGeneralClick(InventoryClickEvent e, Player p) {
		if(e.isShiftClick())e.setCancelled(true);
	}
	
	public void update(){
		new BukkitTask(Core.instance){

			@Override
			public void run() {
				if(getHolder().getOpenInventory().getTopInventory() == null)return;
				if(rp.getSkillPoints() > 64){
					WrapperPlayServerSetSlot packet = new WrapperPlayServerSetSlot();
					packet.setWindowId(((CraftPlayer)getHolder()).getHandle().activeContainer.windowId);
					packet.setSlot(0);
					packet.setSlotData(getSkp());
					packet.sendPacket(getHolder());
				}
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskLater(1);//needs to a tick after inventory update
	}
}
