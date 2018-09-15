package me.pmilon.RubidiaManager.chunks;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import me.pmilon.RubidiaManager.RubidiaManagerPlugin;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.io.Closer;
import com.sk89q.worldedit.util.io.file.FilenameException;
import com.sk89q.worldedit.world.World;

public class ChunkManager {

	private final static HashMap<Chunk,ChunkManager> managers = new HashMap<Chunk,ChunkManager>();
	
	private final Chunk chunk;
	
	public BukkitWorld localWorld;
	public EditSession editSession;
	private final LocalSession localSession;
	
	private ChunkManager(Chunk chunk) {
		this.chunk = chunk;
		
		this.localWorld = new BukkitWorld(chunk.getWorld());
		this.editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession((World) this.getLocalWorld(), -1);
		this.localSession = new LocalSession(WorldEdit.getInstance().getConfiguration());
		this.getLocalSession().setRegionSelector((World) this.getLocalWorld(), new CuboidRegionSelector(this.getLocalWorld(), getPastePosition(this.getChunk().getOrigin()), getPastePosition(this.getChunk().getDestination())));
	}
	
	public static ChunkManager getManager(Chunk chunk){
		ChunkManager manager = null;
		if(ChunkManager.managers.containsKey(chunk)){
			manager = ChunkManager.managers.get(chunk);
		}else{
			manager = new ChunkManager(chunk);
			ChunkManager.managers.put(chunk, manager);
		}
		return manager;
	}
	
	public BukkitWorld getLocalWorld() {
		return localWorld;
	}

	public Chunk getChunk() {
		return chunk;
	}
	
	public boolean isSaved(){
		if(this.getFile() != null)return this.getFile().exists();
		return false;
	}
	
	public ClipboardHolder copy(){
		Region region;
		BlockArrayClipboard blockClipboard = null;
		try {
			region = this.getLocalSession().getSelection((World) this.getLocalWorld());
	        blockClipboard = new BlockArrayClipboard(region);
	        blockClipboard.setOrigin(getPastePosition(this.getChunk().getOrigin()));
	        ForwardExtentCopy copy = new ForwardExtentCopy(this.getEditSession(), region, blockClipboard, region.getMinimumPoint());
            
            Operations.completeLegacy(copy);
		} catch (IncompleteRegionException | MaxChangedBlocksException e) {
		}
        
        return new ClipboardHolder(blockClipboard);
	}
	
	public void paste(){
		Location location = this.getChunk().getOrigin();
		ClipboardHolder temp = this.copy();
		
		try {
	        Vector to = getPastePosition(location);
	        Clipboard clipboard = this.getLocalSession().getClipboard().getClipboard();
	        
	        Operation operation = new ClipboardHolder(clipboard)
	                .createPaste(this.getEditSession())
	                .to(to)
	                .ignoreAirBlocks(false)
	                .build();
	        
			Operations.completeLegacy(operation);

			for(Entity entity : location.getChunk().getEntities()){
				if(entity instanceof Player || entity instanceof EnderCrystal || entity instanceof Minecart || entity instanceof Boat)continue;
				if(entity instanceof ArmorStand && !entity.hasMetadata("display") && !entity.hasMetadata("monster") && entity.getCustomName() == null)continue;
				entity.remove();
			}
			
			this.getLocalSession().setClipboard(temp);
		} catch (EmptyClipboardException | MaxChangedBlocksException e) {
		}
	}
	
	public void write(){
        Closer closer = Closer.create();
        
        try {
            File file = WorldEdit.getInstance().getSafeSaveFile(null, this.getFile().getParentFile(), this.getFile().getName(), "schematic", "schematic");

            ClipboardFormat format = ClipboardFormats.findByAlias("schematic");

            ClipboardHolder holder = this.getLocalSession().getClipboard();
            Clipboard clipboard = holder.getClipboard();
            
            FileOutputStream fos = closer.register(new FileOutputStream(file));
            BufferedOutputStream bos = closer.register(new BufferedOutputStream(fos));
            ClipboardWriter writer = closer.register(format.getWriter(bos));
            writer.write(clipboard);
        } catch (IOException | EmptyClipboardException | FilenameException e) {
        } finally {
            try {
                closer.close();
            } catch (IOException ignored) {
            }
        }
	}
	
	public void read(){
        ClipboardFormat format = ClipboardFormats.findByAlias("schematic");
        Closer closer = Closer.create();
        
		try {
			File file = WorldEdit.getInstance().getSafeSaveFile(null, this.getFile().getParentFile(), this.getFile().getName(), "schematic", "schematic");
			
			FileInputStream fis = closer.register(new FileInputStream(file));
	        BufferedInputStream bis = closer.register(new BufferedInputStream(fis));
	        ClipboardReader reader = format.getReader(bis);

	        Clipboard clipboard = reader.read();
	        
	        this.getLocalSession().setClipboard(new ClipboardHolder(clipboard));
		} catch (IOException | FilenameException ex) {
		}
	}
	
	public void save(){
		this.getLocalSession().setClipboard(this.copy());
		this.write();
	}
	
	public void load(){
		this.read();
		this.paste();
	}

	private Vector getPastePosition(Location loc) {
		return new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	public File getFile(){
		return new File(RubidiaManagerPlugin.getSavesFolder(), this.getChunk().getSaveName() + ".schematic");
	}
	
	public EditSession getEditSession() {
		return editSession;
	}

	public LocalSession getLocalSession() {
		return localSession;
	}

}
