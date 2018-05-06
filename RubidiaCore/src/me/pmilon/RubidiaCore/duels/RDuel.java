package me.pmilon.RubidiaCore.duels;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RChat.RChatUtils;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.Settings;
import me.pmilon.RubidiaCore.utils.Utils;

public class RDuel {

	public enum RDuelOutcome {
		
		CHALLENGER_WINNER, TIE, CHALLENGER_LOSER, CHALLENGER_FORFAIT, CHALLENGED_FORFAIT;
		
	}
	
	private final RPlayer challenger;
	private final RPlayer challenged;
	private final boolean competitive;
	private Location center;
	private double radius;
	private double height;
	private BukkitTask requestTimeout;
	private BukkitTask duelTimeout;
	private BukkitTask duelVisualizer;
	private boolean started = false;
	
	private double challengerHealth;
	private double challengedHealth;
	private double challengerMana;
	private double challengedMana;
	private int challengerFood;
	private int challengedFood;
	public RDuel(RPlayer challenger, RPlayer challenged, boolean competitive){
		this.challenger = challenger;
		this.challenged = challenged;
		this.competitive = competitive;
	}
	
	public RPlayer getChallenger() {
		return challenger;
	}

	public RPlayer getChallenged() {
		return challenged;
	}

	public Location getCenter() {
		return center;
	}
	
	public void setCenter(Location center) {
		this.center = center;
	}

