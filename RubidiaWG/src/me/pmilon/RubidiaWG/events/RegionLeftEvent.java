package me.pmilon.RubidiaWG.events;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.pmilon.RubidiaWG.MovementWay;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

public class RegionLeftEvent extends RegionEvent
{
    public RegionLeftEvent(ProtectedRegion region, Player player, MovementWay movement, PlayerEvent parent)
    {
        super(region, player, movement, parent);
    }
}