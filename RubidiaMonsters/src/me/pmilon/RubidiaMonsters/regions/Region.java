package me.pmilon.RubidiaMonsters.regions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.Locations;
import me.pmilon.RubidiaGuilds.claims.Claim;
import me.pmilon.RubidiaMonsters.RubidiaMonstersPlugin;
import me.pmilon.RubidiaMonsters.dungeons.Dungeon;
import me.pmilon.RubidiaMonsters.dungeons.Dungeons;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.slikey.effectlib.util.ParticleEffect;

public class Region {
	
	public enum RegionType {
		
		DEFAULT(DyeColor.GRAY),
		HILLMINSTER(DyeColor.LIME),
		NEERSTONE(DyeColor.WHITE),
		JANGBURY(DyeColor.GREEN),
		MUSHBLOOF(DyeColor.PURPLE),
		ASTONFIELD(DyeColor.ORANGE);
		
		private final DyeColor color;
		private RegionType(DyeColor color){
			this.color = color;
		}
		
		public DyeColor getDyeColor() {
			return color;
		}
		
	}
	
	private String UUID;
	private Location center;
	private double xRange;
	private double yRange;
	private double zRange;
	private List<Monster> monsters;
	private boolean square;
	private int minLevel;
	private int maxLevel;
	private double yShift;
	private boolean fadingLevel;
	private int maxMonstersAmount;
	private double rageProbability;
	private String dungeonUUID;
	private RegionType type;
	
	public List<Monster> entities = new ArrayList<Monster>();
	
	public HashMap<Player, BukkitTask> watchersTasks = new HashMap<Player, BukkitTask>();
	public Region(String UUID, Location center, double xRange, double yRange, double zRange, List<Monster> monsters, boolean square, int minLevel, int maxLevel, double yShift, boolean fadingLevel, int maxMonstersAmount, double rageProbability, String dungeonUUID, RegionType type){
		this.UUID = UUID;
		this.center = center;
		this.xRange = xRange;
		this.yRange = yRange;
		this.zRange = zRange;
		this.monsters = monsters;
		this.square = square;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
		this.yShift = yShift;
		this.fadingLevel = fadingLevel;
		this.maxMonstersAmount = maxMonstersAmount;
		this.rageProbability = rageProbability;
		this.dungeonUUID = dungeonUUID;
		this.type = type;
	}
	
	public Location getCenter() {
		return center;
	}
	
	public void setCenter(Location center) {
		this.center = center;
	}

	public double getXRange() {
		return xRange;
	}

	public void setXRange(double xRange) {
		this.xRange = xRange;
	}

	public double getZRange() {
		return zRange;
	}

	public void setZRange(double zRange) {
		this.zRange = zRange;
	}

	public List<Monster> getMonsters() {
		return monsters;
	}

	public void setMonsters(List<Monster> monsters) {
		this.monsters = monsters;
	}
	
	public boolean isIn(Location location){
		if(location.getWorld().equals(this.getCenter().getWorld())){
			Location relative = location.clone().subtract(this.getCenter());
			double rX = this.getXRange()/2;
			double rY = this.getYRange()/2;
			double rZ = this.getZRange()/2;
			if(this.isSquare())return Math.abs(relative.getX()) <= rX && Math.abs(relative.getY()) <= rY && Math.abs(relative.getZ()) <= rZ;
			else return (Math.pow(relative.getX(), 2)/Math.pow(rX, 2)) + (Math.pow(relative.getZ(), 2)/Math.pow(rZ, 2)) <= 1 && Math.abs(relative.getY()) <= rY;
		}
		return false;
	}
	
	public int getBaseLevel(Location location){
		double proportion = this.isFadingLevel() ? location.distance(this.getCenter())/this.getDistanceCenterToEdgePassingBy(location) : 1;
		int level = (int) (Math.round((this.getMaxLevel()-this.getMinLevel())+this.getMinLevel())*proportion+Math.abs(this.getCenter().getY()-location.getY())*this.getYShift());
		return level >= this.getMinLevel() ? level : this.getMinLevel();
	}
	
