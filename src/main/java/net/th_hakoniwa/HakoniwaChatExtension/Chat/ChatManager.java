package net.th_hakoniwa.HakoniwaChatExtension.Chat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import net.th_hakoniwa.HakoniwaChatExtension.Enum.ChatDestination;

public class ChatManager {
	private static ChatManager instance = null;

	private Map<UUID, ChatDestination> chatDest = new HashMap<>();
	private Map<UUID, UUID> chatDestOption = new HashMap<>();

	private ChatManager(){}

	public static ChatManager getInstance(){
		if(instance == null){
			instance = new ChatManager();
		}
		return instance;
	}

	public void onJoin(Player p){
		chatDest.put(p.getUniqueId(), ChatDestination.GLOBAL);
	}

	public void onQuit(Player p){
		chatDest.remove(p.getUniqueId());
		chatDestOption.remove(p.getUniqueId());
	}

	public void setDestination(UUID uid, ChatDestination dest, UUID option){
		chatDest.put(uid, dest);
		if(option != null){
			chatDestOption.put(uid, option);
		}else {
			chatDestOption.remove(uid);
		}
	}

	public ChatDestination getDestination(UUID uid){
		return chatDest.get(uid);
	}

	public UUID getOption(UUID uid){
		return chatDestOption.get(uid);
	}
}
