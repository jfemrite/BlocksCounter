package com.lfn.blockscounter;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;




public class Main extends JavaPlugin implements Listener {
	
	private HashMap<Player, Long> timer = new HashMap<>();
	private HashMap<Player, Integer> blocks = new HashMap<>();
	private HashMap<Player, Integer> finalBlocks = new HashMap<>();

	private PacketPlayOutChat packet;
	
	private String title;
	
	@Override
	public void onEnable() {
	
		this.getConfig().options().copyDefaults();
		saveDefaultConfig();
		
		Bukkit.getPluginManager().registerEvents(this, this);
		String rawTitle = this.getConfig().getString("BlocksCounterPlus.title");
		title = rawTitle.replaceAll("&", "§");
	}
	
	@EventHandler
	public void onBlockUpdate(BlockCountUpdateEvent e) {
		String finalTitle = title.replace("{BPS}", finalBlocks.get(e.getPlayer()).toString());
			 
		packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + finalTitle + "\"}"), (byte) 2);
			
		((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(packet);
		finalBlocks.remove(e.getPlayer());
		
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {

			@Override
			public void run() {
				if(finalBlocks.get(e.getPlayer()) != null) {
					finalBlocks.put(e.getPlayer(), 0);
				}
			}
			
		}, 25L);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		
		Player player = e.getPlayer();
		long initialTime;
		long currentTime = System.currentTimeMillis();
		int totalBlocks;		
		
		if(timer.containsKey(player)) {
			initialTime = timer.get(player);
			
			if(currentTime >= initialTime + 1000L) {

				totalBlocks = blocks.get(player);
				this.finalBlocks.put(player, totalBlocks);

				Bukkit.getPluginManager().callEvent(new BlockCountUpdateEvent(player, this.finalBlocks.get(player)));

				timer.remove(player);
			} else {
				blocks.put(player, blocks.get(player) + 1);
				totalBlocks = blocks.get(player);
			}

		} else if(!timer.containsKey(player)) {
			timer.put(player, System.currentTimeMillis());
			blocks.put(player, 1);
			totalBlocks = 1;
			initialTime = timer.get(player);
		}		
	}
}
