package me.pmilon.RubidiaCore.books;

import java.lang.reflect.Method;
import java.util.List;

import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.damages.DamageManager;
import me.pmilon.RubidiaCore.damages.RDamageCause;
import me.pmilon.RubidiaCore.utils.ReflectionUtils;
import me.pmilon.RubidiaCore.utils.ReflectionUtils.PackageType;
import me.pmilon.RubidiaCore.utils.Utils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.IChatBaseComponent.ChatSerializer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftMetaBook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.Plugin;

public class BookUtils {
	
	private static boolean initialised = false;
	private static Method getHandle;
	private static Method openBook;
	   
	static {
	   try {
	      getHandle = ReflectionUtils.getMethod("CraftPlayer", PackageType.CRAFTBUKKIT_ENTITY, "getHandle");
	      openBook = ReflectionUtils.getMethod("EntityPlayer", PackageType.MINECRAFT_SERVER, "a", PackageType.MINECRAFT_SERVER.getClass("ItemStack"), PackageType.MINECRAFT_SERVER.getClass("EnumHand"));
	      initialised = true;
	   } catch (ReflectiveOperationException e) {
	      e.printStackTrace();
	      Bukkit.getServer().getLogger().warning("Cannot force open book!");
	   }
	}
	
	@SuppressWarnings("unchecked")
	public static void openCharacteristicsBook(Plugin plugin, final Player player, ItemStack oldItem){
		RPlayer rp = RPlayer.get(player);
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK,1);
		BookMeta meta = (BookMeta) book.getItemMeta();
		List<IChatBaseComponent> pages;
		try {
		    pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(meta);
		} catch (ReflectiveOperationException ex) {
		    ex.printStackTrace();
		    return;
		}
		
