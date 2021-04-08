package com.datdeveloper.discordchatrelay;

import net.minecraftforge.common.config.Config;

@Config(modid = DiscordChatRelay.MOD_ID, name = DiscordChatRelay.MOD_ID + "/" + DiscordChatRelay.MOD_ID)
public class ModConfig {
    @Config.Name("Discord Token")
    @Config.Comment({"The token for the discord bot as found on the discord developer portal", "https://discord.com/developers/docs/intro"})
    @Config.RequiresMcRestart
    public static String discordToken = "";
}
