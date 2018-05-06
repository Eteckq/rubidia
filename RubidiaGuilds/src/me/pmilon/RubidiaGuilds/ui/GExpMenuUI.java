package me.pmilon.RubidiaGuilds.ui;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.UIHandler;
import me.pmilon.RubidiaCore.ui.UIType;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaGuilds.GuildsPlugin;
import me.pmilon.RubidiaGuilds.guilds.Guild;
import me.pmilon.RubidiaGuilds.guilds.Permission;
import me.pmilon.RubidiaGuilds.guilds.Relation;
import me.pmilon.RubidiaGuilds.utils.LevelUtils;

public class GExpMenuUI extends UIHandler {

	private ItemStack ITEM_BACK = new ItemStack(Material.MELON, 1);
	private ItemStack ITEM_INFOS = new ItemStack(Material.EXP_BOTTLE, 1);
	
	private Guild guild;
	
	private final int SLOT_BACK = 8;
	private final int SLOT_INFOS = 7;
	public GExpMenuUI(Player p, Guild guild) {
		super(p);
		this.guild = guild;
		this.menu = Bukkit.createInventory(this.getHolder(), 9, StringUtils.abbreviate(this.getGuild().getName() + rp.translateString(" : Offerings", " : Offrandes"), 32));
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public UIType getType() {
		return UIType.UNKNOWN;
	}

	@Override
	protected void onGeneralClick(InventoryClickEvent e, final Player p) {
		if(e.getCurrentItem() != null){
			if(e.isShiftClick()){
				if(!LevelUtils.loots.contains(e.getCurrentItem().getType()) && !e.getCurrentItem().getType().equals(Material.AIR)){
					e.setCancelled(true);
					rp.sendMessage("�cThis is not a valid offering.", "�cCe n'est pas une bonne offrande.");
				}else{
					Bukkit.getScheduler().runTaskLater(GuildsPlugin.instance, new Runnable(){
						public void run(){
							menu.setItem(SLOT_INFOS, getInfos(getValue()));
							Utils.updateInventory(p);
						}
					}, 0);
				}
			}
		}
	}

	@Override
	protected void onInventoryClick(InventoryClickEvent e, final Player p) {
		if(e.getCurrentItem() != null){
			int slot = e.getRawSlot();
			if(slot < this.SLOT_INFOS){
				if(!LevelUtils.loots.contains(e.getCursor().getType()) && !e.getCursor().getType().equals(Material.AIR)){
					e.setCancelled(true);
					rp.sendMessage("�cThis is not a valid offering.", "�cCe n'est pas une bonne offrande.");
				}else{
					Bukkit.getScheduler().runTaskLater(GuildsPlugin.instance, new Runnable(){
						public void run(){
							menu.setItem(SLOT_INFOS, getInfos(getValue()));
							Utils.updateInventory(p);
						}
					}, 0);
				}
			}else{
				e.setCancelled(true);
				if(slot == this.SLOT_INFOS){
					if(gm.getPermission(Permission.OFFER)){
						int oldLevel = this.getGuild().getLevel();
						double value = this.getValue();
						this.getGuild().setExperience(this.getGuild().getExperience()+value);
						int coloredBars = (int) (30*(this.getGuild().getExperience()/LevelUtils.getLevelExperienceAmount(this.getGuild().getLevel())));
						String xpBar = "";
						for(int i = 0;i < 30;i++){
							if(i == 0){
								if(i < coloredBars)xpBar += "�4[";
								else xpBar += "�8[";
							}else if(i == 29){
								if(i < coloredBars)xpBar += "�4]";
								else xpBar += "�8]";
							}else{
								if(i < coloredBars)xpBar += "�c|";
								else xpBar += "�7|";
							}
						}
						int newLevel = this.getGuild().getLevel();
						this.getGuild().broadcastMessage(Relation.MEMBER, "�&d>>> �&cYour guild has gain �&d" + value + " �&cmore XP!", "�&d>>> �&cVotre guilde a gagn� �&d" + value + " �&cpoint d'XP suppl�mentaire !");
						if(oldLevel < newLevel)this.getGuild().broadcastMessage(Relation.MEMBER, "�&d>>> �&cLevel up! �&d" + this.getGuild().getName() + " �&cis now level �&d" + newLevel + "�&c!", "�&d>>> �&cNiveau sup�rieur atteint ! �&d" + this.getGuild().getName() + " �&cest d�sormais niveau �&d" + newLevel + " �&c!");
						this.getGuild().broadcastMessage(Relation.MEMBER, "�&d>>> " + xpBar, "�&d>>> " + xpBar);
						
						for(int invSlot = 0;invSlot < 7;invSlot++){
							this.menu.setItem(invSlot, new ItemStack(Material.AIR));
						}
						this.menu.setItem(this.SLOT_INFOS, this.getInfos(this.getValue()));
					}else rp.sendMessage("�cYou don't have permission to offer for your guild!", "�cVous n'avez pas la permission de faire des offrandes pour votre guilde !");
				}else if(slot == this.SLOT_BACK){
					this.empty();
					Core.uiManager.requestUI(new GInfosMenuUI(this.getHolder(), this.getGuild()));
				}
			}
		}
	}

	@Override
	protected void onInventoryClose(InventoryCloseEvent e, Player arg1) {
		this.empty();
	}

	@Override
	protected boolean openWindow() {
		this.menu.setItem(this.SLOT_BACK, this.getBack());
		this.menu.setItem(this.SLOT_INFOS, this.getInfos(0.0));
		return this.getHolder().openInventory(this.menu) != null;
	}
	
	public void empty(){
		for(int slot = 0;slot < this.SLOT_INFOS;slot++){
			ItemStack stack = this.menu.getItem(slot);
			if(stack != null)this.getHolder().getInventory().addItem(stack);
		}
	}
	
	protected ItemStack getBack(){
		ItemMeta META_BACK = ITEM_BACK.getItemMeta();
		META_BACK.setDisplayName("�6�l" + rp.translateString("Informations menu", "Menu des informations"));
		META_BACK.setLore(Arrays.asList(rp.translateString("�7Get back to the informations menu.", "�7Retourner au menu des informations."), "", rp.translateString("�e�lClick to open", "�e�lCliquez pour ouvrir")));
		ITEM_BACK.setItemMeta(META_BACK);
		return ITEM_BACK;
	}
	protected ItemStack getInfos(double exp){
		ItemMeta META_BACK = ITEM_INFOS.getItemMeta();
		META_BACK.setDisplayName("�a�l" + rp.translateString("Confirm offering", "Confirmer l'offrande"));
		
		int level = this.getGuild().getLevel();
		double experience = this.getGuild().getExperience() + exp;
		while(experience > LevelUtils.getLevelExperienceAmount(level)){
			experience -= LevelUtils.getLevelExperienceAmount(level);
			level++;
		}
		double factor = (experience/LevelUtils.getLevelExperienceAmount(level));
		int coloredBars = (int) (30*factor);
		String xpBar = "";
		for(int i = 0;i < 30;i++){
			if(i == 0){
				if(i < coloredBars)xpBar += "�4[";
				else xpBar += "�8[";
			}else if(i == 29){
				if(i < coloredBars)xpBar += "�4]";
				else xpBar += "�8]";
			}else{
				if(i < coloredBars)xpBar += "�c|";
				else xpBar += "�7|";
			}
		}
		META_BACK.setLore(Arrays.asList(rp.translateString("�cAll of your items will disappear.", "�cTous vos items dispara�tront."), "", rp.translateString("�6�lFinal level: ", "�6�lNiveau final : ") + "�e" + level + "     " + xpBar + "   ", "", rp.translateString("�e�lClick to confirm", "�e�lCliquez pour confirmer")));
		ITEM_INFOS.setItemMeta(META_BACK);
		return ITEM_INFOS;
	}
	
	public double getValue(){//dont forget to add type to levelUtil.loots list
		double value = 0;
		for(int slot = 0;slot < 7;slot++){
			ItemStack stack = this.menu.getItem(slot);
			if(stack != null){
				if(stack.getType().equals(Material.ROTTEN_FLESH))value += .15*stack.getAmount();
				else if(stack.getType().equals(Material.STRING))value += .19*stack.getAmount();
				else if(stack.getType().equals(Material.SULPHUR))value += .23*stack.getAmount();
				else if(stack.getType().equals(Material.BONE))value += .18*stack.getAmount();
				else if(stack.getType().equals(Material.BLAZE_ROD))value += .34*stack.getAmount();
				else if(stack.getType().equals(Material.SPIDER_EYE))value += .2*stack.getAmount();
				else if(stack.getType().equals(Material.ENDER_PEARL))value += .27*stack.getAmount();
				//else if(stack.getType().equals(Material.NETHER_STAR))value += 1.82*stack.getAmount();
				else if(stack.getType().equals(Material.DRAGON_EGG))value += 5.23*stack.getAmount();
				else if(stack.getType().equals(Material.NETHER_WARTS))value += .01*stack.getAmount();
				else if(stack.getType().equals(Material.WHEAT))value += .008*stack.getAmount();
				else if(stack.getType().equals(Material.CARROT_ITEM))value += .012*stack.getAmount();
				else if(stack.getType().equals(Material.MELON))value += .005*stack.getAmount();
				else if(stack.getType().equals(Material.PUMPKIN))value += .015*stack.getAmount();
				else if(stack.getType().equals(Material.POTATO_ITEM))value += .012*stack.getAmount();
				else if(stack.getType().equals(Material.MELON_BLOCK))value += .03*stack.getAmount();
				else if(stack.getType().equals(Material.SUGAR_CANE))value += .01*stack.getAmount();
			}
		}
		return value;
	}

	public Guild getGuild() {
		return guild;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
	}

}
