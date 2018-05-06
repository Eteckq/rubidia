package me.pmilon.RubidiaQuests.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.ListMenuUIHandler;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.quests.Objective;
import me.pmilon.RubidiaQuests.quests.ObjectiveType;
import me.pmilon.RubidiaQuests.quests.QEvent;
import me.pmilon.RubidiaQuests.quests.QEventType;
import me.pmilon.RubidiaQuests.quests.Quest;
import me.pmilon.RubidiaQuests.quests.Reward;
import me.pmilon.RubidiaQuests.utils.Utils;

public class PlayerQuestList extends ListMenuUIHandler<Quest> {

	private int taskId;
	
	public PlayerQuestList(Player p) {
		super(p, StringUtils.abbreviate(p.getName() + " | Active Quests", 32), StringUtils.abbreviate(p.getName() + " | Quêtes actives", 32), 3);
		for(int i = 0;i < rp.getActiveQuests().size();i++){
			this.list.add(rp.getActiveQuests().get(i));
		}
	}

	@Override
	protected ItemStack getInformations() {
		ItemStack item = new ItemStack(Material.MELON, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(rp.translateString("§8§lActive quests list", "§8§lListe des quêtes actives"));
		meta.setLore(Arrays.asList(rp.translateString("§7Left click a quest to get help", "§7Cliquez gauche sur une quête pour obtenir un suivi"), rp.translateString("§7Right click a quest to give up", "§7Cliquez droit sur une quête pour l'abandonner")));
		item.setItemMeta(meta);
		return item;
	}

	@Override
	protected ItemStack getItem(Quest quest) {
		ItemStack item = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(quest.getColoredTitle());
		List<String> lore = new ArrayList<String>();
		lore.addAll(Arrays.asList(quest.getColoredSubtitle(), ""));
		List<Objective> objectives = quest.getObjectivesByType(ObjectiveType.TIME);
		if(!objectives.isEmpty()){
			for(Objective objective : objectives){
				lore.add(objective.getInfos(this.rp));
			}
			lore.add("");
		}
		lore.add("§4§l" + rp.translateString("Objectives:", "Objectifs :"));
		for(Objective objective : quest.getObjectives()){
			if(!objective.getType().equals(ObjectiveType.TIME))lore.add("  " + objective.getInfos(this.rp));
		}
		for(QEvent qEvent : quest.getQEvents()){
			if(qEvent.getType().equals(QEventType.SPAWN)){
				int amount = 0;
				if(qEvent.monsters.containsKey(rp))amount = qEvent.monsters.get(rp).size();
				if(qEvent.getMonster() != null)lore.add("  " + (amount > 0 ? "§4[✘] §c" : "§2[✔] §a") + rp.translateString(amount + " remaining " + qEvent.getMonster().getName() + (amount > 1 && !qEvent.getMonster().getName().endsWith("s") ? "s" : "") + " to kill", amount + " " + qEvent.getMonster().getName() + (amount > 1 && !qEvent.getMonster().getName().endsWith("s") ? "s" : "") + " restant" + (amount > 1 && !qEvent.getMonster().getName().endsWith("s") ? "s" : "") + " à éliminer"));
			}
		}
		lore.addAll(Arrays.asList("", "§6§l" + rp.translateString("Rewards:", "Récompenses :")));
		for(Reward reward : quest.getRewards()){
			lore.add("  " + reward.getInfos(this.rp));
		}
		lore.add("");
		if(quest.getHolder(rp) != null){
			lore.add("§8§l" + rp.translateString("Given by: ", "Donnée par : ") + "§7" + quest.getHolder(rp).getName());
			if(!quest.isAutoFinish())lore.add(rp.translateString("§8§oGet back to this NPC once you finished the quest!", "§8§oRetournez voir ce PNJ une fois la quête terminée !"));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	@Override
	protected void onClick(InventoryClickEvent e, Player player, ItemStack arg2) {
		Quest quest = this.get(e.getRawSlot());
		if(e.isRightClick()){
			if(quest.isGiveupable() || rp.isOp()){
				if(rp.isOp() && !quest.isGiveupable())rp.sendMessage("§eYou could give up this quest because you were Operator.", "§eVous avez pu abandonner cette quête car vous êtes Opérateur.");
				Core.uiManager.requestUI(new GiveUpConfirmationUI(rp, quest));
			}else rp.sendMessage("§cYou cannot give up this quest!", "§cVous ne pouvez abandonner cette quête !");
		}else{
			Quest followed = rp.getFollowedQuest();
			if(followed != null){
				rp.sendMessage("§cYou stopped following quest §4" + followed.getColoredTitle() + "§c.", "§cVous avez arrêté le suivi de la quête §4" + followed.getColoredTitle() + "§c.");
				rp.setFollowedQuest(null);
			}
			if(quest != null){
				if(!quest.equals(followed)){
					rp.setFollowedQuest(quest);
					rp.sendMessage("§aYou started following quest §2" + quest.getColoredTitle() + "§a.", "§aVous avez lancé le suivi de la quête §2" + quest.getColoredTitle() + "§a.");
				}
			}
			Utils.updateFollowedQuest(this.getHolder(), true);
		}
	}

	@Override
	protected void onInfosClick(InventoryClickEvent e) {
		//
	}

	@Override
	public String getType() {
		return "PLAYER_QUESTS_LIST_MENU";
	}

	@Override
	protected void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
		Bukkit.getScheduler().cancelTask(this.taskId);
	}

	public void update(){
		for(Quest quest : this.list){
			if(rp.getActiveQuests().contains(quest)){
				if(!quest.getObjectivesByType(ObjectiveType.TIME).isEmpty()){
					this.menu.setItem(this.list.indexOf(quest), this.getItem(quest));
				}
			}else{
				Bukkit.getScheduler().cancelTask(this.taskId);
				Core.uiManager.requestUI(new PlayerQuestList(this.getHolder()));
				break;
			}
		}
	}

	@Override
	protected void onOpen() {
		this.taskId = Bukkit.getScheduler().runTaskTimer(QuestsPlugin.instance, new Runnable(){
			public void run(){
				update();
			}
		}, 0, 20).getTaskId();
	}

	@Override
	protected void onPageTurn() {
		// TODO Auto-generated method stub
		
	}
	
}
