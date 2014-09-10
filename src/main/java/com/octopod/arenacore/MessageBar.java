package com.octopod.arenacore;

import com.octopod.arenacore.reflection.ReflectionException;
import com.octopod.arenacore.reflection.ReflectionUtils;
import net.minecraft.server.v1_7_R4.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MessageBar {

	private static Map<UUID, MessageBar> playerBars = new HashMap<>();
	final static int ENTITY_ID = 1234;

	public static MessageBar addMessageBar(Player player, String message)
	{
		MessageBar bar = new MessageBar(message);
		playerBars.put(player.getUniqueId(), bar);
		MessageBar.sendPacket(player, bar.getSpawnPacket(player.getLocation().add(0, -300, 0)));
		return bar;
	}

	public static MessageBar getMessageBar(Player player)
	{
		return playerBars.get(player.getUniqueId());
	}

	public static boolean hasMessageBar(Player player)
	{
		return playerBars.containsKey(player.getUniqueId());
	}

	public static void removeMessageBar(Player player)
	{
		MessageBar bar = getMessageBar(player);
		MessageBar.sendPacket(player, bar.getDestroyPacket());
		playerBars.remove(player.getUniqueId());
	}

	public static void sendPacket(Player player, Packet packet)
	{
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
	}

	String message;
	float health;

	public MessageBar() {this(" ", 100);}

	public MessageBar(String message) {this(message, 100);}

	public MessageBar(String message, float health)
	{
		setMessage(message);
		setHealth(health);
	}
	
	public void setMessage(String message)
	{
		this.message = message == null || message.equals("") ? " " : message;
	}

	public String getMessage()
	{
		return message;
	}

	public float getHealth()
	{
		return health;
	}

	public void setHealth(float percent)
	{
		this.health = percent;
	}	

	@SuppressWarnings("deprecation")
	public PacketPlayOutSpawnEntityLiving getSpawnPacket(Location loc)
	{
		PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();
		try {
			ReflectionUtils.setField(packet, "a", (int)ENTITY_ID);
			ReflectionUtils.setField(packet, "b", (byte) EntityType.ENDER_DRAGON.getTypeId());
			ReflectionUtils.setField(packet, "c", loc.getBlockX());
			ReflectionUtils.setField(packet, "d", loc.getBlockY());
			ReflectionUtils.setField(packet, "e", loc.getBlockZ());
			ReflectionUtils.setField(packet, "f", (byte)0);
			ReflectionUtils.setField(packet, "g", (byte)0);
			ReflectionUtils.setField(packet, "h", (byte)0);
			ReflectionUtils.setField(packet, "i", (byte)0);
			ReflectionUtils.setField(packet, "j", (byte)0);
			ReflectionUtils.setField(packet, "k", (byte)0);
			ReflectionUtils.setField(packet, "l", getWatcher());
		} catch (ReflectionException e) {
			e.printStackTrace();
		}
		return packet;
	}

	public PacketPlayOutEntityMetadata getMetaPacket()
	{
		return new PacketPlayOutEntityMetadata(
				ENTITY_ID,
				getWatcher(),
				true
		);
	}

	public PacketPlayOutEntityTeleport getTeleportPacket(Location loc)
	{
		return new PacketPlayOutEntityTeleport(
				ENTITY_ID,
				loc.getBlockX() * 32, loc.getBlockY() * 32, loc.getBlockZ() * 32,
				(byte)0, (byte)0
		);
	}
	
	public PacketPlayOutEntityDestroy getDestroyPacket()
	{
		return new PacketPlayOutEntityDestroy(ENTITY_ID);
	}

	private DataWatcher getWatcher(){
		DataWatcher watcher = new DataWatcher(null);
		
		watcher.a(0, (byte) 0x20); //Flags, 0x20 = invisible
		watcher.a(6, (Float)health * 2); //Health
		watcher.a(7, (Integer)0); //Potion ID
		watcher.a(8, (Byte)(byte)0); //Is potion ambient?
		watcher.a(10, message); //Entity name
		watcher.a(11, (Byte)(byte)1); //Show name, 1 = show, 0 = don't show
		//watcher.a(16, (Integer) (int) health); //Wither health, 300 = full health
		
		return watcher;
	}
}
