package me.pmilon.RubidiaCore.ui;

import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.PacketPlayOutBlockAction;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import me.pmilon.RubidiaCore.ui.abstracts.UIHandler;

public class EnderChest extends UIHandler{

	private Block block;
	public EnderChest(Player p, Block block) {
		super(p);
		this.menu = Bukkit.createInventory(this.getHolder(), 27, StringUtils.abbreviate(rp.translateString("Ender Chest", "Coffre du Néant") + " | " + rp.getName(),32));
		this.block = block;
	}

	@Override
	public String getType() {
		return "ENDER_CHEST";
	}

	@Override
	protected boolean openWindow() {
		this.changeChestState(block.getLocation(), true);
		for(int i : rp.getLoadedSPlayer().getEnderchest().keySet()){
			this.getMenu().setItem(i, rp.getLoadedSPlayer().getEnderchest().get(i));
		}
		return this.getHolder().openInventory(this.getMenu()) != null;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent e, Player p) {
	}

	@Override
	public void onGeneralClick(InventoryClickEvent e, Player p) {
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent e, Player p) {
		if(block.getType().toString().contains("CHEST"))this.changeChestState(block.getLocation(), false);
		for(int i = 0;i < this.getMenu().getSize();i++){
			rp.getLoadedSPlayer().getEnderchest().put(i, this.getMenu().getItem(i));
		}
	}

	@Override
	protected void closeUI() {
		this.getHolder().closeInventory();
	}

	@SuppressWarnings("deprecation")
	public void changeChestState(Location loc, boolean open) {
	    byte dataByte = (open) ? (byte) 1 : 0;
	    for (Player player : Bukkit.getOnlinePlayers()) {
	        if(open)player.playSound(loc, Sound.BLOCK_CHEST_OPEN, 1, 1);
	        else player.playSound(loc, Sound.BLOCK_CHEST_CLOSE, 1, 1);
	        BlockPosition position = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	        PacketPlayOutBlockAction blockActionPacket = new PacketPlayOutBlockAction(position, net.minecraft.server.v1_12_R1.Block.getById(loc.getBlock().getTypeId()), (byte) 1, dataByte);
	        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(blockActionPacket);
	    }
	}
	
}
