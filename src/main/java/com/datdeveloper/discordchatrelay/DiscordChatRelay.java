package com.datdeveloper.discordchatrelay;

import com.datdeveloper.discordchatrelay.relay.DiscordRelay;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;

import javax.security.auth.login.LoginException;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;

@Mod(
        modid = DiscordChatRelay.MOD_ID,
        name = DiscordChatRelay.MOD_NAME,
        version = DiscordChatRelay.VERSION,
        serverSideOnly = true,
        acceptableRemoteVersions = "*"
)
public class DiscordChatRelay {

    public static final String MOD_ID = "discordchatrelay";
    public static final String MOD_NAME = "Discord Chat Relay";
    public static final String VERSION = "1.0";
    public static Logger log;

    public JDA discordAPI;
    public boolean ready = false;

    public ServerManager serverManager;

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static DiscordChatRelay INSTANCE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) throws FileNotFoundException {
        log = event.getModLog();

        serverManager = new ServerManager(event.getModConfigurationDirectory());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        log.info("Starting Discord Chat Relay");
        if (!ModConfig.discordToken.isEmpty()) {
            try {
                discordAPI = JDABuilder.createLight(ModConfig.discordToken, GatewayIntent.GUILD_MESSAGES)
                        .addEventListeners(new DiscordRelay(serverManager))
                        .build();
            } catch (LoginException e) {
                log.error("Failed to start Discord Chat Relay, the token appears to be invalid");
                e.printStackTrace();
            }
        } else {
            log.error("Failed to start Discord Chat Relay, you need to set the token in config");
        }
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        ready = discordAPI != null;
        if (!ready) log.warn("Discord Chat Relay failed to start, messages will not be relayed");
    }
}