		TextComponent tPage2 = new TextComponent(" §8§l" + ("CARACTERISTIQUES") + "\n§n                            \n\n ");
		TextComponent hover21 = new TextComponent("§9§l» §r" + ("Vie max :"));
		hover21.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(("§7Vie maximale"))}));
		tPage2.addExtra(hover21);
		tPage2.addExtra(" §8" + rp.getMaxHealth() + " §rPV\n ");
		TextComponent hover22 = new TextComponent("§9§l» §r" + ("Attaque :"));
		hover22.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(("§7Dégâts moyen avec l'arme en main\n§7Détails au survol : Mêlée, distance, magie et dégâts critiques"))}));
		tPage2.addExtra(hover22);
		tPage2.addExtra(" §8");
		TextComponent hover23 = new TextComponent(String.valueOf(Utils.round(DamageManager.getDamages(player, null, oldItem, RDamageCause.MELEE, false, true),3)));
		hover23.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("§8" + ("Critique :") + " §7" + rp.getAverageCriticalMagicDamages(oldItem))}));
		tPage2.addExtra(hover23);
		tPage2.addExtra("\n ");
		TextComponent hover24 = new TextComponent("§9§l» §r" + ("Défense :"));
		hover24.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(("§7Défense physique moyenne avec l'armure équipée"))}));
		tPage2.addExtra(hover24);
		tPage2.addExtra(" §8");
		TextComponent hover241 = new TextComponent(String.valueOf(rp.getAverageDefense()));
		hover241.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("§7" + ("§7vos boucliers lorsque vous ne bloquez pas !"))}));
		tPage2.addExtra(hover241);
		tPage2.addExtra("\n ");
		TextComponent hover25 = new TextComponent("§9§l» §r" + ("Chance CC :"));
		hover25.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(("§7Chance de coup critique (en sautant)"))}));
		tPage2.addExtra(hover25);
		tPage2.addExtra(" §8" + Utils.round(20+rp.getCriticalStrikeChanceFactor()*100, 1) + "%\n ");
		TextComponent hover251 = new TextComponent("§9§l» §r" + ("Dégâts CC :"));
		hover251.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(("§7Dégâts totaux des coups critiques"))}));
		tPage2.addExtra(hover251);
		tPage2.addExtra(" §8" + Utils.round(rp.getCriticalStrikeDamagesFactor()*100, 1) + "%\n ");
		TextComponent hover27 = new TextComponent("§9§l» §r" + ("Régén. :"));
		hover27.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(("§7Vitesse de régénération de la vigueur"))}));
		tPage2.addExtra(hover27);
		tPage2.addExtra(" §8" + Utils.round(rp.getVigorPerSecond(),2) + " §rEP/s\n ");
		TextComponent hover28 = new TextComponent("§9§l» §r" + ("Dgts compt. :"));
		hover28.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(("§7Dégâts totaux des compétences"))}));
		tPage2.addExtra(hover28);
		tPage2.addExtra(" §8" + Utils.round(rp.getAbilityDamagesFactor()*100, 1) + "%\n ");
		TextComponent hover26 = new TextComponent("§9§l» §r" + ("Def compt. :"));
		hover26.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(("§7Défense magique"))}));
		tPage2.addExtra(hover26);
		tPage2.addExtra(" §8" + Utils.round(100+rp.getAbilityDefenseFactor()*100, 1) + " §r%\n ");
		TextComponent hover29 = new TextComponent("§9§l» §r" + ("Blocage :"));
		hover29.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(("§7Chance de bloquer une attaque\n§7Bloquer une attaque n'inflige aucun dégâts"))}));
		tPage2.addExtra(hover29);
		tPage2.addExtra(" §8" + Utils.round(rp.getBlockChanceFactor()*100, 2) + "%\n ");
		TextComponent hover210 = new TextComponent("§9§l» §r" + ("Butin rare:"));
		hover210.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(("§7Chance d'obtenir un butin rare"))}));
		tPage2.addExtra(hover210);
		tPage2.addExtra(" §8+" + Utils.round(rp.getLootBonusChanceFactor()*100, 2) + "%\n ");
		TextComponent hover211 = new TextComponent("§9§l» §r" + ("Coût élev. :"));
		hover211.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(("§7Coût d'élévation en élytres"))}));
		tPage2.addExtra(hover211);
		tPage2.addExtra(" §8" + Utils.round(rp.getAeroplaneCost(), 3) + " §rEP");
		
		IChatBaseComponent page2 = ChatSerializer.a(ComponentSerializer.toString(tPage2));
		pages.add(page2);
		
		meta.setTitle("Characteristics");
		meta.setAuthor("Rubidia");
		book.setItemMeta(meta);
		BookUtils.openBook(/*plugin, player, */book, player/*, oldItem, slot*/);
	}
	
	public static boolean openBook(ItemStack i, Player p) {
	   if (!initialised) return false;
	   ItemStack held = p.getInventory().getItemInMainHand();
	   try {
	      p.getInventory().setItemInMainHand(i);
	      sendPacket(i, p);
	   } catch (ReflectiveOperationException e) {
	      e.printStackTrace();
	      initialised = false;
	   }
	   p.getInventory().setItemInMainHand(held);
	   return initialised;
	}
	
	private static void sendPacket(ItemStack i, Player p) throws ReflectiveOperationException {
	   Object entityplayer = getHandle.invoke(p);
	   Class<?> enumHand = PackageType.MINECRAFT_SERVER.getClass("EnumHand");
	   Object[] enumArray = enumHand.getEnumConstants();
	   openBook.invoke(entityplayer, getItemStack(i), enumArray[0]);
	}
	
	public static Object getItemStack(ItemStack item) {
	   try {
	      Method asNMSCopy = ReflectionUtils.getMethod(PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftItemStack"), "asNMSCopy", ItemStack.class);
	      return asNMSCopy.invoke(PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftItemStack"), item);
	   } catch (Exception e) {
	      e.printStackTrace();
	   }
	   return null;
	}
}
