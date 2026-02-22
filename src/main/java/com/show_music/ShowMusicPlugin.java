package com.show_music;

import javax.inject.Inject;

import com.google.inject.Provides;
import com.show_music.config.DisplayMode;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Music Track Display"
)
public class ShowMusicPlugin extends Plugin
{
	private static final int MUSIC_TRACK_WIDGET_ID = 15663108;
	private String lastTrack = "";

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;
	@Inject
	private ShowMusicOverlay overlay;
	@Inject
	private ConfigManager configManager;
	@Inject
	private ChatMessageManager chatMessageManager;

	@Provides
	ShowMusicConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ShowMusicConfig.class);
	}
	@Override
	protected void startUp() throws Exception
	{
		if(provideConfig(configManager).displayToggle() == DisplayMode.Overlay) overlayManager.add(overlay);
		lastTrack = ""; // Reset on plugin start
	}

	@Override
	protected void shutDown() throws Exception
	{
		if(provideConfig(configManager).displayToggle() == DisplayMode.Overlay) overlayManager.remove(overlay);
		lastTrack = ""; // Clean up on plugin stop
	}

	@Subscribe
	public void onGameTick(GameTick event) {

		if(provideConfig(configManager).displayToggle() == DisplayMode.Overlay || (client.getMusicVolume() == 0 && provideConfig(configManager).muteHide())) return;
		Widget musicWidget = client.getWidget(MUSIC_TRACK_WIDGET_ID);
		if (musicWidget != null) {
			String currentTrack = musicWidget.getText();
			if (currentTrack != null && !currentTrack.equals(lastTrack)) {
				lastTrack = currentTrack;
				chatMessageManager.queue(QueuedMessage.builder()
						.type(net.runelite.api.ChatMessageType.GAMEMESSAGE)
						.runeLiteFormattedMessage("Now playing: " + currentTrack)
						.build());
			}
		}
	}
}
