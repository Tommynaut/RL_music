package com.show_music;

import com.show_music.config.DisplayMode;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("musicOverlay")
public interface ShowMusicConfig  extends Config {
    @ConfigItem(
            keyName = "muteHide",
            name = "Hide on Mute",
            description = "If the music volume is muted, the text/overlay will be hidden.",
            position = 1
    )
    default boolean muteHide()
    {
        return false;
    }

    @ConfigItem(
            keyName = "displayToggle",
            name = "Display",
            description = "Toggles display between overlay and chat box",
            position = 2
    )
    default DisplayMode displayToggle(){
        return DisplayMode.Overlay;
    }

	@ConfigItem(
			keyName = "showUnlockHint",
			name = "Show Unlock Hint (text only)",
			description = "Show the unlock hint for the currently playing song, only for Text display mode",
			position = 3
	)
	default boolean showUnlockHint() { return false; }
}
