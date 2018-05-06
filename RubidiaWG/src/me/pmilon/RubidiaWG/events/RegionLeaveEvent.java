package me.pmilon.RubidiaWG.events;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.pmilon.RubidiaWG.MovementWay;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;

public class RegionLeaveEvent extends RegionEvent  implements Cancellable {
    private boolean cancelled, cancellable;
    public RegionLeaveEvent(ProtectedRegion region, Player player, MovementWay movement, PlayerEvent parent)
    {
        super(region, player, movement, parent);
        cancelled = false;
        cancellable = true;
        
        if (movement == MovementWay.SPAWN
            || movement == MovementWay.DISCONNECT)
        {
            cancellable = false;
        }
    }
    
    @Override
    public void setCancelled(boolean cancelled)
    {
        if (!this.cancellable)
        {
            return;
        }
        
        this.cancelled = cancelled;
    }
    
    @Override
    public boolean isCancelled()
    {
        return this.cancelled;
    }
    
    
    public boolean isCancellable()
    {
        return this.cancellable;
    }
    
    protected void setCancellable(boolean cancellable)
    {
        this.cancellable = cancellable;

        if (!this.cancellable)
        {
            this.cancelled = false;
        }
    }
}