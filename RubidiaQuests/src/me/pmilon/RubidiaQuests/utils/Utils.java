package me.pmilon.RubidiaQuests.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaQuests.QuestHelpRunnable;
import me.pmilon.RubidiaQuests.QuestsPlugin;
import me.pmilon.RubidiaQuests.commands.QuestItemCommandExecutor;
import me.pmilon.RubidiaQuests.quests.ObjectiveType;
import me.pmilon.RubidiaQuests.quests.QEvent;
import me.pmilon.RubidiaQuests.quests.QEventType;
import me.pmilon.RubidiaQuests.quests.Quest;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

public class Utils {

	public static boolean isQuestItem(ItemStack item){
		if(item != null){
			if(!item.getType().equals(Material.AIR)){
				ItemMeta meta = item.getItemMeta();
				List<String> lore = meta.getLore();
				if(lore != null){
					if(!lore.isEmpty()){
						return lore.get(0).equals(QuestItemCommandExecutor.QUEST_STRING);
					}
				}
			}
		}
		return false;
	}
	
	public static void updateFollowedQuest(final Player player, boolean updateTasks){
		RPlayer rp = RPlayer.get(player);
		if(updateTasks){
			QuestHelpRunnable task = rp.getQuestHelpTask();
			if(task != null){
				task.cancel();
			}
		}
		Quest quest = rp.getFollowedQuest();
		Objective objective = player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
		if(objective != null)objective.unregister();
		if(quest != null){
			if(rp.getActiveQuests().contains(quest)){
				List<String> display = new ArrayList<String>();
				if(updateTasks){
					QuestHelpRunnable task = new QuestHelpRunnable(QuestsPlugin.instance, player, quest);
					rp.setQuestHelpTask(task);
					task.runTaskTimer(0, 60);
				}
				display.addAll(Arrays.asList(quest.getColoredTitle(),quest.getColoredSubtitle()));
				List<me.pmilon.RubidiaQuests.quests.Objective> objectives = me.pmilon.RubidiaCore.utils.Utils.getModifiableCopy(quest.getObjectives());
				List<me.pmilon.RubidiaQuests.quests.Objective> times = me.pmilon.RubidiaCore.utils.Utils.getModifiableCopy(quest.getObjectivesByType(ObjectiveType.TIME));
				List<QEvent> events = me.pmilon.RubidiaCore.utils.Utils.getModifiableCopy(quest.getQEventsByType(QEventType.SPAWN));
				objectives.removeAll(times);
				int index = 0;
				int size = objectives.size()+times.size()+events.size();
				int max = 0;
				String displayS = "";
				if(!times.isEmpty()){
					display.add("");
					for(int i = 0;i < times.size();i++){
						if(index >= 8 && size > 9)break;
						me.pmilon.RubidiaQuests.quests.Objective obj = times.get(i);
						displayS = "  " + obj.getInfos(rp);
						display.add(displayS);
						max = Math.max(max, ChatColor.stripColor(displayS).length());
						index++;
					}

					if(updateTasks){
						new BukkitTask(Core.instance){

							@Override
							public void run() {
								if(player.isOnline()){
									Utils.updateFollowedQuest(player, false);
								}else this.cancel();
							}

							@Override
							public void onCancel() {
							}
							
						}.runTaskTimer(20, 20);
					}
				}
				display.addAll(Arrays.asList("","§7§l" + rp.translateString("Objectives:", "Objectifs :")));
				for(int i = 0; i < events.size();i++){
					if(index >= 8 && size > 9)break;
					QEvent event = events.get(i);
					int amount = 0;
					if(event.monsters.containsKey(rp))amount = event.monsters.get(rp).size();
					displayS = "  " + (amount > 0 ? "§4[✘] §c" : "§2[✔] §a") + rp.translateString(amount + " remaining " + event.getMonster().getName() + (amount > 1 && !event.getMonster().getName().endsWith("s") ? "s" : "") + " to kill", amount + " " + event.getMonster().getName() + (amount > 1 && !event.getMonster().getName().endsWith("s") ? "s" : "") + " restant" + (amount > 1 && !event.getMonster().getName().endsWith("s") ? "s" : "") + " à éliminer");
					display.add(displayS);
					max = Math.max(max, ChatColor.stripColor(displayS).length());
					index++;
				}
				for(int i = 0; i < objectives.size();i++){
					if(index >= 8 && size > 9)break;
					me.pmilon.RubidiaQuests.quests.Objective obj = objectives.get(i);
					if(!obj.isFilled(rp)){
						displayS = "  " + obj.getInfos(rp);
						display.add(displayS);
						max = Math.max(max, ChatColor.stripColor(displayS).length());
						index++;
					}
				}
				for(int i = 0; i < objectives.size();i++){
					if(index >= 8 && size > 9)break;
					me.pmilon.RubidiaQuests.quests.Objective obj = objectives.get(i);
					if(obj.isFilled(rp)){
						displayS = "  " + obj.getInfos(rp);
						display.add(displayS);
						max = Math.max(max, ChatColor.stripColor(displayS).length());
						index++;
					}
				}
				if(index >= 8 && quest.getObjectives().size() > 9)display.add("    §7" + rp.translateString("and " + (quest.getObjectives().size()-index) + " more...", "et " + (quest.getObjectives().size()-index) + " autres..."));
				String separation = "";
				for(int i = 0;i < max && i < 36;i++){
					separation += "-";
				}
				display.addAll(Arrays.asList("", "§7§m" + separation));
				Utils.display("§7§l" + rp.translateString("Quest help", "Quête suivie"), display.toArray(new String[display.size()]), player);
			}else rp.setFollowedQuest(null);
		}
	}
	
	public static void display(String title, String[] strings, Player player){
		int space = 0;
		Objective objective = player.getScoreboard().registerNewObjective(title, "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		for(int i = 0;i < strings.length;i++){
			if(strings[i].equals("")){
				for(int s = 0;s < space;s++){
					strings[i] += " ";
				}
				space++;
			}
			
			objective.getScore(StringUtils.abbreviate(strings[i], 40)).setScore(strings.length-i-1);
		}
	}
}
