package me.pmilon.RubidiaCore.mage;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.tasks.BukkitTask;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MageMeteor extends Effect {
	
    public ParticleEffect particle = ParticleEffect.EXPLOSION_HUGE;
    public int zigZags = 5;
    public int particles = 2;
    protected int step = 0;
    
    public Player player;

    public MageMeteor(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.INSTANT;
        period = 5;
        iterations = 200;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        Location target = getTarget();
        if (target == null) {
            cancel();
            return;
        }
        Vector link = target.toVector().subtract(location.toVector());
        float length = (float) link.length();
        link.normalize();

        float ratio = length / particles;
        final Vector v = link.multiply(ratio);
        final Location loc = location.clone().subtract(v);
        
        new BukkitTask(Core.instance){

			@Override
			public void run() {
                loc.add(v);
                Core.playAnimEffect(particle, loc, 0, 0, 0, 1, 1);
			}

			@Override
			public void onCancel() {
			}
        	
        }.runTaskTimerCancelling(0, 2, particles*2);
    }
    public void setPlayer(Player en){
    	if(en != null){
    		player = en;
    	}else{
    		player = null;
    	}
    }
}