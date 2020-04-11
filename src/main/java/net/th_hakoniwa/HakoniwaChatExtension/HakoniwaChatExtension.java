package net.th_hakoniwa.HakoniwaChatExtension;

import net.th_hakoniwa.HakoniwaChatExtension.Command.ChatCommand;
import net.th_hakoniwa.HakoniwaChatExtension.Listener.EventListener;
import net.th_hakoniwa.HakoniwaCore.Extension.HakoniwaExtension;

public class HakoniwaChatExtension extends HakoniwaExtension {
	@Override
	public void onEnable(){
		//イベントリスナー登録
		getPlugin().getServer().getPluginManager().registerEvents(new EventListener(), getPlugin());

		//コマンド登録
		registerSubCommand("chat", new ChatCommand(), "Chat manage command");
	}

	@Override
	public void onDisable(){

	}
}