	public double getRadius() {
		return radius;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	public void request(){
		this.getChallenger().getDuels().add(this);
		final RDuel instance = this;
		this.setRequestTimeout(new BukkitTask(Core.instance){

			@Override
			public void run() {
				getChallenger().getDuels().remove(instance);
				getChallenger().sendMessage("§4" + getChallenged().getName() + "§c has not answered your duel request in time...", "§4" + getChallenged().getName() + "§c n'a pas répondu à votre invitation en duel à temps...");
				getChallenged().sendMessage("§cYou have not answered §4" + getChallenger().getName() + "§c's duel request in time...", "§cVous n'avez pas répondu à l'invitation en duel de §4" + getChallenger().getName() + "§c à temps...");
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskLater(Settings.DUEL_REQUEST_TIME*20));
		this.getChallenged().sendTitle(this.getChallenged().translateString("§6§lNew" + (this.isCompetitive() ? " competitive" : "") + " duel", "§6§lNouveau duel" + (this.isCompetitive() ? " compétitif" : "")), this.getChallenged().translateString("§eYou have been challenged by ", "§eVous avez été défié par ") + this.getChallenger().getName(), 10, 160, 10);
		this.getChallenger().sendMessage("§eYou just challenged §6" + this.getChallenged().getName() + "§e in a " + (this.isCompetitive() ? "competitive " : "") + "duel.", "§eVous venez de défier §6" + this.getChallenged().getName() + "§e en duel" + (this.isCompetitive() ? " compétitif" : "") + ".");
	}
	
	public void cancelRequest(){
		if(this.getRequestTimeout() != null){
			this.getRequestTimeout().cancel();
			this.setRequestTimeout(null);
		}
		this.getChallenger().getDuels().remove(this);
		this.getChallenger().sendMessage("§cYour duel request to §4" + getChallenged().getName() + " §chas been cancelled.", "§cVotre invitation en duel contre §4" + getChallenged().getName() + "§c a été annulée.");
		this.getChallenged().sendMessage("§4" + getChallenger().getName() + "§c cancelled his duel request.", "§4" + getChallenger().getName() + "§c a annulé sont invitation en duel.");
	}
	
	public void start(){
		if(this.getRequestTimeout() != null){
			this.getRequestTimeout().cancel();
			this.setRequestTimeout(null);
		}
		this.getChallenger().getDuels().remove(this);
		this.getChallenger().setCurrentDuel(this);
		this.getChallenged().setCurrentDuel(this);
		if(this.getChallenged().isOnline() && this.getChallenger().isOnline()){
			final Player p1 = this.getChallenger().getPlayer();
			final Player p2 = this.getChallenged().getPlayer();
			p1.setNoDamageTicks(110);
			p2.setNoDamageTicks(110);
			this.challengerHealth = p1.getHealth();
			this.challengedHealth = p2.getHealth();
			this.challengerMana = this.getChallenger().getNrj();
			this.challengedMana = this.getChallenged().getNrj();
			this.challengerFood = p1.getFoodLevel();
			this.challengedFood = p2.getFoodLevel();
			p1.setHealth(p1.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()-.01);
			p2.setHealth(p2.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()-.01);
			this.getChallenger().setNrj(this.getChallenger().getMaxNrj());
			this.getChallenged().setNrj(this.getChallenged().getMaxNrj());
			p1.setFoodLevel(20);
			p2.setFoodLevel(20);
			this.count();
		}
	}
	
	public void count(){
		final Player p1 = this.getChallenger().getPlayer();
		final Player p2 = this.getChallenged().getPlayer();
		this.getChallenger().sendTitle(this.getChallenger().translateString("§eDuel will begin...", "§eLe duel va débuter..."), "§6" + this.getChallenger().getName() + " §eVS §6" + this.getChallenged().getName(), 0, 30, 5);
		this.getChallenged().sendTitle(this.getChallenged().translateString("§eDuel will begin...", "§eLe duel va débuter..."), "§6" + this.getChallenged().getName() + " §eVS §6" + this.getChallenger().getName(), 0, 30, 5);
		Bukkit.getScheduler().runTaskLater(Core.instance, new Runnable(){
			public void run(){
				getChallenger().sendTitle("§c3...", "", 0, 10, 5);
				getChallenged().sendTitle("§c3...", "", 0, 10, 5);
				p1.playSound(p1.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
				p2.playSound(p2.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
				Bukkit.getScheduler().runTaskLater(Core.instance, new Runnable(){
					public void run(){
						getChallenger().sendTitle("§c2...", "", 0, 10, 5);
						getChallenged().sendTitle("§c2...", "", 0, 10, 5);
						p1.playSound(p1.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
						p2.playSound(p2.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
						Bukkit.getScheduler().runTaskLater(Core.instance, new Runnable(){
							public void run(){
								getChallenger().sendTitle("§41...", "", 0, 10, 5);
								getChallenged().sendTitle("§41...", "", 0, 10, 5);
								p1.playSound(p1.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
								p2.playSound(p2.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
								Bukkit.getScheduler().runTaskLater(Core.instance, new Runnable(){
									public void run(){
										getChallenger().sendTitle("§4FIGHT!", "", 0, 10, 5);
										getChallenged().sendTitle("§4FIGHT!", "", 0, 10, 5);
										p1.playSound(p1.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 2F);
										p2.playSound(p2.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 2F);
										setStarted(true);
										Vector link = p2.getLocation().toVector().subtract(p1.getLocation().toVector()).multiply(.5);
										setCenter(p1.getLocation().add(link));
										setRadius(Math.max(link.length()+4.0,8.0));
										setHeight(Math.abs(link.getY())+40.0);
										final List<Location> locations = new ArrayList<Location>();
										for(double i = 0;i <= Math.PI/2;i += Math.PI/(getRadius()*120/8.0)){
											double x = getRadius()*Math.cos(i);
											double z = getRadius()*Math.sin(i);
											locations.add(getCenter().clone().add(x,0,z));
											locations.add(getCenter().clone().add(x,0,-z));
											locations.add(getCenter().clone().add(-x,0,-z));
											locations.add(getCenter().clone().add(-x,0,z));
										}
										setDuelVisualizer(new BukkitTask(Core.instance){

											@Override
											public void run() {
												int baseY1 = p1.getLocation().getBlockY();
												int baseY2 = p2.getLocation().getBlockY();
												for(Location location : locations){
													if(getChallenger().getEffects()){
														Location location1 = location.clone().add(0,baseY1-location.getY()+3+Utils.random.nextDouble(),0);
														if(location1.distanceSquared(p1.getLocation()) <= 81){
															Settings.DUEL_WALL_PARTICLE.display(new Vector(0,-1,0), Utils.random.nextFloat()*.2F, location1, p1);
														}
													}
													if(getChallenged().getEffects()){
														Location location2 = location.clone().add(0,baseY2-location.getY()+3+Utils.random.nextDouble(),0);
														if(location2.distanceSquared(p2.getLocation()) <= 81){
															Settings.DUEL_WALL_PARTICLE.display(new Vector(0,-1,0), Utils.random.nextFloat()*.2F, location2, p2);
														}
													}
												}
											}

											@Override
											public void onCancel() {
											}
											
										}.runTaskTimer(0, 6));
										setDuelTimeout(new BukkitTask(Core.instance){

											@Override
											public void run() {
												reward(RDuelOutcome.TIE);
											}

											@Override
											public void onCancel() {
											}
											
										}.runTaskLater(Settings.DUEL_TIMEOUT*60*20));
										
									}
								}, 20);
							}
						}, 20);
					}
				}, 20);
			}
		}, 40);
	}
	
	public void reward(RDuelOutcome outcome){
		if(this.getChallenger().isOnline()){
			final Player p1 = this.getChallenger().getPlayer();
			this.resetStats(p1);
			new BukkitTask(Core.instance){

				@Override
				public void run() {
					if(p1 != null)p1.playSound(p1.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1.5F);
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskTimerCancelling(0, 4, 12);
		}
		if(this.getChallenged().isOnline()){
			final Player p2 = this.getChallenged().getPlayer();
			this.resetStats(p2);
			new BukkitTask(Core.instance){

				@Override
				public void run() {
					if(p2 != null)p2.playSound(p2.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1.5F);
				}

				@Override
				public void onCancel() {
				}
				
			}.runTaskTimerCancelling(0, 5, 15);
		}
		this.getChallenger().setCurrentDuel(null);
		this.getChallenged().setCurrentDuel(null);
		if(this.getDuelTimeout() != null){
			this.getDuelTimeout().cancel();
			this.setDuelTimeout(null);
		}
		if(this.getDuelVisualizer() != null){
			this.getDuelVisualizer().cancel();
			this.setDuelVisualizer(null);
		}
		int old_renom1 = this.getChallenger().getRenom();
		int old_renom2 = this.getChallenged().getRenom();
		switch(outcome){
		case CHALLENGER_WINNER:
			this.getChallenger().sendTitle(this.getChallenger().translateString("§2§lVictory!", "§2§lVictoire !"), this.getChallenger().translateString("§aYou win the duel", "§aVous remportez le duel"), 10, 160, 10);
			this.getChallenged().sendTitle(this.getChallenged().translateString("§4§lDefeat...", "§4§lDéfaite..."), this.getChallenged().translateString("§cYou lose the duel", "§cVous perdez le duel"), 10, 160, 10);
			if(this.isCompetitive()){
				int bonus = 0;
				if(this.getChallenged().getRenom() > this.getChallenger().getRenom()){
					bonus = (int) Math.round(Settings.COMPETITIVE_DUEL_FACTOR_BONUS*this.getChallenged().getRenom());
				}
				int winpool = (int) Math.round((1-Math.pow(((double)Math.abs(this.getChallenger().getRLevel()-this.getChallenged().getRLevel()))/Settings.COMPETITIVE_DUEL_LEVEL_SHIFT_MAX,Settings.COMPETITIVE_DUEL_FACTOR_POWER))*Settings.COMPETITIVE_DUEL_WIN_RENOM_MAX);
				this.getChallenger().setRenom(this.getChallenger().getRenom()+winpool+bonus+Settings.COMPETITIVE_DUEL_WIN_RENOM_MIN);
				this.getChallenged().setRenom(this.getChallenged().getRenom()-winpool);
				this.getChallenger().sendMessage("§eYour victory made you win §6" + (this.getChallenger().getRenom()-old_renom1) + " §epoints of renown.", "§eVotre victoire vous a fait gagner §6" + (this.getChallenger().getRenom()-old_renom1) + " §epoints de renom.");
				this.getChallenged().sendMessage("§eYour defeat made you lose §6" + (old_renom2-this.getChallenged().getRenom()) + " §epoints of renown.", "§eVotre défaite vous a fait perdre §6" + (old_renom2-this.getChallenged().getRenom()) + " §epoints de renom.");
				RChatUtils.broadcastInfo("§6§l" + this.getChallenger().getName() + " §e⚔§6§l " + this.getChallenged().getName() + "  §8Victoire de " + this.getChallenger().getName() + "  §e§o(+" + (this.getChallenger().getRenom()-old_renom1) + ") (" + (this.getChallenged().getRenom()-old_renom2) + ")");
			}else{
				RChatUtils.broadcastInfo("§6§l" + this.getChallenger().getName() + " §e⚔§6§l " + this.getChallenged().getName() + "  §8Victoire de " + this.getChallenger().getName());
			}
			return;
		case TIE:
			RChatUtils.broadcastInfo("§6§l" + this.getChallenger().getName() + " §e⚔§6§l " + this.getChallenged().getName() + "  §8Égalité !");
			this.getChallenger().sendTitle(this.getChallenger().translateString("§6§lIt's a tie!", "§6§lÉgalité !"), this.getChallenger().translateString("§eNo kill in 3 min...", "§eAucun meurtre en 3 minutes..."), 10, 160, 10);
			this.getChallenged().sendTitle(this.getChallenged().translateString("§6§lIt's a tie!", "§6§lÉgalité !"), this.getChallenged().translateString("§eNo kill in 3 min...", "§eAucun meurtre en 3 minutes..."), 10, 160, 10);
			if(this.isCompetitive()){
				this.getChallenger().setRenom(this.getChallenger().getRenom()+Settings.COMPETITIVE_DUEL_TIE_RENOM);
				this.getChallenged().setRenom(this.getChallenged().getRenom()+Settings.COMPETITIVE_DUEL_TIE_RENOM);
				this.getChallenger().sendMessage("§eAs you loyally fought for " + Settings.DUEL_TIMEOUT + " minutes, you both won §6" + Settings.COMPETITIVE_DUEL_FORFAIT_RENOM + " §epoints of renown.", "§eEn tant que loyaux guerriers, vous gagnez tous deux §6" + Settings.COMPETITIVE_DUEL_FORFAIT_RENOM + " §epoints de renom.");
				this.getChallenged().sendMessage("§eAs you loyally fought for " + Settings.DUEL_TIMEOUT + " minutes, you both won §6" + Settings.COMPETITIVE_DUEL_FORFAIT_RENOM + " §epoints of renown.", "§eEn tant que loyaux guerriers, vous gagnez tous deux §6" + Settings.COMPETITIVE_DUEL_FORFAIT_RENOM + " §epoints de renom.");
			}
			return;
		case CHALLENGER_LOSER:
			this.getChallenged().sendTitle(this.getChallenged().translateString("§2§lVictory!", "§2§lVictoire !"), this.getChallenged().translateString("§aYou win the duel", "§aVous remportez le duel"), 10, 160, 10);
			this.getChallenger().sendTitle(this.getChallenger().translateString("§4§lDefeat...", "§4§lDéfaite..."), this.getChallenger().translateString("§cYou lose the duel", "§cVous perdez le duel"), 10, 160, 10);
			if(this.isCompetitive()){
				int bonus = 0;
				if(this.getChallenger().getRenom() > this.getChallenged().getRenom()){
					bonus = (int) Math.round(Settings.COMPETITIVE_DUEL_FACTOR_BONUS*this.getChallenger().getRenom());
				}
				int winpool = (int) Math.round((1-Math.pow(((double)Math.abs(this.getChallenger().getRLevel()-this.getChallenged().getRLevel()))/Settings.COMPETITIVE_DUEL_LEVEL_SHIFT_MAX,Settings.COMPETITIVE_DUEL_FACTOR_POWER))*Settings.COMPETITIVE_DUEL_WIN_RENOM_MAX);
				this.getChallenged().setRenom(this.getChallenged().getRenom()+winpool+bonus+Settings.COMPETITIVE_DUEL_WIN_RENOM_MIN);
				this.getChallenger().setRenom(this.getChallenger().getRenom()-winpool);
				this.getChallenged().sendMessage("§eYour victory made you win §6" + (this.getChallenged().getRenom()-old_renom2) + " §epoints of renown.", "§eVotre victoire vous a fait gagner §6" + (this.getChallenged().getRenom()-old_renom2) + " §epoints de renom.");
				this.getChallenger().sendMessage("§eYour defeat made you lose §6" + (old_renom1-this.getChallenger().getRenom()) + " §epoints of renown.", "§eVotre défaite vous a fait perdre §6" + (old_renom1-this.getChallenger().getRenom()) + " §epoints de renom.");
				RChatUtils.broadcastInfo("§6§l" + this.getChallenger().getName() + " §e⚔§6§l " + this.getChallenged().getName() + "  §8Victoire de " + this.getChallenged().getName() + "  §e§o(" + (this.getChallenger().getRenom()-old_renom1) + ") (+" + (this.getChallenged().getRenom()-old_renom2) + ")");
			}else{
				RChatUtils.broadcastInfo("§6§l" + this.getChallenger().getName() + " §e⚔§6§l " + this.getChallenged().getName() + "  §8Victoire de " + this.getChallenged().getName());
			}
			return;
		case CHALLENGED_FORFAIT:
			this.getChallenger().sendTitle(this.getChallenger().translateString("§6§lForfait!", "§6§lForfait !"), this.getChallenger().translateString("§eWhat a coward...", "§eQuel lâche..."), 10, 160, 10);
			if(this.isCompetitive()){
				this.getChallenger().setRenom(this.getChallenger().getRenom()+Settings.COMPETITIVE_DUEL_FORFAIT_RENOM);
				this.getChallenged().setRenom(this.getChallenged().getRenom()-2*Settings.COMPETITIVE_DUEL_FORFAIT_RENOM);
				this.getChallenger().sendMessage("§e" + this.getChallenged().getName() + "'s forfait made you win §6" + Settings.COMPETITIVE_DUEL_FORFAIT_RENOM + " §epoints of renown.", "§eLe forfait de " + this.getChallenged().getName() + " vous a fait gagner §6" + Settings.COMPETITIVE_DUEL_FORFAIT_RENOM + " §epoints de renom.");
				RChatUtils.broadcastInfo("§6§l" + this.getChallenger().getName() + " §e⚔§6§l " + this.getChallenged().getName() + "  §8Forfait de " + this.getChallenged().getName() + "  §e§o(+" + (this.getChallenger().getRenom()-old_renom1) + ") (" + (this.getChallenged().getRenom()-old_renom2) + ")");
			}else{
				RChatUtils.broadcastInfo("§6§l" + this.getChallenger().getName() + " §e⚔§6§l " + this.getChallenged().getName() + "  §8Forfait de " + this.getChallenged().getName());
			}
			return;
		case CHALLENGER_FORFAIT:
			this.getChallenged().sendTitle(this.getChallenged().translateString("§6§lForfait!", "§6§lForfait !"), this.getChallenged().translateString("§eWhat a coward...", "§eQuel lâche..."), 10, 160, 10);
			if(this.isCompetitive()){
				this.getChallenged().setRenom(this.getChallenged().getRenom()+Settings.COMPETITIVE_DUEL_FORFAIT_RENOM);
				this.getChallenger().setRenom(this.getChallenger().getRenom()-2*Settings.COMPETITIVE_DUEL_FORFAIT_RENOM);
				this.getChallenged().sendMessage("§e" + this.getChallenger().getName() + "'s forfait made you win §6" + Settings.COMPETITIVE_DUEL_FORFAIT_RENOM + " §epoints of renown.", "§eLe forfait de " + this.getChallenger().getName() + " vous a fait gagner §6" + Settings.COMPETITIVE_DUEL_FORFAIT_RENOM + " §epoints de renom.");
				RChatUtils.broadcastInfo("§6§l" + this.getChallenger().getName() + " §e⚔§6§l " + this.getChallenged().getName() + "  §8Forfait de " + this.getChallenger().getName() + "  §e§o(" + (this.getChallenger().getRenom()-old_renom1) + ") (+" + (this.getChallenged().getRenom()-old_renom2) + ")");
			}else{
				RChatUtils.broadcastInfo("§6§l" + this.getChallenger().getName() + " §e⚔§6§l " + this.getChallenged().getName() + "  §8Forfait de " + this.getChallenger().getName());
			}
			return;
		}
		if(this.isCompetitive()){
			this.getChallenger().getLastCompetitiveDuelDates().put(this.getChallenged().getUniqueId(), System.currentTimeMillis());
			this.getChallenged().getLastCompetitiveDuelDates().put(this.getChallenger().getUniqueId(), System.currentTimeMillis());
		}
	}
	
	public void resetStats(Player player){
		RPlayer rp = RPlayer.get(player);
		if(rp.equals(this.getChallenger())){
			rp.setNrj(this.challengerMana);
			player.setHealth(this.challengerHealth);
			player.setFoodLevel(this.challengerFood);
		}else if(rp.equals(this.getChallenged())){
			rp.setNrj(this.challengedMana);
			player.setHealth(this.challengedHealth);
			player.setFoodLevel(this.challengedFood);
		}
	}

	public BukkitTask getRequestTimeout() {
		return requestTimeout;
	}

	public void setRequestTimeout(BukkitTask requestTimeout) {
		this.requestTimeout = requestTimeout;
	}

	public BukkitTask getDuelTimeout() {
		return duelTimeout;
	}

	public void setDuelTimeout(BukkitTask duelTimeout) {
		this.duelTimeout = duelTimeout;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public BukkitTask getDuelVisualizer() {
		return duelVisualizer;
	}

	public void setDuelVisualizer(BukkitTask duelVisualizer) {
		this.duelVisualizer = duelVisualizer;
	}

	public boolean isCompetitive() {
		return competitive;
	}
}
