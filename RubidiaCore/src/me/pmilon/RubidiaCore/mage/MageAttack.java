package me.pmilon.RubidiaCore.mage;

import java.util.List;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.damages.DamageManager;
import me.pmilon.RubidiaCore.damages.RDamageCause;
import me.pmilon.RubidiaCore.tasks.BukkitTask;
import me.pmilon.RubidiaCore.utils.Locations;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MageAttack extends BukkitTask {
    public int particles = 50;
    
    private Player player;
    private ItemStack item;

    protected boolean critical;
    
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
		this.setDamager(player);
    }
    
    public void setDamager(Player p){
		player = p;
    }
    
	@Override
	public void run() {
        Vector link = target.toVector().subtract(this.origin.toVector());
        float length = (float) link.length();
        link.normalize();

        float ratio = length / particles;
        Vector v = link.multiply(ratio);
        Location loc = this.origin.clone().subtract(v);
        for (int i = 0; i < particles; i++) {
            boolean damaged = false;
            loc.add(v);
            loc.getWorld().spawnParticle(Particle.END_ROD, loc, 1);
            loc.getWorld().spawnParticle(Particle.SNOWBALL, loc, 1);
            if(critical)loc.getWorld().spawnParticle(Particle.CRIT_MAGIC, loc, 1);
            if(loc.getBlock().getType().isSolid())break;
            List<Entity> near = Locations.getNearbyEntities(loc, 2);
            for(LivingEntity en : Core.toDamageableLivingEntityList(player, near, RDamageCause.MAGIC)){
            	if(en.getLocation().distanceSquared(loc) <= 1 || en.getLocation().add(0,1,0).distanceSquared(loc) <= 1){
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
}

