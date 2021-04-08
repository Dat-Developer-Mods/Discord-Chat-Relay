package com.datdeveloper.discordchatrelay.relay;

import com.datdeveloper.discordchatrelay.DiscordChatRelay;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DiscordChatRelay.MOD_ID)
public class MinecraftRelay {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void relayChat(ServerChatEvent event) {
        DiscordChatRelay.INSTANCE.serverManager.sendMessageToAllServers(event.getComponent().getUnformattedText());
    }
}
