package com.datdeveloper.discordchatrelay.relay;

import com.datdeveloper.discordchatrelay.DiscordChatRelay;
import com.datdeveloper.discordchatrelay.ServerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.jetbrains.annotations.NotNull;

public class DiscordRelay extends ListenerAdapter {
    ServerManager serverManager;

    public DiscordRelay(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (!event.getName().equals("setrelaychannel")) return;

        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {

            serverManager.setServerChatChannel(event.getGuild(), event.getChannel().getId());
            event.reply("Successfully set relay channel to this one").setEphemeral(true).queue();
        } else {
            event.reply("You don't have permission to do that").setEphemeral(true).queue();
        }
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String message = event.getMessage().getContentDisplay();
        if (message.isEmpty()) return;

        String username = event.getMember().getNickname();
        username = (username == null ? event.getAuthor().getName() : username);
        if (serverManager.getServerChatChannel(event.getGuild()).equals(event.getChannel()) && DiscordChatRelay.INSTANCE.ready) FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendMessage(new TextComponentString(TextFormatting.BLUE + "[Discord] " + TextFormatting.RESET + "<" + username + "> " + message));
    }
}
