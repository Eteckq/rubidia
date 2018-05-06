package me.pmilon.RubidiaQuests.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ui.abstracts.ListMenuUIHandler;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.quests.Objective;
import me.pmilon.RubidiaQuests.quests.Quest;
import me.pmilon.RubidiaQuests.quests.QuestType;
import me.pmilon.RubidiaQuests.quests.Reward;

public class QuestListRewardChoose extends ListMenuUIHandler<Quest> {

	private Quest quest;
	private QuestPNJ pnj;
	private Reward reward;
	public QuestListRewardChoose(Player p, Quest quest, QuestPNJ pnj, Reward reward) {
		super(p, StringUtils.abbreviate(quest.getTitle() + " | Reward " + reward.getIndex(),32), StringUtils.abbreviate(quest.getTitle() + " | Reward " + reward.getIndex(),32), 5);
		this.quest = quest;
		this.pnj = pnj;
		this.reward = reward;
		HashMap<String, List<Quest>> quests = new HashMap<String, List<Quest>>();
		for(QuestType type : QuestType.values()){
			quests.put(type.toString(), new ArrayList<Quest>());
		}
		for(Quest qst : QuestsPlugin.questColl.data()){
			quests.get(qst.getType().toString()).add(qst);
		}
		for(QuestType type : QuestType.values()){
			List<Quest> questss = quests.get(type.toString());
			Collections.sort(questss, new Comparator<Quest>(){

				@Override
				public int compare(Quest q1, Quest q2) {
					return q1.getTitle().compareTo(q2.getTitle());
				}
				
			});
			this.list.addAll(questss);
		}
	}

	@Override
	protected ItemStack getInformations() {
		ItemStack item = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Click one to choose or click here to get back");
		item.setItemMeta(meta);
		return item;
	}

	@Override
	protected ItemStack getItem(Quest quest) {
		Wool wool = new Wool(quest.getType().getColor());
		ItemStack item = wool.toItemStack(1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(quest.getColoredTitle());
		List<String> lore = new ArrayList<String>();
		lore.add(quest.getColoredSubtitle());
		for(Objective objective : quest.getObjectives()){
			lore.add("�8" + objective.getType().getToDo() + objective.getInformation());
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	@Override
	protected void onClick(InventoryClickEvent e, Player arg1, ItemStack arg2) {
		this.getReward().setQuestTargetUUID(this.get(e.getRawSlot()).getUUID());
		Core.uiManager.requestUI(new RewardEditionMenu(this.getHolder(), this.getQuest(), this.getPnj(), this.getReward()));
	}

	@Override
	protected void onInfosClick(InventoryClickEvent e) {
		Core.uiManager.requestUI(new RewardEditionMenu(this.getHolder(), this.getQuest(), this.getPnj(), this.getReward()));
	}

	@Override
	public String getType() {
		return "REWARD_QUEST_SELECTION_MENU";
	}

	@Override
	protected void onInventoryClose(InventoryCloseEvent arg0, Player arg1) {
	}

	public Quest getQuest() {
		return quest;
	}

	public void setQuest(Quest quest) {
		this.quest = quest;
	}

	public QuestPNJ getPnj() {
		return pnj;
	}

	public void setPnj(QuestPNJ pnj) {
		this.pnj = pnj;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

	@Override
	protected void onOpen() {
		// nothing
	}

	@Override
	protected void onPageTurn() {
		// TODO Auto-generated method stub
		
	}
}
