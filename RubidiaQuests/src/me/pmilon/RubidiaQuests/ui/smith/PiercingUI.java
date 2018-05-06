package me.pmilon.RubidiaQuests.ui.smith;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.Note.Tone;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ritems.general.RItem;
import me.pmilon.RubidiaCore.ritems.general.RItemStack;
import me.pmilon.RubidiaCore.ritems.general.RItemStacks;
import me.pmilon.RubidiaCore.ritems.weapons.Piercing;
import me.pmilon.RubidiaCore.ritems.weapons.Piercing.PiercingType;
import me.pmilon.RubidiaCore.ritems.weapons.Weapon;
import me.pmilon.RubidiaCore.scrolls.Scroll;
import me.pmilon.RubidiaCore.scrolls.ScrollType;
import me.pmilon.RubidiaCore.scrolls.ScrollType.ScrollUsage;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;
import me.pmilon.RubidiaCore.utils.Utils;
import me.pmilon.RubidiaQuests.QuestsPlugin;

public class PiercingUI extends UIHandler {

	private ItemStack WPN_SLOT = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14);
	private ItemStack ORI_SLOT = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)8);
	private ItemStack FREE_SLOT = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)8);
	private ItemStack LOCKED_SLOT = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)7);
	
	private Weapon weapon;
	private Random random = new Random();
	private BukkitTask endTask = null;
	private int finalSlot;
	//private boolean started = false;
	private Piercing piercing;
	private List<Scroll> scroll = new ArrayList<Scroll>();
	private boolean orichalcum = false;
	
	public PiercingUI(Player p) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), 9, "Piercing");
		
		ItemMeta meta = FREE_SLOT.getItemMeta();
		meta.setDisplayName("§f§l" + rp.translateString("FREE SLOT", "PORE LIBRE"));
		meta.setLore(Arrays.asList("§7" + rp.translateString("Select a joyau to place inside in your inventory.", "Sélectionnez un joyau à placer à l'intérieur dans votre inventaire."), "§7" + rp.translateString("WARNING: once placed, it is sealed!", "ATTENTION : une fois placé, il est scellé !")));
		FREE_SLOT.setItemMeta(meta);
		
		meta.setDisplayName("§8§l" + rp.translateString("LOCKED SLOT", "PORE FERME"));
		meta.setLore(Arrays.asList("§7" + rp.translateString("Pierce your weapon/armor to unlock", "Percez votre arme/armure pour ouvrir"), "§7" + rp.translateString("this slot and place a joyau inside.", "ce pore et y placer un joyau.")));
		LOCKED_SLOT.setItemMeta(meta);
		
		meta.setDisplayName("§f§l" + rp.translateString("WEAPON/ARMOR", "ARME/ARMURE"));
		meta.setLore(Arrays.asList("§7" + rp.translateString("Select a weapon/armor to pierce,", "Sélectionnez une arme/armure à percer,"), "§7" + rp.translateString("allowing to place joyaux inside and", "vous permettant d'y placer des joyaux"), "§7" + rp.translateString("increasing your statistics (while you use it).", "augmentant vos statistiques (lorsque vous l'utilisez).")));
		WPN_SLOT.setItemMeta(meta);
		
		meta.setDisplayName("§f§lORICHALQUE");
		meta.setLore(Arrays.asList("§7" + rp.translateString("Select an Orichalque to use to pierce", "Sélectionnez un Orichalque à utiliser"), "§7" + rp.translateString("your weapon/armor (unlock a slot).", "pour le perçage de votre arme/armure."), "§7" + rp.translateString("You will then be able to place a joyau inside.", "Vous pourrez ensuite y placer un joyau.")));
		ORI_SLOT.setItemMeta(meta);
		
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@Override
	public String getType() {
		return "PIERCING_MENU";
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player p) {
		if(this.endTask != null){
			e.setCancelled(true);
			return;
		}
		if(e.getCurrentItem() != null){
			ItemStack item = e.getCurrentItem();
			if(!item.getType().equals(Material.AIR)){
				RItem rItem = new RItem(item);
				if(rItem.isWeapon()){
					e.setCancelled(true);
					e.setCurrentItem(new ItemStack(Material.AIR));
					if(this.weapon != null){
						this.getHolder().getInventory().addItem(this.weapon.getNewItemStack(rp));
					}
					this.weapon = rItem.getWeapon();
					this.update();
				}else if(rItem.isScroll()){
					Scroll scroll = rItem.getScroll();
					if(scroll.getType().getUsage().equals(ScrollUsage.PIERCING)){
						if(this.weapon != null){
							e.setCancelled(true);
							if(!this.isScrollActive(scroll.getType())){
								this.scroll.add(scroll);
								item.setAmount(item.getAmount()-1);
								if(item.getAmount() <= 0)item = new ItemStack(Material.AIR);
								e.setCurrentItem(item);
								this.update();
							}
						}else rp.sendMessage("§cChoose a weapon/armor to pierce first.", "§cChoisissez d'abord une arme/armure à percer.");
					}else rp.sendMessage("§cThis scroll cannot be used during a piercing.", "§cCe parchemin ne peut être utilisé durant un piercing.");
				}else if(item.isSimilar(RItemStacks.ORICHALCUM.getItemStack())){
					e.setCancelled(true);
					if(weapon != null){
						if(weapon.getHoles() < 8){
							if(!this.orichalcum){
								item.setAmount(item.getAmount()-1);
								if(item.getAmount() <= 0)item = new ItemStack(Material.AIR);
								e.setCurrentItem(item);
								this.orichalcum = true;
								this.update();
							}
						}else rp.sendMessage("§cYour " + (this.weapon.isAttack() ? "weapon" : "armor") + " already has too many holes!", "§cVotre " + (this.weapon.isAttack() ? "arme" : "armure") + " a déjà trop été percée !");
					}else rp.sendMessage("§cChoose a weapon/armor to pierce first.", "§cChoisissez d'abord une arme/armure à percer.");
				}else{
					piercing = null;
					for(Field field : RItemStacks.class.getFields()){
						if(field.getType().equals(RItemStack.class)){
							if(field.getName().startsWith("POWDER_")){
								try {
									RItemStack powder = ((RItemStack) field.get(null));
									if(powder.getItemStack().isSimilar(item)){
										piercing = new Piercing(PiercingType.valueOf(field.getName().split("_")[1]));
										break;
									}
								} catch (IllegalArgumentException | IllegalAccessException ex) {
									ex.printStackTrace();
								}
							}
						}
					}
					if(piercing != null){
						rp.sendMessage("§ePlace this joyau inside an open slot!", "§ePlacez ce joyau à l'intérieur d'un pore ouvert !");
					}else e.setCancelled(true);
				}
			}else piercing = null;
		}
	}

	@Override
	public void onInventoryClick(final InventoryClickEvent e, Player p) {
		e.setCancelled(true);
		if(e.getCurrentItem() != null){
			ItemStack item = e.getCurrentItem();
			if(!item.getType().equals(Material.AIR)){
				final int slot = e.getRawSlot();
				if(slot == 0){
					if(this.orichalcum){
						this.getHolder().getInventory().addItem(RItemStacks.ORICHALCUM.getItemStack());
						this.orichalcum = false;
					}
					if(this.weapon != null){
						this.getHolder().getInventory().addItem(this.weapon.getNewItemStack(rp));
					}
					this.weapon = null;
					this.scroll.clear();
					this.update();
				}else if(slot == 8 && !item.isSimilar(FREE_SLOT)){
					if(this.endTask == null && item.getType().equals(Material.DOUBLE_PLANT)){
						this.endTask = new BukkitTask(QuestsPlugin.instance){

							@Override
							public void run() {
								ItemStack item = getMenu().getItem(finalSlot);
								if(item.isSimilar(LOCKED_SLOT)){
									weapon.setHoles(weapon.getHoles()+1);
									rp.sendMessage("§aPiercing successful! Your " + (weapon.isAttack() ? "weapon" : "armor") + " has now an additional hole.", "§aPiercing réussi ! Votre " + (weapon.isAttack() ? "arme" : "armure") + " a désormais un pore ouvert supplémentaire.");
								}else if(item.isSimilar(FREE_SLOT)){
									if(isScrollActive(ScrollType.PIERCING_PROTECTION_B)){
										weapon.setHoles(weapon.getHoles()-1);
										rp.sendMessage("§cPiercing failed! A hole in your " + (weapon.isAttack() ? "weapon" : "armor") + " has been filled.", "§cEchec du piercing ! Un pore de votre " + (weapon.isAttack() ? "arme" : "armure") + " a été fermé.");
									}else rp.sendMessage("§ePiercing failed! But the scroll protected a hole from being closed.", "§eEchec du piercing ! Mais le parchemin a empêché un pore d'être refermé.");
								}else if(weapon.getPiercings().size() >= finalSlot){
									if(isScrollActive(ScrollType.PIERCING_PROTECTION_A)){
										weapon.getPiercings().remove(finalSlot-1);
										rp.sendMessage("§cPiercing failed! A hole in your " + (weapon.isAttack() ? "weapon" : "armor") + " has been emptied.", "§cEchec du piercing ! Un pore de votre " + (weapon.isAttack() ? "arme" : "armure") + " a été vidé.");
									}else rp.sendMessage("§ePiercing failed! But the scroll protected a hole from being emptied.", "§eEchec du piercing ! Mais le parchemin a empêché un pore d'être vidé.");
								}
								orichalcum = false;
								scroll.clear();
								getHolder().getWorld().playSound(getHolder().getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1, 1);
								update();
								endTask = null;
							}

							@Override
							public void onCancel() {
							}
							
						};
						double iterations = random.nextInt(12)+24;
						long waitTime = 0;
						for(int j = 0;j <= iterations;j++){
							final int k = j;
							final int i = (k+7)%8+1;
							waitTime += (long) Math.round(Math.pow(j/iterations, 1.74)*9+3);
							final boolean end = j == iterations;
							final UIHandler instance = this;
							new BukkitTask(QuestsPlugin.instance){
								@Override
								public void run() {
									if(Core.uiManager.hasActiveSession(getHolder())){
										if(Core.uiManager.getSession(getHolder()).getUIHandler().equals(instance)){
											getMenu().setItem(i, i > weapon.getHoles() ? LOCKED_SLOT : (i > weapon.getPiercings().size() ? FREE_SLOT : getDisplay(weapon.getPiercings().get(i-1))));
											getMenu().setItem(k%8+1, RItemStacks.ORICHALCUM.getItemStack());
											getHolder().playNote(getHolder().getLocation(), Instrument.PIANO, Note.flat(0, Tone.D));
											if(end){
												endTask.runTaskLater(20);
											}
										}
									}
								}

								@Override
								public void onCancel() {
								}
								
							}.runTaskLater(waitTime);
							if(j == iterations)finalSlot = i;
						}
					}
				}else{
					if(item.isSimilar(LOCKED_SLOT)){
						rp.sendMessage("§cYou need an orichalque to unlock this slot.", "§cVous avez besoin d'un orichalque pour débloquer ce pore.");
					}else if(item.isSimilar(FREE_SLOT)){
						if(piercing != null){
							weapon.getPiercings().add(piercing);
							e.setCancelled(false);
							e.setCurrentItem(new ItemStack(Material.AIR));
							new BukkitTask(QuestsPlugin.instance){

								@Override
								public void run() {
									ItemStack itm = getMenu().getItem(slot).clone();
									getMenu().setItem(slot, getDisplay(piercing));
									if(!e.isRightClick()){
										itm.setAmount(itm.getAmount()-1);
										if(itm.getAmount() <= 0)itm = new ItemStack(Material.AIR);
										getHolder().getInventory().addItem(itm);
									}
									update();
								}

								@Override
								public void onCancel() {
								}
								
							}.runTaskLater(0);
						}else rp.sendMessage("§cYou can only place joyau inside your " + (weapon.isAttack() ? "weapon" : "armor") + "!", "§cVous ne pouvez placer que des joyaux à l'intérieur de votre " + (this.weapon.isAttack() ? "arme" : "armure") + " !");
					}
				}
			}
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		if(this.endTask != null){
			endTask.run();
		}else{
			if(this.orichalcum){
				this.getHolder().getInventory().addItem(RItemStacks.ORICHALCUM.getItemStack());
			}
			if(!this.scroll.isEmpty()){
				for(Scroll scroll : this.scroll){
					scroll.give(this.getHolder());
				}
			}
		}
		if(weapon != null){
			this.getHolder().getInventory().addItem(weapon.getNewItemStack(rp));
		}
	}

	@Override
	protected boolean openWindow() {
		this.getMenu().setItem(0, WPN_SLOT.clone());
		return this.getHolder().openInventory(this.menu) != null;
	}

	private ItemStack getDisplay(Piercing piercing){
		ItemStack item = null;
		RItemStack rItem = RItemStacks.valueOf("POWDER_" + piercing.getType().toString());
		if(rItem != null){
			item = rItem.getItemStack();
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(meta.getDisplayName() + " §4§o(" + rp.translateString("sealed", "scellé") + ")");
			meta.setLore(Arrays.asList("§d" + rp.translateString(piercing.getType().getDisplayEn(), piercing.getType().getDisplayFr()) + " +" + piercing.getType().getAmount()));
			item.setItemMeta(meta);
		}
		return item;
	}
	
	private void update(){
		if(this.weapon != null){
			this.getMenu().setItem(0, this.weapon.updateState(rp, this.weapon.getNewItemStack(rp)));
			if(this.scroll != null){
				this.getMenu().setItem(0, Utils.setGlowingWithoutAttributes(this.getMenu().getItem(0)));
			}
			for(int i = 1;i < 9;i++){
				this.getMenu().setItem(i, i > weapon.getHoles() ? this.LOCKED_SLOT : (i > weapon.getPiercings().size() ? this.FREE_SLOT : this.getDisplay(weapon.getPiercings().get(i-1))));
			}
			if(weapon.getHoles() <= 7)this.getMenu().setItem(8, ORI_SLOT);
			if(this.orichalcum){
				ItemStack start = RItemStacks.ORICHALCUM.getItemStack();
				ItemMeta meta = start.getItemMeta();
				meta.setDisplayName("§f§lOrichalque");
				List<String> lore = new ArrayList<String>();
				lore.addAll(Arrays.asList(rp.translateString("§7During piercing, this orichalcum", "§7Durant le piercing, cet orichalque"), rp.translateString("§7will pass over all the slots.", "§7va passer au dessus de chacun des pores."), "§7" + rp.translateString("If it stops over a locked slot, it will open.", "S'il s'arrête au dessus d'un pore fermé, il s'ouvre."), "§7" + rp.translateString("If it stops over a filled slot, it will be emptied.", "S'il s'arrête au dessus d'un pore rempli, il sera vidé."), "§7" + rp.translateString("If it stops over an empty slot, it will close.","S'il s'arrête au dessus d'un pore ouvert, il se ferme."), rp.translateString("§7You can use appropriate scrolls to prevent it.", "§7Vous pouvez utiliser des parchemins appropriés pour l'éviter."), ""));
				if(!this.scroll.isEmpty()){
					lore.add("§6§l" + rp.translateString("Active scrolls:", "Parchemins actifs :"));
					for(Scroll scroll : this.scroll){
						lore.add("  §e" + rp.translateString(scroll.getType().getNameEn(),scroll.getType().getNameFr()));
					}
					lore.add("");
				}
				lore.add(rp.translateString("§fClick here to start piercing.", "§fCliquez ici pour débuter le piercing."));
				meta.setLore(lore);
				start.setItemMeta(meta);
				this.getMenu().setItem(8, start);
			}
		}else{
			for(int i = 1;i < 9;i++){
				this.getMenu().setItem(i, new ItemStack(Material.AIR));
			}
			this.getMenu().setItem(0, WPN_SLOT.clone());
		}
	}
	
	private boolean isScrollActive(ScrollType type){
		for(Scroll scroll : this.scroll){
			if(scroll.getType().equals(type)){
				return true;
			}
		}
		return false;
	}
	
}
