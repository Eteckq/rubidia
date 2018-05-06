package me.pmilon.RubidiaManager.chunks;

import org.bukkit.World;

public class NChunk extends Chunk{

	private boolean active;
	public NChunk(World world, int x, int z, boolean active) {
		super(world, x, z);
		this.active = active;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}

}
