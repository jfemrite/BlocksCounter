package com.lfn.blockscounter;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;


public class Actionbar_1_8_R3 implements Actionbar {

	@Override
	public void sendActionbar(Player p, String message) {

		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte) 2);
		
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		
	}

}
