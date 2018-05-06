package me.pmilon.RubidiaCore.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.Mastery;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.abilities.AbilitiesAPI;
import me.pmilon.RubidiaCore.abilities.Ability;
import me.pmilon.RubidiaCore.packets.WrapperPlayServerSetSlot;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.ui.managers.UIType;
import me.pmilon.RubidiaCore.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.Note.Tone;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SkillTree extends UIHandler {

	RClass rClass = rp.getRClass().equals(RClass.VAGRANT) ? RClass.PALADIN : rp.getRClass();
	List<Ability> abilities = AbilitiesAPI.getAvailable(rClass);//to get all available
	List<Ability> leveledUp = AbilitiesAPI.getAvailable(rp);//to get all abilities with enough level
	private boolean canClick = true;
	
	public SkillTree(Player p) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), 9, rp.translateString("SkillTree", "Arbre des compétences"));
	}

	@Override
	public UIType getType(){
		return UIType.SKILLTREE;
	}
	
	@Override
	protected boolean openWindow() {
		getMenu().setItem(0, this.getSkp());
		if(rp.getRClass().equals(RClass.VAGRANT))abilities = AbilitiesAPI.getAvailable(rClass);
		for(Ability ability : abilities){
			getMenu().setItem(ability.getIndex(), this.getAbility(ability));
		}
		return this.getHolder().openInventory(getMenu()) != null;
	}

	@Override
	protected void onInventoryClick(InventoryClickEvent e, Player p) {
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
								Ability ability = AbilitiesAPI.getAbility(rClass, slot);
								if(ability.getLevelMax() < rp.getAbLevel(slot)+amount){
									amount = ability.getLevelMax()-rp.getAbLevel(slot);
								}
								if(amount == 0){
									rp.sendMessage("§cYou've already reached level max for this ability.", "§cVous avez déjà atteint le niveau maximal pour cette capacité.");
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
										leveledUp = AbilitiesAPI.getAvailable(rp);
										getMenu().setItem(slot, this.getAbility(ability));
										getMenu().setItem(0, this.getSkp());
										if(rp.getSkillPoints() < 1){
											for(Ability ability1 : abilities){
												if(slot != ability1.getIndex())getMenu().setItem(ability1.getIndex(), this.getAbility(ability1));
											}
										}
										if(ability.getLevelMax() == rp.getAbLevel(slot))rp.sendMessage("§aYou reached level max for this ability.", "§aVous avez atteint le niveau maximal pour cette capacité.");
									}else{
										if(ability.getMastery().equals(Mastery.MASTER))rp.sendMessage("§cThis ability is locked! Unlock it being a master at level " + Mastery.MASTER.getLevel() + "!", "§cCette compétence est bloquée ! Utilisez-la au niveau " + Mastery.MASTER.getLevel() + " en devenant maître !");
										else if(ability.getMastery().equals(Mastery.HERO))rp.sendMessage("§cThis ability is locked! Unlock it being a hero at level " + Mastery.HERO.getLevel() + "!", "§cCette compétence est bloquée ! Utilisez-la au niveau " + Mastery.HERO.getLevel() + " en devenant héros !");
									}
								}
							}else{
								this.getHolder().playSound(this.getHolder().getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
								rp.sendMessage("§cYou don't have enough skillpoints!", "§cVous n'avez pas assez de points de compétence !");
							}
						}
					}
				}else{
					if(slot == 0){
						if(rClass.equals(RClass.PALADIN))rClass = RClass.RANGER;
						else if(rClass.equals(RClass.RANGER))rClass = RClass.MAGE;
						else if(rClass.equals(RClass.MAGE))rClass = RClass.ASSASSIN;
						else if(rClass.equals(RClass.ASSASSIN))rClass = RClass.PALADIN;
						rp.sendMessage("§cNow showcasing " + rClass.toString().toLowerCase() + "' abilities.", "§cPrésentation des compétences des " + rClass.toString().toLowerCase() + "s.");
						this.close(false);
						Core.uiManager.requestUI(this);
					}else{
						rp.sendMessage("§cYou are vagrant! These abilities are only here in showcase.", "§cVous êtes vagabond ! Ces compétences ne sont qu'en présentation.");
					}
				}
			}
		}
	}
	
	@Override
	protected void onInventoryClose(InventoryCloseEvent e, Player p) {
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}
	
	private ItemStack getSkp(){
		ItemStack item = new ItemStack(rp.getSkillPoints() > 0 ? Material.BOOK : Material.BOOK_AND_QUILL, rp.getSkillPoints() > 64  || rp.getSkillPoints() < 1 ? 1 : rp.getSkillPoints());
		ItemMeta meta = item.getItemMeta();
		String color = (rp.getSkillPoints() > 0 ? "§2" : "§4");
		String ccolor = (rp.getSkillPoints() > 0 ? "§a" : "§c");
		meta.setDisplayName(ccolor + "§l" + rp.getSkillPoints() + color + " " + rp.translateString("skillpoints", "point" + (rp.getSkillPoints() > 1 ? "s" : "") + " de compétences"));
		meta.setLore(Arrays.asList("§7" + rp.translateString("SkillPoints are gained everytime you level up.", "Les points de compétences sont gagnés à chaque niveau."), "§7" + rp.translateString("They allow you to level up one of the following", "Ils vous permettent d'augmenter le niveau d'une des"), "§7" + rp.translateString("abilities, modifying its damages and its cost.", "compétences suivantes, modifiant ses dégâts et son coût."), "", rp.getRClass().equals(RClass.VAGRANT) ? rp.translateString("§6Showcasing " + rClass.toString().toLowerCase() + "s' abilties.", "§6Présentation des compétences des " + rClass.toString().toLowerCase() + "s.") : rp.translateString("§8Left click to increase level by 1.", "§8Clic gauche pour augmenter d'un niveau."), rp.getRClass().equals(RClass.VAGRANT) ? rp.translateString("§e§lClick to change class showcasing.", "§e§lCliquez pour changer de classe présentée.") : rp.translateString("§8Sneak + left click to increase level by 5.", "§8Sneak + clic gauche pour augmenter de 5 niveaux.")));
		item.setItemMeta(meta);
		return item;
	}
	
	private ItemStack getAbility(Ability ability){
		boolean has = leveledUp.contains(ability);
		double max = ability.getRClass().equals(RClass.RANGER) ? 385.0 : 60.0;
		ItemStack item = new ItemStack(rp.isAtLeast(ability.getMastery()) ? ability.getRClass().getBaseWeapon() : Material.BARRIER, rp.getAbLevel(ability.getIndex()) < 1 ? 1 : rp.getAbLevel(ability.getIndex()), (short) (has ? ability.getRClass().getBaseWeapon().getMaxDurability()*(1.0004-(8-ability.getIndex())/max) : ability.getRClass().getBaseWeapon().getMaxDurability()*(1.0004-(16-ability.getIndex())/max)));
		ItemMeta meta = item.getItemMeta();
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
		String color = has ? "§2" : "§4";
		String ccolor = has ? "§a" : "§c";
		meta.setDisplayName(color + (ability.getMastery().equals(Mastery.MASTER) ? rp.translateString("§l[MASTER] ", "§l[MAITRE] ") : (ability.getMastery().equals(Mastery.HERO) ? rp.translateString("§l[HERO] ", "§l[HEROS] ") : "")) + ccolor + rp.translateString(ability.getName()[0], ability.getName()[1]) + color + (ability.isPassive() ? " - PASSIVE" : " - ACTIVE"));
		List<String> lore = new ArrayList<String>();
		List<String> description = ability.getDescription();
		for(int i = 0;i < (description.size()/2);i++){
			lore.add("§7" + rp.translateString(description.get(i), description.get(i+(description.size()/2))));
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
			if(clicks.length > 0)keystroke += rp.translateString("", "Clic ");
			for(int i = 0;i < clicks.length;i++){
				if(clicks[i].equals("D"))keystroke += rp.translateString("Right", "Droit");
				else if(clicks[i].equals("G"))keystroke += rp.translateString("Left", "Gauche");
				if(i != clicks.length-1)keystroke += "/";
			}
			if(clicks.length > 0)keystroke += rp.translateString(" Click", "");
		}
		double damages = Utils.round((rp.getAbLevel(ability.getIndex())*ability.getDamagesPerLevel()+ability.getDamagesMin())*(ability.isPassive() ? 1 : rp.getAbilityDamagesFactor()), 2);
		double upDamages = Utils.round(((rp.getAbLevel(ability.getIndex())+1)*ability.getDamagesPerLevel()+ability.getDamagesMin())*(ability.isPassive() ? 1 : rp.getAbilityDamagesFactor()), 2);
		double cost = Utils.round((rp.getAbLevel(ability.getIndex())*ability.getVigorPerLevel()+ability.getVigorMin()), 2);
		double upCost = Utils.round(((rp.getAbLevel(ability.getIndex())+1)*ability.getVigorPerLevel()+ability.getVigorMin()), 2);
		String cdamColor = damages-upDamages > 0 ? "§c" : "§a";
		String ddamColor = damages-upDamages > 0 ? "§4" : "§2";
		String ccostColor = cost-upCost < 0 ? "§c" : "§a";
		String dcostColor = cost-upCost < 0 ? "§4" : "§2";
		lore.addAll(Arrays.asList("", rp.translateString("§6§lUsage: ", "§6§lUtilisation : ") + "§7" + keystroke, rp.translateString("§6§lCost: ", "§6§lCoût : ") + "§7" + cost + " EP" + (rp.getSkillPoints() > 0 && ability.getLevelMax() > rp.getAbLevel(ability.getIndex()) ? " §e§l>>§a " + ccostColor + upCost + " " + dcostColor + "EP" : ""), "§6§l" + rp.translateString(ability.getSuppInfo() == null ? "Damages" : ability.getSuppInfo()[0], ability.getSuppInfo() == null ? "Dégâts" : ability.getSuppInfo()[1]) + rp.translateString(": ", " : ") + "§7" + damages + (ability.getUnit() == null ? "" : " " + rp.translateString(ability.getUnit()[0], ability.getUnit()[0])) + (rp.getSkillPoints() > 0 && ability.getLevelMax() > rp.getAbLevel(ability.getIndex()) ? " §e§l>>§a " + cdamColor + upDamages + (ability.getUnit() == null ? "" : " " + ddamColor + rp.translateString(ability.getUnit()[0], ability.getUnit()[0])) : ""), "", "§e§lNiveau " + rp.getAbLevel(ability.getIndex()) + "/" + ability.getLevelMax()));
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
	protected void onGeneralClick(InventoryClickEvent e, Player p) {
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
