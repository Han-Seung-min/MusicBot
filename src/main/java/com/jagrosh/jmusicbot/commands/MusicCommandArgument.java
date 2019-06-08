package com.jagrosh.jmusicbot.commands.owner;

import com.jagrosh.jmusicbot.audio.PlayerManager;

public class MusicCommandArgument {
    public MusicCommandArgument(PlayerManager players, String emogi)
    {
        this.players = players;
        this.emogi = emogi;
    }

    private PlayerManager players;
    public PlayerManager getPlayers() { return this.players; }
    public void setPlayers(PlayerManager players) { this.players = players; }

    private String emogi;
    public String getEmogi() { return this.emogi; }
    public void setEmogi(String emogi) { this.emogi = emogi; }
}
}