	public double getDistanceCenterToEdgePassingBy(Location location){
		Location relative = location.clone().subtract(this.getCenter());
		if(this.isSquare())return location.distance(this.getCenter())*this.getXRange()/(2*relative.getX());
		else{
			double teta;
			if(Math.max(this.getXRange(), this.getZRange()) == this.getXRange()){
				teta = relative.toVector().angle(new Vector(1,0,0));
				return Math.sqrt(1/((Math.cos(teta)*Math.cos(teta)/(this.getXRange()*this.getXRange()/4))+(Math.sin(teta)*Math.sin(teta)/(this.getZRange()*this.getZRange()/4))));
			}else{
				teta = relative.toVector().angle(new Vector(0,0,1));
				return Math.sqrt(1/((Math.cos(teta)*Math.cos(teta)/(this.getZRange()*this.getZRange()/4))+(Math.sin(teta)*Math.sin(teta)/(this.getXRange()*this.getXRange()/4))));
			}
		}
	}
	
	public Location getRandomSpawnLocation(Monster monster){
		double x = RubidiaMonstersPlugin.random.nextDouble()*(this.getXRange()/2)*(RubidiaMonstersPlugin.random.nextBoolean() ? -1 : 1);
		double y = RubidiaMonstersPlugin.random.nextDouble()*(this.getYRange()/2);
		double yMin = this.getCenter().getY()-(this.getYRange()/2);
		double z = RubidiaMonstersPlugin.random.nextDouble()*(this.getZRange()/2)*(RubidiaMonstersPlugin.random.nextBoolean() ? -1 : 1);
		Location location = Locations.getCenter(this.getCenter().clone().add(x, y, z));
		if(Claim.get(location) != null)return this.getRandomSpawnLocation(monster);
		location.getChunk().load();
		if(!this.isSquare()){
			while(!this.isIn(location)){
				location.setZ(this.getCenter().getZ() + RubidiaMonstersPlugin.random.nextDouble()*(this.getZRange()/2)*(RubidiaMonstersPlugin.random.nextBoolean() ? -1 : 1));
			}
		}
		if(monster.getType().equals(EntityType.SQUID) || monster.getType().equals(EntityType.GUARDIAN)){
			while(!location.getBlock().getType().toString().contains("WATER")
					&& location.getY() >= yMin){
				location.setY(location.getY()-1);
			}
		}else{
			while((!location.getBlock().getType().isTransparent()
					|| !location.clone().add(0,1,0).getBlock().getType().isTransparent()
					|| !location.clone().subtract(0,1,0).getBlock().getType().isSolid()
					|| location.clone().subtract(0,1,0).getBlock().getType().toString().contains("LEAVES")
					|| location.clone().subtract(0,1,0).getBlock().getType().toString().contains("LOG"))
					&& location.getY() >= yMin){
				location.setY(location.getY()-1);
			}
		}
		if(location.getY() < yMin)return this.getRandomSpawnLocation(monster);
		if(location.getBlock().getType().toString().contains("SNOW"))return location.add(0,1,0);
		return location;
	}
	
	public void showPlayer(final Player player){
		this.stopShowingPlayer(player);
		player.sendMessage("�eNo showing this region");
		this.watchersTasks.put(player, new BukkitTask(RubidiaMonstersPlugin.getInstance()){
			@Override
			public void run(){
				double y = player.getLocation().getY()-1.4;
				if(getSize() <= 512 && player.isOnline()){
					if(isSquare()){
						for(double x = -getXRange()/2;x < getXRange()/2;x+=.5){
							ParticleEffect.REDSTONE.display(0, 0, 0, 0, 1, new Location(player.getWorld(), getCenter().getX()+x, y, getCenter().getZ()+getZRange()/2), player);
							ParticleEffect.REDSTONE.display(0, 0, 0, 0, 1, new Location(player.getWorld(), getCenter().getX()+x, y, getCenter().getZ()-getZRange()/2), player);
						}
						for(double z = -getZRange()/2;z < getZRange()/2;z+=.5){
							ParticleEffect.REDSTONE.display(0, 0, 0, 0, 1, new Location(player.getWorld(), getCenter().getX()+getXRange()/2, y, getCenter().getZ()+z), player);
							ParticleEffect.REDSTONE.display(0, 0, 0, 0, 1, new Location(player.getWorld(), getCenter().getX()-getXRange()/2, y, getCenter().getZ()+z), player);
						}
					}else{
						for(double teta = 0;teta < 360;teta+=124/getSize()){
							double rad = Math.toRadians(teta);
							double x = getCenter().getX() + (getXRange()/2)*Math.cos(rad);
							double z = getCenter().getZ() + (getZRange()/2)*Math.sin(rad);
							ParticleEffect.REDSTONE.display(0, 0, 0, 0, 1, new Location(player.getWorld(), x, y, z), player);
						}
					}
				}else{
					this.cancel();
					player.sendMessage("�eSize is too big!");
				}
			}

			@Override
			public void onCancel() {
				player.sendMessage("�eNo longer showing this region");
			}
		}.runTaskTimer(0, 20));
	}
	
