package me.pmilon.RubidiaCore.packets;

import org.bukkit.World;
import org.bukkit.entity.Entity;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class WrapperPlayServerRelEntityMove extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.REL_ENTITY_MOVE;

    public WrapperPlayServerRelEntityMove() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
        /*for(Object object : handle.getModifier().getValues()){
        	System.out.print(object.getClass().getName());
        }*/
    }

    public WrapperPlayServerRelEntityMove(PacketContainer packet) {
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
     * Retrieve DX.
     * @return The current DX
     */
    public double getDx() {
        return handle.getIntegers().read(1) / 8000.0D;
    }

    /**
     * Set DX.
     * @param value - new value.
     */
    public void setDx(double value) {
        handle.getIntegers().write(1, (int) (value*8000.0D));
    }

    /**
     * Retrieve DY.
     * @return The current DY
     */
    public double getDy() {
        return handle.getIntegers().read(2) / 8000.0D;
    }

    /**
     * Set DY.
     * @param value - new value.
     */
    public void setDy(double value) {
        handle.getIntegers().write(2,  (int) (value*8000.0D));
    }

    /**
     * Retrieve DZ.
     * @return The current DZ
     */
    public double getDz() {
        return handle.getIntegers().read(3) / 8000.0D;
    }

    /**
     * Set DZ.
     * @param value - new value.
     */
    public void setDz(double value) {
        handle.getIntegers().write(3,  (int) (value*8000.0D));
    }

    /**
     * Retrieve On Ground.
     * @return The current On Ground
     */
    public boolean getOnGround() {
        return handle.getSpecificModifier(boolean.class).read(0);
    }

    /**
     * Set On Ground.
     * @param value - new value.
     */
    public void setOnGround(boolean value) {
        handle.getSpecificModifier(boolean.class).write(0, value);
    }
}