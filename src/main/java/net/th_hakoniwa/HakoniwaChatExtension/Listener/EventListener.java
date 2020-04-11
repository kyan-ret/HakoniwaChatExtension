package net.th_hakoniwa.HakoniwaChatExtension.Listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.th_hakoniwa.HakoniwaChatExtension.Chat.ChatManager;
import net.th_hakoniwa.HakoniwaChatExtension.Enum.ChatDestination;
import net.th_hakoniwa.HakoniwaClanExtension.API.HakoniwaClanAPI;
import net.th_hakoniwa.HakoniwaClanExtension.Data.Clan;
import net.th_hakoniwa.HakoniwaCore.API.HakoniwaAPI;
import net.th_hakoniwa.ThRaceExtension.API.ThRaceAPI;
import net.th_hakoniwa.ThRaceExtension.Data.Race;

public class EventListener implements Listener {
	//TODO クランの参加、脱退、解散時にチャット先を更新する


	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		ChatManager.getInstance().onJoin(e.getPlayer());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		ChatManager.getInstance().onQuit(e.getPlayer());
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		//プレイヤー取得
		Player p = e.getPlayer();
		//チャットメッセージ取得
		String chat = e.getMessage();
		//チャットの送信先を取得
		ChatDestination dest = ChatManager.getInstance().getDestination(p.getUniqueId());
		//TODO 送信先一時変更タグを取得
		//.cc chat

		//必要な情報を取得
		Clan clan = HakoniwaClanAPI.getClanFromPlayerUUID(p.getUniqueId());
		Race race = ThRaceAPI.getPlayerData(p.getUniqueId()).getRace();

		//チャット送信先別で処理を分ける
		switch(dest){
		case GLOBAL:
			//グローバルチャット フォーマット変更
			String ctag = "";
			if(clan != null) ctag = "§f[" + ChatColor.translateAlternateColorCodes('&', clan.getClanTag()) + "§f]";
			String rtag = race.getTag();
			String namecolor = "§f";
			if(p.isOp()){
				namecolor = "§b";
			}
			String msg = chat.replaceAll("%", "%%");
			if(p.hasPermission("hakoniwa.admin.chat")) msg = ChatColor.translateAlternateColorCodes('&', msg);
			e.setFormat(ctag + rtag + namecolor + p.getDisplayName() + "§f: " + msg);
			break;
		case CLAN:
			//クランチャット
			e.setCancelled(true);

			//対象クランnullチェック
			if(clan == null) {
				//送信先不明
				p.sendMessage("[§eHC§f] §c送信先クランが見つかりません");
				return;
			}

			HakoniwaClanAPI.sendMessageToClan(clan, "[§bCC§f]" + p.getName() + ": " + e.getMessage());

			//TODO 権限持ちに転送

			break;
		case CLAN_OTHER:
			//クランチャット(管理者用)
			e.setCancelled(true);

			Clan target = HakoniwaClanAPI.getClanFromUUID(ChatManager.getInstance().getOption(p.getUniqueId()));
			if(target == null) {
				//送信先不明
				p.sendMessage("[§eHC§f] §c送信先クランが見つかりません");
				return;
			}

			p.sendMessage("[§cCC§f]<§e" + target.getClanName() + "§f>" + p.getName() + ": " + e.getMessage());
			HakoniwaClanAPI.sendMessageToClan(
					target,
					"[§cCC§f]" + ChatColor.translateAlternateColorCodes('&', HakoniwaAPI.getPrefix(p.getUniqueId())) + "§f"
					+ p.getName() + ": " + e.getMessage()
					);

			//TODO 権限持ちに転送

			break;
		}
	}
}
