package com.datdeveloper.discordchatrelay.relay;

import com.datdeveloper.discordchatrelay.DiscordChatRelay;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber(modid = DiscordChatRelay.MOD_ID)
public class MinecraftRelay {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void relayChat(ServerChatEvent event) {
        // TODO: Replace with more dynamic system
        String message = event.getComponent().getUnformattedText().replace("@everyone", "everyone").replace("@here", "here");
        DiscordChatRelay.INSTANCE.serverManager.sendMessageToAllServers(message);
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayerMP)) return;
        DiscordChatRelay.INSTANCE.serverManager.sendMessageToAllServers(event.getEntityLiving().getCombatTracker().getDeathMessage().getUnformattedText());
    }

    @SubscribeEvent
    public static void onPlayerAdvancement(AdvancementEvent event) {
        DisplayInfo displayInfo = event.getAdvancement().getDisplay();
        if (displayInfo == null) return;
        DiscordChatRelay.INSTANCE.serverManager.sendMessageToAllServers(event.getEntityPlayer().getDisplayNameString() + " has made the advancement: " + displayInfo.getTitle().getUnformattedText());
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        DiscordChatRelay.INSTANCE.serverManager.sendMessageToAllServers(event.player.getDisplayNameString() + " joined the game");
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        DiscordChatRelay.INSTANCE.serverManager.sendMessageToAllServers(event.player.getDisplayNameString() + " left the game");
    }
}
