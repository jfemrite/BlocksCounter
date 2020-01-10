package com.lfn.blockscounter;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;

public class Actionbar_1_8_R1 implements Actionbar {

	@Override
	public void sendActionbar(Player p, String message) {
		
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte) 2);
		
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		
	}

}
