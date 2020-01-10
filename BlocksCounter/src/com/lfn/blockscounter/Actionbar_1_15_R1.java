package com.lfn.blockscounter;

import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_15_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_15_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_15_R1.PacketPlayOutTitle.EnumTitleAction;

public class Actionbar_1_15_R1 implements Actionbar {

	@Override
	public void sendActionbar(Player p, String message) {

		PacketPlayOutTitle packet = new PacketPlayOutTitle(EnumTitleAction.ACTIONBAR, ChatSerializer.a("{\"text\":\"" + message + "\"}"), 20, 60, 20);
		
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		
	}
	
}
