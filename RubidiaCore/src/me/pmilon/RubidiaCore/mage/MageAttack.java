package me.pmilon.RubidiaCore.mage;

import java.util.List;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleEffect;
import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.damages.DamageManager;
import me.pmilon.RubidiaCore.damages.RDamageCause;
import me.pmilon.RubidiaCore.utils.Locations;

import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MageAttack extends Effect {
    public ParticleEffect particle1 = ParticleEffect.END_ROD;
    public ParticleEffect particle2 = ParticleEffect.SNOWBALL;
    public int particles = 50;
    
    private Player player;
    private ItemStack item;

    protected boolean critical;
    
    public MageAttack(Player player, ItemStack item, boolean critical) {
        super(new EffectManager(EffectLib.instance()));
        type = EffectType.INSTANT;
        period = 100;
        iterations = 1000;
        this.player = player;
        this.item = item;
        this.critical = critical;
        this.asynchronous = false;

		Location location = player.getEyeLocation().clone().add(player.getEyeLocation().getDirection().normalize());
		this.setDynamicOrigin(new DynamicLocation(location));
		this.setDynamicTarget(new DynamicLocation(location.add(player.getEyeLocation().getDirection().normalize().multiply(9))));
		this.setDamager(player);
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        Location target = getTarget();
        Vector link = target.toVector().subtract(location.toVector());
        float length = (float) link.length();
        link.normalize();

        float ratio = length / particles;
        Vector v = link.multiply(ratio);
        Location loc = location.clone().subtract(v);
        for (int i = 0; i < particles; i++) {
            boolean damaged = false;
            loc.add(v);
            display(particle1, loc);
            display(particle2, loc);
            if(critical)display(ParticleEffect.CRIT_MAGIC, loc);
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
    
    public void setDamager(Player p){
		player = p;
    }
}

