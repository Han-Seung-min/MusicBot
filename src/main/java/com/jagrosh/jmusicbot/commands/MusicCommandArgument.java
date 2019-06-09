package com.jagrosh.jmusicbot.commands;

import com.jagrosh.jmusicbot.audio.PlayerManager;

public class MusicCommandArgument {

    private PlayerManager players;
    public PlayerManager getPlayers() { return this.players; }

    private String emogi;
    public String getEmogi() { return this.emogi; }

    public MusicCommandArgument(PlayerManager players, String emogi)
    {
        this.players = players;
        this.emogi = emogi;
    }
}