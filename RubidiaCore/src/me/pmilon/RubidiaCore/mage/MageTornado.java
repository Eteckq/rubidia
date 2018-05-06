package me.pmilon.RubidiaCore.mage;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.RandomUtils;

public class MageTornado extends Effect{
	
	public ParticleEffect tornadoParticle = ParticleEffect.FLAME;
	public Color tornadoColor = null;
	public ParticleEffect cloudParticle = ParticleEffect.CLOUD;
	public Color cloudColor = Color.WHITE;
	public float cloudSize = 2.5f;
	public double yOffset = .8;
	public float tornadoHeight = 4f;
	public float maxTornadoRadius = 1.5f;
    public double distance = .375d;
    protected int step = 0;
    
    private Player player;
	
	public MageTornado(EffectManager manager, Player player){
		super(manager);
		type = EffectType.REPEATING;
		period = 5;
		iterations = 12;
		this.player = player;
		this.setDynamicOrigin(new DynamicLocation(player.getLocation()));
	}
	
	@Override
	public void onRun(){
		this.setDynamicOrigin(new DynamicLocation(player.getLocation()));
		Location l = getLocation().add(0, yOffset, 0);
		for(int i = 0; i < (100 * cloudSize); i++){
			Vector v = RandomUtils.getRandomCircleVector().multiply(RandomUtils.random.nextDouble() * cloudSize);
			display(cloudParticle, l.add(v), cloudColor, 0, 1);
			l.subtract(v);
		}
		Location t = l.clone().add(0, .2, 0);
		double r = .45 * (maxTornadoRadius*(2.35/tornadoHeight));
		for(double y = 0; y < tornadoHeight; y+=distance){
			double fr = r * y;
			if(fr > maxTornadoRadius)
				fr = maxTornadoRadius;
			for(Vector v : createCircle(y, fr)){
				display(tornadoParticle, t.add(v), tornadoColor);
				t.subtract(v);
				step++;
			}
		}
		l.subtract(0, yOffset, 0);
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_SCREAM, 1, 2);
	}
	
	public ArrayList<Vector> createCircle(double y, double radius){
		double amount = radius * 64;
		double inc = (2*Math.PI)/amount;
		ArrayList<Vector> vecs = new ArrayList<Vector>();
		for(int i = 0; i < amount; i++){
			double angle = i * inc;
			double x = radius * Math.cos(angle);
			double z = radius * Math.sin(angle);
			Vector v = new Vector(x, y, z);
			vecs.add(v);
		}
		return vecs;
	}

}