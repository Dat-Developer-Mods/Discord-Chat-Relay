package com.datdeveloper.discordchatrelay;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ServerManager {
    private Map<String, String> channelMap;
    File channelMapFile;

    public ServerManager(File configDir) throws FileNotFoundException {
        channelMapFile = new File(configDir, DiscordChatRelay.MOD_ID + "/channelMap.json");
        if (channelMapFile.exists()) {
            Gson gson = new Gson();
            FileReader reader = new FileReader(channelMapFile);
            channelMap = gson.fromJson(reader, new TypeToken<Map<String, String>>(){}.getType());
        } else {
            channelMap = new HashMap<>();
        }
    }

    public TextChannel getServerChatChannel(Guild server) {
        if (channelMap.containsKey(server.getId())) {
            TextChannel channel = server.getTextChannelById(channelMap.get(server.getId()));
            if (channel == null) {
                channel = server.getDefaultChannel();
                setServerChatChannel(server, null);
            }
            return channel;
        } else {
            return server.getDefaultChannel();
        }
    }

    public void setServerChatChannel(Guild server, String channelID) {
        if (channelID == null) {
            channelMap.remove(server.getId());
        }
        channelMap.put(server.getId(), channelID);
        try {
            Gson gson = new Gson();
            if (channelMapFile.exists()) channelMapFile.delete();
            channelMapFile.createNewFile();
            FileWriter writer = new FileWriter(channelMapFile);
            writer.write(gson.toJson(channelMap));
            writer.close();
        } catch (IOException e) {
            DiscordChatRelay.log.error("Failed to store channel map");
        }
    }

    public void sendMessage(Guild server, Message message) {
        getServerChatChannel(server).sendMessage(message).queue();
    }

    public void sendMessageToAllServers(String message) {
        Message theMessage = (new MessageBuilder(message)).build();
        for (Guild server : DiscordChatRelay.INSTANCE.discordAPI.getGuilds()) {
            sendMessage(server, theMessage);
        }
    }
}
