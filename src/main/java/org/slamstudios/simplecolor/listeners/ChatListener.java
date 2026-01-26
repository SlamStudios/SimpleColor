package org.slamstudios.simplecolor.listeners;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.slamstudios.simplecolor.ColorParser;
import org.slamstudios.simplecolor.SimpleColor;
import org.slamstudios.simplecolor.SimpleColorConfig;
import org.slamstudios.simplecolor.aliases.Color;

public class ChatListener {

    public static void onPlayerChat(PlayerChatEvent event) {
        SimpleColorConfig config = SimpleColor.getConfig();

        // If chat parsing is disabled, don't modify the event
        if (!config.isChatParsingEnabled()) {
            return;
        }

        PlayerRef playerRef = event.getSender();
        String playerName = playerRef.getUsername();
        String content = event.getContent();
        String format = config.getChatFormat();

        event.setFormatter((player, message) -> {
            // Build the formatted message by replacing placeholders
            // Split format around {message} to handle it specially
            int msgIndex = format.indexOf("{message}");
            if (msgIndex >= 0) {
                String beforeMsg = format.substring(0, msgIndex).replace("{player}", playerName);
                String afterMsg = format.substring(msgIndex + 9).replace("{player}", playerName);

                Message prefix = ColorParser.parse(beforeMsg);
                Message parsedContent = ColorParser.parse(content);
                Message suffix = ColorParser.parse(afterMsg);

                return prefix.insert(parsedContent).insert(suffix);
            } else {
                // No {message} placeholder, just parse the whole format
                String formatted = format.replace("{player}", playerName).replace("{message}", content);
                return ColorParser.parse(formatted);
            }
        });
    }
}
