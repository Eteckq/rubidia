package me.pmilon.RubidiaCore.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class ReflectionUtils {
	
	public enum PackageType {
		MINECRAFT_SERVER("net.minecraft.server." + getServerVersion()),
		CRAFTBUKKIT("org.bukkit.craftbukkit." + getServerVersion()),
		CRAFTBUKKIT_BLOCK(CRAFTBUKKIT, "block"),
		CRAFTBUKKIT_CHUNKIO(CRAFTBUKKIT, "chunkio"),
		CRAFTBUKKIT_COMMAND(CRAFTBUKKIT, "command"),
		CRAFTBUKKIT_CONVERSATIONS(CRAFTBUKKIT, "conversations"),
		CRAFTBUKKIT_ENCHANTMENS(CRAFTBUKKIT, "enchantments"),
		CRAFTBUKKIT_ENTITY(CRAFTBUKKIT, "entity"),
		CRAFTBUKKIT_EVENT(CRAFTBUKKIT, "event"),
		CRAFTBUKKIT_GENERATOR(CRAFTBUKKIT, "generator"),
		CRAFTBUKKIT_HELP(CRAFTBUKKIT, "help"),
		CRAFTBUKKIT_INVENTORY(CRAFTBUKKIT, "inventory"),
		CRAFTBUKKIT_MAP(CRAFTBUKKIT, "map"),
		CRAFTBUKKIT_METADATA(CRAFTBUKKIT, "metadata"),
		CRAFTBUKKIT_POTION(CRAFTBUKKIT, "potion"),
		CRAFTBUKKIT_PROJECTILES(CRAFTBUKKIT, "projectiles"),
		CRAFTBUKKIT_SCHEDULER(CRAFTBUKKIT, "scheduler"),
		CRAFTBUKKIT_SCOREBOARD(CRAFTBUKKIT, "scoreboard"),
		CRAFTBUKKIT_UPDATER(CRAFTBUKKIT, "updater"),
		CRAFTBUKKIT_UTIL(CRAFTBUKKIT, "util");

		private final String path;

		/**
		 * Construct a new package type
		 * 
		 * @param path Path of the package
		 */
		private PackageType(String path) {
			this.path = path;
		}

		/**
		 * Construct a new package type
		 * 
		 * @param parent Parent package of the package
		 * @param path Path of the package
		 */
		private PackageType(PackageType parent, String path) {
			this(parent + "." + path);
		}

		/**
		 * Returns the path of this package type
		 * 
		 * @return The path
		 */
		public String getPath() {
			return path;
		}

		/**
		 * Returns the class with the given name
		 * 
		 * @param className Name of the desired class
		 * @return The class with the specified name
		 * @throws ClassNotFoundException If the desired class with the specified name and package cannot be found
		 */
		public Class<?> getClass(String className) throws ClassNotFoundException {
			return Class.forName(this + "." + className);
		}

		// Override for convenience
		@Override
		public String toString() {
			return path;
		}

		/**
		 * Returns the version of your server
		 * 
		 * @return The server version
		 */
		public static String getServerVersion() {
			return Bukkit.getServer().getClass().getPackage().getName().substring(23);
		}
	}
	
	public static String getVersion() {
		String name = Bukkit.getServer().getClass().getPackage().getName();
		String version = name.substring(name.lastIndexOf('.') + 1) + ".";
		return version;
	}

	public static Class<?> getNMSClass(String className) {
		String fullName = "net.minecraft.server." + getVersion() + className;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(fullName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clazz;
	}

	public static Class<?> getNMSClassWithException(String className) throws Exception {
		String fullName = "net.minecraft.server." + getVersion() + className;
		Class<?> clazz = Class.forName(fullName);
		return clazz;
	}

	public static Class<?> getOBCClass(String className) {
		String fullName = "org.bukkit.craftbukkit." + getVersion() + className;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(fullName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clazz;
	}

	public static Object getHandle(Object obj) {
		try {
			return getMethod(obj.getClass(), "getHandle", new Class[0]).invoke(obj, new Object[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Field getField(Class<?> clazz, String name) {
		try {
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			return field;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Method getMethod(Class<?> clazz, String name, Class<?>... args) {
		for (Method m : clazz.getMethods()) {
			if (m.getName().equals(name) && (args.length == 0 || ClassListEqual(args, m.getParameterTypes()))) {
				m.setAccessible(true);
				return m;
			}
		}
		return null;
	}
	
	public static Method getMethod(String className, PackageType packageType, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException, ClassNotFoundException {
		return getMethod(packageType.getClass(className), methodName, parameterTypes);
	}

	public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
		boolean equal = true;
		if (l1.length != l2.length) return false;
		for (int i = 0; i < l1.length; i++) {
			if (l1[i] != l2[i]) {
				equal = false;
				break;
			}
		}
		return equal;
	}



	public static void sendPacket(Player p, Object packet) {
		if (p == null || packet == null) throw new IllegalArgumentException("player and packet cannot be null");
		try {
			Object handle = ReflectionUtils.getHandle(p);
			Object connection = ReflectionUtils.getField(handle.getClass(), "playerConnection").get(handle);
			ReflectionUtils.getMethod(connection.getClass(), "sendPacket", ReflectionUtils.getNMSClass("Packet")).invoke(connection, new Object[] { packet });
		} catch (Exception e) {
		}
	}


	
	public static Object getPrivateField(String fieldName, Class<?> clazz, Object object)
    {
        Field field;
        Object o = null;

        try
        {
            field = clazz.getDeclaredField(fieldName);

            field.setAccessible(true);

            o = field.get(object);
        }
        catch(NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return o;
    }
}