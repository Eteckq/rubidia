package me.pmilon.RubidiaCore.packets;

import org.bukkit.World;
import org.bukkit.entity.Entity;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class WrapperPlayServerSpawnEntityWeather extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.SPAWN_ENTITY_WEATHER;

    public WrapperPlayServerSpawnEntityWeather() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerSpawnEntityWeather(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieve Entity ID.
     * <p>
     * Notes: entity's ID
     * @return The current Entity ID
     */
    public int getEntityID() {
        return handle.getIntegers().read(0);
    }

    /**
     * Set Entity ID.
     * @param value - new value.
     */
    public void setEntityID(int value) {
        handle.getIntegers().write(0, value);
    }

    /**
     * Retrieve the entity of the painting that will be spawned.
     * @param world - the current world of the entity.
     * @return The spawned entity.
     */
    public Entity getEntity(World world) {
        return handle.getEntityModifier(world).read(0);
    }

    /**
     * Retrieve the entity of the painting that will be spawned.
     * @param event - the packet event.
     * @return The spawned entity.
     */
    public Entity getEntity(PacketEvent event) {
        return getEntity(event.getPlayer().getWorld());
    }

    /**
     * Retrieve Type.
     * <p>
     * Notes: the global entity type, currently always 1 for thunderbolt.
     * @return The current Type
     */
    public int getType() {
        return handle.getIntegers().read(1);
    }

    /**
     * Set Type.
     * @param value - new value.
     */
    public void setType(int value) {
        handle.getIntegers().write(1, value);
    }

    /**
     * Retrieve X.
     * <p>
     * Notes: thunderbolt X a fixed-point number
     * @return The current X
     */
    public double getX() {
        return handle.getDoubles().read(0);
    }

    /**
     * Set X.
     * @param value - new value.
     */
    public void setX(double value) {
        handle.getDoubles().write(0, value);
    }

    /**
     * Retrieve Y.
     * <p>
     * Notes: thunderbolt Y a fixed-point number
     * @return The current Y
     */
    public double getY() {
        return handle.getDoubles().read(1);
    }

    /**
     * Set Y.
     * @param value - new value.
     */
    public void setY(double value) {
        handle.getDoubles().write(1, value);
    }

    /**
     * Retrieve Z.
     * <p>
     * Notes: thunderbolt Z a fixed-point number
     * @return The current Z
     */
    public double getZ() {
        return handle.getDoubles().read(2);
    }

    /**
     * Set Z.
     * @param value - new value.
     */
    public void setZ(double value) {
        handle.getDoubles().write(2, value);
    }
}