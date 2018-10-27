package me.pmilon.RubidiaCore.abilities;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.ItemMessage;
import me.pmilon.RubidiaCore.RManager.Mastery;
import me.pmilon.RubidiaCore.RManager.RClass;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.damages.DamageManager;
import me.pmilon.RubidiaCore.damages.RDamageCause;
import me.pmilon.RubidiaCore.events.RPlayerAbilityEvent;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.Utils;

public abstract class Ability {
	
	private final String name;
	private final List<String> description;
	private final RClass rClass;
	private final Mastery mastery;
	private final int index;
	private final boolean passive;
	private final String sequence;
	private final String suppInfo;
	private final String unit;
	private final int toggleableTicks;
	public Ability(String name, List<String> description, RClass rClass, Mastery mastery, int index, boolean passive,
			String sequence, String suppInfo, String unit, int toggleableTicks){
		this.name = name;
		this.description = description;
		this.rClass = rClass;
		this.mastery = mastery;
		this.index = index;
		this.passive = passive;
		this.sequence = sequence;
		this.suppInfo = suppInfo;
		this.unit = unit;
		this.toggleableTicks = toggleableTicks;
	}
	
	public List<String> getDescription() {
		return description;
	}

	public boolean isPassive() {
		return passive;
	}

	public String getSequence() {
		return sequence;
	}

	public String getName() {
		return name;
	}

	public void execute(final RPlayer rp){
		final Player player = rp.getPlayer();
		double vigorCost = this.getVigorCost(rp);
		if(rp.hasVigor(vigorCost)){
			RPlayerAbilityEvent event = new RPlayerAbilityEvent(rp, this);
			Bukkit.getPluginManager().callEvent(event);
			if(!event.isCancelled()){
				ItemMessage.sendMessage(player, "§2§l> Compétence   §a" + this.getName() + " §8§m §r " + (this.isToggleable() ? (!rp.isActiveAbility(this.getIndex()) ? "§cdésactivée" : "§eactivée") : "§e-" + Utils.round(vigorCost, 1) + " §6§lEP"), 2);
				if(rp.isActiveAbility(this.getIndex())){
					if(this.isToggleable()) {
						BukkitTask.tasks.get(player.getMetadata("abilityTask" + this.getIndex()).get(0).asInt()).cancel();
					}
				}else{
					rp.setActiveAbility(this.getIndex(), true);
					this.run(rp);

					if(this.isToggleable()) {
						player.setMetadata("abilityTask" + this.getIndex(), new FixedMetadataValue(Core.instance, new BukkitTask(Abilities.getPlugin()){
							int step = 0;
							int step2 = 0;

							@Override
							public void run() {
								if(step >= 4){
									double vigor = getInstance().getVigorCost(rp);
									if(rp.hasVigor(vigor)){
										rp.addVigor(-vigor);
										step = 0;
									}else{
										getInstance().vigorless(player);
										this.cancel();
									}
								}
								
								if(step2 >= getInstance().getToggleableTicks()/5.){
									getInstance().onEffect(rp);
									step2 = 0;
								}
								
								Core.playAnimEffect(Particle.VILLAGER_ANGRY, player.getEyeLocation(), .25F, .25F, .25F, .5F, 5);
								step++;
								step2++;
							}

							@Override
							public void onCancel() {
								getInstance().onCancel(rp);
								rp.setActiveAbility(getInstance().getIndex(), false);
							}
							
						}.runTaskTimer(0, 5).getTaskId()));
					}
				}
			}
		}else this.vigorless(player);
		if(!rp.isActiveAbility(this.getIndex()) || this.isToggleable()){
		}
	}
	
	public void vigorless(Player player) {
		player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
		ItemMessage.sendMessage(player, "§cPas assez de vigueur !", 40);
	}
	
	public abstract void run(RPlayer rp);
	
	public abstract void animate(RPlayer rp, LivingEntity target);
	
	public void damage(RPlayer rp, List<LivingEntity> targets) {
		for(LivingEntity target : targets) {
			DamageManager.damage(target, rp.getPlayer(), this.getDamages(rp), RDamageCause.ABILITY);
			this.animate(rp, target);
		}
	}
	
	//for toggleable abilities only
	public abstract void onEffect(RPlayer rp);
	public abstract void onCancel(RPlayer rp);
	
	public void takeVigor(RPlayer rp) {
		rp.addVigor(-this.getVigorCost(rp));
	}

	public Mastery getMastery() {
		return mastery;
	}

	public String getSuppInfo() {
		return suppInfo;
	}

	public String getUnit() {
		return unit;
	}

	public int getToggleableTicks() {
		return toggleableTicks;
	}
	
	public boolean isToggleable() {
		return toggleableTicks > 0;
	}

	public double getDamages(RPlayer rp) {
		return rp.getAbilityLevel(this.getIndex())*this.getSettings().getDamagesPerLevel()+this.getSettings().getDamagesMin();
	}
	
	public double getVigorCost(RPlayer rp) {
		return rp.getAbilityLevel(this.getIndex())*this.getSettings().getVigorPerLevel()+this.getSettings().getVigorMin();
	}

	public RAbilitySettings getSettings() {
		return RAbilitySettings.getSettings(this.getRClass(), this.getIndex());
	}

	public RClass getRClass() {
		return rClass;
	}

	public int getIndex() {
		return index;
	}
	
	public Ability getInstance() {
		return this;
	}
	
}
