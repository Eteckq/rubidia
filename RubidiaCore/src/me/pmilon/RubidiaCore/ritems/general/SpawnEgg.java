package me.pmilon.RubidiaCore.ritems.general;

import net.minecraft.server.v1_12_R1.NBTTagCompound;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class SpawnEgg {
    private EntityType type;

    public SpawnEgg(EntityType type) {
        this.type = type;
    }
    
    public EntityType getSpawnedType() {
        return type;
    }

    public void setSpawnedType(EntityType type) {
        if (type.isAlive()) {
            this.type = type;
        }
    }

    public ItemStack toItemStack() {
        return toItemStack(1);
    }

    @SuppressWarnings("deprecation")
    public ItemStack toItemStack(int amount) {
        ItemStack item = new ItemStack(Material.MONSTER_EGG, amount);
        net.minecraft.server.v1_12_R1.ItemStack stack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tagCompound = stack.getTag();
        if(tagCompound == null){
            tagCompound = new NBTTagCompound();
        }
        NBTTagCompound id = new NBTTagCompound();
        id.setString("id", type.getName());
        tagCompound.set("EntityTag", id);
        stack.setTag(tagCompound);
        return CraftItemStack.asBukkitCopy(stack);
    }

    public static SpawnEgg fromItemStack(ItemStack item) {
        if (item == null)
            throw new IllegalArgumentException("item cannot be null");
        if (item.getType() != Material.MONSTER_EGG )
            throw new IllegalArgumentException("item is not a monster egg");
        net.minecraft.server.v1_12_R1.ItemStack stack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tagCompound = stack.getTag();
        if (tagCompound != null) {    
            @SuppressWarnings("deprecation")
            EntityType type = EntityType.fromName(tagCompound.getCompound("EntityTag").getString("id"));    
            if (type != null) {
                return new SpawnEgg(type);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}