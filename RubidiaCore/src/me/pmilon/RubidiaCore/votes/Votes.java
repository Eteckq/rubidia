package me.pmilon.RubidiaCore.votes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RChat.RChatFixDisplay;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Votes {

	public static Vote currentVote = null;
	public static HashMap<String, Long> lastVotes = new HashMap<String, Long>();
	public static List<RPlayer> voters = new ArrayList<RPlayer>();
	public static int currentVoteProAmount = 0;
	public static BukkitTask task = null;
	
	public static boolean startVote(Vote vote){
		if(canBeStarted(vote)){
			currentVote = vote;
			voters.clear();
			for(RPlayer rp : RPlayer.getOnlines()){
			    TextComponent accept = new TextComponent(("§a[ ✔ POUR]"));
			    TextComponent refuse = new TextComponent(("§c[ ✘ CONTRE]"));
			    ClickEvent acceptEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vote 0 0 0 0 0 true");
			    ClickEvent refuseEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vote 0 0 0 0 0 false");
			    accept.setClickEvent(acceptEvent);
			    refuse.setClickEvent(refuseEvent);
			    TextComponent text = new TextComponent("        ");
			    text.addExtra(accept);
			    text.addExtra(new TextComponent("     "));
			    text.addExtra(refuse);
			    rp.getChat().addFixDisplay(new RChatFixDisplay(rp,400,null).addLine("").addLine("   §e> " + vote.getQuestion()).addText(text).addLine(""));
				rp.sendTitle(("§6§lNouveau vote !"), ("§eCliquez dans le chat pour voter"), 5, 90, 30);
			}
			task = new BukkitTask(Core.instance){

				@Override
				public void run() {
					Votes.endCurrentVote();
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskLater(400);
			return true;
		}
		return false;
	}
	
	public static boolean canBeStarted(Vote vote){
		if(lastVotes.containsKey(vote.getType())){
			return currentVote == null && System.currentTimeMillis()-lastVotes.get(vote.getType()) > vote.getTime();
		}
		return currentVote == null;
	}
	
	public static void vote(RPlayer rp, boolean flag){
		voters.add(rp);
		if(flag)currentVoteProAmount++;
		if(voters.size() >= RPlayer.getOnlines().size()){
			Votes.endCurrentVote();
		}
	}
	
	public static boolean hasVoted(RPlayer rp){
		return voters.contains(rp);
	}
	
	public static boolean isOk(){
		return ((double) currentVoteProAmount)/RPlayer.getOnlines().size() >= .5;
	}
	
	public static void endCurrentVote(){
		if(currentVote != null){
			if(isOk()){
				for(RPlayer rp : RPlayer.getOnlines()){
					if(!Votes.hasVoted(rp)){
						rp.getChat().forceCloseFixDisplay();
					}
					rp.sendTitle(("§2§lLe vote est passé !"), "", 5, 90, 30);
				}
				currentVote.run();
			}else{
				for(RPlayer rp : RPlayer.getOnlines()){
					if(!Votes.hasVoted(rp)){
						rp.getChat().forceCloseFixDisplay();
					}
					rp.sendTitle(("§4§lLe vote est annulé"), "", 5, 90, 30);
				}
			}
			lastVotes.put(currentVote.getType(), System.currentTimeMillis());
			voters.clear();
			currentVote = null;
		}
		if(task != null){
			task.cancel();
			task = null;
		}
	}
	
}
