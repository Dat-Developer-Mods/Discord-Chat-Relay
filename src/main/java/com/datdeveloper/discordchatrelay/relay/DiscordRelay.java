package com.datdeveloper.discordchatrelay.relay;

import com.datdeveloper.discordchatrelay.DiscordChatRelay;
import com.datdeveloper.discordchatrelay.ServerManager;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.jetbrains.annotations.NotNull;

public class DiscordRelay extends ListenerAdapter {
    ServerManager serverManager;

    public DiscordRelay(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String message = event.getMessage().getContentDisplay();
        if (message.equals("!DR SetChannel")) {
            serverManager.setServerChatChannel(event.getGuild(), event.getMessage().getChannel().getId());
            serverManager.sendMessage(event.getGuild(), (new MessageBuilder("Successfully set relay channel")).build());
        } else {
            String username = event.getMember().getNickname();
            username = (username == null ? event.getAuthor().getName() : username);
            if (serverManager.getServerChatChannel(event.getGuild()).equals(event.getChannel()) && DiscordChatRelay.INSTANCE.ready) FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendMessage(new TextComponentString("[Discord] <" + username + "> " + message));
        }
    }
}
