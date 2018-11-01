package me.pmilon.RubidiaCore.mage;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.damages.DamageManager;
import me.pmilon.RubidiaCore.damages.RDamageCause;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.Locations;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MageAttack extends BukkitTask {
    public int particles = 50;
    
    private Player player;
    private ItemStack item;

    protected boolean critical;
    
    private final World world;
    private final Location origin;
    private final Location target;
    
    public MageAttack(Player player, ItemStack item, boolean critical) {
        super(Core.instance);
        this.player = player;
        this.item = item;
        this.critical = critical;

		Location location = player.getEyeLocation().clone().add(player.getEyeLocation().getDirection().normalize());
		this.origin = location;
		this.target = location.add(player.getEyeLocation().getDirection().normalize().multiply(9));
		this.world = location.getWorld();
		this.setDamager(player);
    }
    
    public void setDamager(Player p){
		player = p;
    }
    
	@Override
	public void run() {
        Vector link = target.toVector().subtract(this.origin.toVector());
        Vector step = link.normalize().multiply(link.length() / particles);
        Location location = this.origin.clone();
        for (int i = 0; i < particles; i++) {
            location.add(step);
            this.getWorld().spawnParticle(Particle.END_ROD, location, 1);
            this.getWorld().spawnParticle(Particle.SNOWBALL, location, 1);
            if(critical) {
            	this.getWorld().spawnParticle(Particle.CRIT_MAGIC, location, 1);
            }
            
            if(location.getBlock().getType().isSolid()) {
            	break;
            }

            boolean damaged = false;
            for(LivingEntity en : Core.toDamageableLivingEntityList(player, Locations.getNearbyEntities(location, 1), RDamageCause.MAGIC)) {
            	if(en.getLocation().distanceSquared(location) <= 1
            			|| en.getLocation().add(0,1,0).distanceSquared(location) <= 1) {
                	double damages = DamageManager.getDamages(player, en, item, RDamageCause.MAGIC, critical, false);
            		DamageManager.damage(en, player, damages, RDamageCause.MAGIC);
            		for(Enchantment enchant : item.getEnchantments().keySet()){
            			int level = item.getEnchantmentLevel(enchant);
            			if(enchant.equals(Enchantment.FIRE_ASPECT)){
            				en.setFireTicks(40*level);
            			}else if(enchant.equals(Enchantment.KNOCKBACK)){
            				en.setVelocity(en.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(level*.75));
            			}
            		}
            		damaged = true;
            	}
            	
                if(damaged)break;
            }
            
            if(damaged)break;
        }
	}

	@Override
	public void onCancel() {
	}

	public World getWorld() {
		return world;
	}
}

