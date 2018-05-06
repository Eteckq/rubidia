package me.pmilon.RubidiaCore.handlers;

import java.util.Random;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.events.RubidiaEntityDamageEvent;
import me.pmilon.RubidiaCore.packets.WrapperPlayServerEntityDestroy;
import me.pmilon.RubidiaCore.packets.WrapperPlayServerRelEntityMove;
import me.pmilon.RubidiaCore.packets.WrapperPlayServerSpawnEntityLiving;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.Utils;
import net.minecraft.server.v1_12_R1.EntityArmorStand;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.ParticleEffect.BlockData;

public class GamePlayEffectsHandler implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamage(final RubidiaEntityDamageEvent e){
		if(!e.isCancelled()){
			final Entity damaged = e.getEntity();
			for(final Player player : Core.toPlayerList(damaged.getNearbyEntities(16, 16, 16))){
				RPlayer rp = RPlayer.get(player);
				if(rp.getCombatLevel() > 1){
					Location location = damaged.getLocation().add(new Vector(Utils.random.nextDouble(),-.5,Utils.random.nextDouble()));
					EntityArmorStand stand1 = new EntityArmorStand(((CraftWorld)player.getWorld()).getHandle());
					stand1.setLocation(0, 0, 0, 0, 0);
					stand1.setCustomName("§c-" + Math.round(e.getDamages()));
					stand1.setCustomNameVisible(true);
					stand1.setSmall(true);
					stand1.setInvisible(true);
					stand1.spawnIn(stand1.getWorld());
					WrapperPlayServerSpawnEntityLiving packet = new WrapperPlayServerSpawnEntityLiving(stand1.getBukkitEntity());
					packet.setX(location.getX());
					packet.setY(location.getY());
					packet.setZ(location.getZ());
					packet.sendPacket(player);
					
					final WrapperPlayServerRelEntityMove packet3 = new WrapperPlayServerRelEntityMove();
					packet3.setEntityID(packet.getEntityID());
					packet3.setDx(0);
					packet3.setDy(.01);
					packet3.setDz(0);
					packet3.setOnGround(false);
					
					final WrapperPlayServerEntityDestroy packet4 = new WrapperPlayServerEntityDestroy();
					packet4.setEntities(new int[]{packet.getEntityID()});
					new BukkitTask(Core.instance){

						@Override
						public void run() {
							packet3.sendPacket(player);
						}

						@Override
						public void onCancel() {
							packet4.sendPacket(player);
						}
						
					}.runTaskTimerCancelling(0, 1, 30);
					if(rp.getCombatLevel() > 2){
						new BukkitTask(Core.instance){
							public void run(){
								Core.playAnimEffect(ParticleEffect.BLOCK_CRACK, damaged.getLocation().add(0,.6,0), .3F, .5F, .3F, 1, 40, new BlockData(Material.REDSTONE_BLOCK, (byte)0));
								Random r = new Random();
								for(int i = 0;i < r.nextInt(4)+3;i++){
									ItemStack stack = new ItemStack(Material.REDSTONE);
									ItemMeta meta = stack.getItemMeta();
									meta.setDisplayName(String.valueOf(r.nextInt()));
									stack.setItemMeta(meta);
									final Item item = damaged.getWorld().dropItem(damaged.getLocation().add(0,.6,0), stack);
									item.setPickupDelay(Integer.MAX_VALUE);
									new BukkitTask(Core.instance){
										public void run(){
											item.remove();
										}

										@Override
										public void onCancel() {
										}
									}.runTaskLater(11);
								}
							}

							@Override
							public void onCancel() {
							}
							
						}.runTaskLater(2);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onHopperPickUp(InventoryPickupItemEvent event){
		ItemStack item = event.getItem().getItemStack();
		if(item.hasItemMeta()){
			ItemMeta meta = item.getItemMeta();
			if(meta.hasDisplayName()){
				if(Utils.isInteger(meta.getDisplayName())){
					event.setCancelled(true);
				}
			}
		}
	}

}
