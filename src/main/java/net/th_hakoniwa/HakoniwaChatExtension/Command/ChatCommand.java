package net.th_hakoniwa.HakoniwaChatExtension.Command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.th_hakoniwa.HakoniwaChatExtension.Chat.ChatManager;
import net.th_hakoniwa.HakoniwaChatExtension.Enum.ChatDestination;
import net.th_hakoniwa.HakoniwaClanExtension.API.HakoniwaClanAPI;
import net.th_hakoniwa.HakoniwaClanExtension.Data.Clan;
import net.th_hakoniwa.HakoniwaCore.Core.Command.Sub.Base.HCSubCommandBase;

public class ChatCommand implements HCSubCommandBase {

	@Override
	public boolean onSubCommand(CommandSender sender, String subCmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			//プレイヤー以外は実行不可
			sender.sendMessage("[§aHCT§f] §cThis command can only be executed by the player.");
			return false;
		}

		Player p = (Player) sender;


		if(args.length > 0) {
			if(args[0].equalsIgnoreCase("global")) {
				//グローバルチャット
				ChatManager.getInstance().setDestination(p.getUniqueId(), ChatDestination.GLOBAL, null);
				//通知
				p.sendMessage("[§aHCT§f] チャットの送信先を§eグローバルチャンネル§fに設定しました");
				return true;
			}else if(args[0].equalsIgnoreCase("clan")) {
				if(args.length > 1) {
					//対象指定
					if(p.hasPermission("hakoniwa.admin.chat")) {
						Clan target = HakoniwaClanAPI.getClanFromName(args[1]);

						if(target == null) {
							p.sendMessage("[§aHCT§f] §c対象のクランが見つかりません");
							return false;
						}

						//クランチャット(管理者割り込み)
						ChatManager.getInstance().setDestination(p.getUniqueId(), ChatDestination.CLAN_OTHER, target.getUniqueId());
						//通知
						p.sendMessage("[§aHCT§f] チャットの送信先を§eクラン\"" + target.getClanName() + "\"§fに設定しました");

						return true;
					}
				}

				//通常クランチャット
				Clan clan = HakoniwaClanAPI.getClanFromPlayerUUID(p.getUniqueId());
				if(clan != null) {
					//クランチャット
					ChatManager.getInstance().setDestination(p.getUniqueId(), ChatDestination.CLAN, clan.getUniqueId());
					//通知
					p.sendMessage("[§aHCT§f] チャットの送信先を§e所属クラン§fに設定しました");
					return true;
				}else {
					p.sendMessage("[§aHCT§f] §cクランに所属していないため送信先を変更できませんでした");
				}
			}
		}

		//ヘルプ
		p.sendMessage("[§aHCT§f] +- HakoniwaMapEditExtension (for HakoniwaCore) command help -+");
		p.sendMessage("[§aHCT§f] /" + label + " " + subCmd + " global");
		p.sendMessage("[§aHCT§f] /" + label + " " + subCmd + " clan");
		if(p.hasPermission("hakoniwa.admin.chat")) p.sendMessage("[§aHCT§f] /" + label + " " + subCmd + " clan [ClanName]");

		return false;
	}
}
