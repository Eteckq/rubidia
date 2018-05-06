package me.pmilon.RubidiaCore.tags;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import net.minecraft.server.v1_12_R1.EntityLiving;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;

public class TagStand {

	protected final Entity holder;
	protected String[] layers;
	protected boolean fix;
	protected BukkitTask[] tasks;
	//protected BukkitTask task;
	protected ArmorStand[] displays;
	public TagStand(Entity holder, String[] layers, boolean fix){
		this.holder = holder;
		this.fix = fix;
		this.tasks = new BukkitTask[layers.length];
		this.displays = new ArmorStand[layers.length];
		this.setLayers(layers, false);
	}
	
	public Entity getHolder() {
		return holder;
	}
	
	public String[] getLayers() {
		return layers;
	}
	
	public ArmorStand[] getDisplays(){
		return displays;
	}
	
	public void setLayers(String[] layers, boolean update) {
		for(int i = 0; i < layers.length / 2; i++){
		    String temp = layers[i];
		    layers[i] = layers[layers.length - i - 1];
		    layers[layers.length - i - 1] = temp;
		}
		
		this.layers = layers;
		if(update)this.update();
	}
	
	public void display(){
		TagStandManager.registerTagStand(this);
		for(int i = 0;i < layers.length;i++){
			this.register(i);
		}
		/*this.task = new BukkitTask(Core.instance){
			public void run(){
				for(ArmorStand display : displays){
					if(display.isDead() || !display.isValid()){
						update();
						break;
					}
				}
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskTimer(0,4*20);*/
	}
	
	public ArmorStand register(int index){
		final double deviation = .05+index*.3;
		final ArmorStand display = this.getHolder().getWorld().spawn(this.getHolder().getLocation().add(0,((EntityLiving) ((CraftEntity)getHolder()).getHandle()).length+deviation,0), ArmorStand.class);
		this.displays[index] = display;
		if(!layers[index].isEmpty()){
			display.setCustomName(layers[index]);
			display.setCustomNameVisible(true);
		}else{
			display.setCustomName(null);
			display.setCustomNameVisible(false);
		}
		display.setMarker(true);
		display.setNoDamageTicks(Integer.MAX_VALUE);
		display.setVisible(false);
		display.setBasePlate(false);
		display.setGravity(false);
		display.setSmall(true);
		display.setMetadata("display", new FixedMetadataValue(Core.instance, this.getHolder().getUniqueId().toString()));
		this.tasks[index] = new BukkitTask(Core.instance){
			public void run(){
				if(!getHolder().isValid() || getHolder().isDead()){
					remove(true);
				}else{
					double height = ((EntityLiving) ((CraftEntity)getHolder()).getHandle()).length+deviation;
					if(!getHolder().getLocation().getWorld().equals(display.getLocation().getWorld()) || getHolder().getLocation().add(0,height,0).distanceSquared(display.getLocation()) != 0){
						display.teleport(getHolder().getLocation().add(0,height,0));
					}
				}
			}

			@Override
			public void onCancel() {
			}
			
		}.runTaskTimer(0,0);
		return display;
	}
	
	public void update(){
		if(this.displays.length == this.layers.length){
			for(int i = 0;i < this.displays.length;i++){
				ArmorStand stand = this.displays[i];
				if(stand != null){
					if(stand.isDead() || !stand.isValid()){
						stand.remove();
						if(this.tasks[i] != null)this.tasks[i].cancel();
						this.register(i);
					}else if(stand.getCustomName() == null || !stand.getCustomName().equals(this.layers[i])){
						stand.setCustomName(this.layers[i]);
					}
				}
			}
		}else{
			this.remove(false);
			this.tasks = new BukkitTask[layers.length];
			this.displays = new ArmorStand[layers.length];
			new BukkitTask(Core.instance){
				public void run(){//to avoid asynchronous entity add!
					display();
				}

				@Override
				public void onCancel() {
				}
			}.runTaskLater(3);
		}
	}
	
	public void remove(boolean unregister){
		//task.cancel();
		for(BukkitTask task : this.tasks){
			if(task != null){
				task.cancel();
			}
		}
		for(ArmorStand stand : this.displays){
			if(stand != null){
				stand.remove();
			}
		}
		if(unregister)TagStandManager.unregisterTagStand(this);
	}

	public boolean isFix() {
		return fix;
	}

	public void setFix(boolean fix) {
		this.fix = fix;
		this.update();
	}
	
}