	public void stopShowingPlayer(Player player){
		if(this.watchersTasks.containsKey(player)){
			this.watchersTasks.get(player).cancel();
			this.watchersTasks.remove(player);
		}
	}

	public boolean isSquare() {
		return square;
	}

	public void setSquare(boolean square) {
		this.square = square;
	}

	public boolean isFadingLevel() {
		return fadingLevel;
	}

	public void setFadingLevel(boolean fadingLevel) {
		this.fadingLevel = fadingLevel;
	}

	public int getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}
	
	public double getSize(){
		if(this.isSquare())return this.getXRange()*this.getZRange();
		else return Math.PI*(this.getXRange()/2)*(this.getZRange()/2);
	}

	public double getYRange() {
		return yRange;
	}

	public void setYRange(double yRange) {
		this.yRange = yRange;
	}

	public double getYShift() {
		return yShift;
	}

	public void setYShift(double yShift) {
		this.yShift = yShift;
	}

	public int getMaxMonstersAmount() {
		return maxMonstersAmount;
	}

	public void setMaxMonstersAmount(int maxMonstersAmount) {
		this.maxMonstersAmount = maxMonstersAmount;
	}

	public double getRageProbability() {
		return rageProbability;
	}

	public void setRageProbability(double rageProbability) {
		this.rageProbability = rageProbability;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public double getMediumLevel(){
		return (this.maxLevel+this.minLevel)/2.0;
	}
	
	public boolean isDungeonRoom(){
		return dungeonUUID != null && !dungeonUUID.isEmpty();
	}
	
	public Dungeon getDungeon(){
		return Dungeons.get(this.dungeonUUID);
	}

	public String getDungeonUUID() {
		return dungeonUUID;
	}

	public void setDungeonUUID(String dungeonUUID) {
		this.dungeonUUID = dungeonUUID;
	}
	
	public boolean hasSpawnLocation(){
		for(Monster monster : this.getMonsters()){
			for(double x = -this.getXRange()/2;x < this.getXRange()/2;x++){
				for(double z = -this.getZRange()/2;z < this.getZRange()/2;z++){
					for(double y = -this.getYRange()/2;y < this.getYRange()/2;y++){
						Location location = this.getCenter().clone().add(x, y, z);
						if(Claim.get(location) != null)continue;
						if(monster != null){
							if(monster.getType().equals(EntityType.SQUID) || monster.getType().equals(EntityType.GUARDIAN)){
								if(!location.getBlock().getType().toString().contains("WATER")){
									continue;
								}
							}else{
								if(!location.getBlock().getType().isTransparent()
										|| !location.clone().add(0,1,0).getBlock().getType().isTransparent()
										|| !location.clone().subtract(0,1,0).getBlock().getType().isSolid()
										|| location.clone().subtract(0,1,0).getBlock().getType().toString().contains("LEAVES")
										|| location.clone().subtract(0,1,0).getBlock().getType().toString().contains("LOG")){
									continue;
								}
							}
						}
						
						if(!this.isSquare()){
							Location relative = location.clone().subtract(this.getCenter());
							if((Math.pow(relative.getX(), 2)/Math.pow(this.getXRange()/2, 2)) + (Math.pow(relative.getZ(), 2)/Math.pow(this.getZRange()/2, 2)) <= 1)return true;
						}else return true;
					}
				}
			}
		}
		return true;
	}

	public RegionType getType() {
		return type;
	}

	public void setType(RegionType type) {
		this.type = type;
	}
}
