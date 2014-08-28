package com.octopod.mgframe.chatbuilder;

import com.octopod.octal.minecraft.AbstractPlayer;
import net.minecraft.server.v1_7_R2.ChatSerializer;
import net.minecraft.server.v1_7_R2.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;

public class BukkitPlayer extends AbstractPlayer {
	
	 private Object player;
	 
	 public BukkitPlayer(Object p) {player = p;}
	 
	 public void sendJsonMessage(String json) {
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a(json));
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
	 }

}