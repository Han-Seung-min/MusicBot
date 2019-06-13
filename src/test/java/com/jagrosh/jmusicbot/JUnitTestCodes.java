package com.jagrosh.jmusicbot;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.examples.command.AboutCommand;
import com.jagrosh.jdautilities.examples.command.PingCommand;
import com.jagrosh.jmusicbot.audio.Nowplaying;
import com.jagrosh.jmusicbot.audio.NowplayingConfig;
import com.jagrosh.jmusicbot.audio.NowplayingHandler;
import com.jagrosh.jmusicbot.audio.Player;
import com.jagrosh.jmusicbot.audio.PlayerConfig;
import com.jagrosh.jmusicbot.audio.PlayerManager;
import com.jagrosh.jmusicbot.commands.MusicCommandArgument;
import com.jagrosh.jmusicbot.commands.commandbuilder.music.MusicCommandsBuilder;
import com.jagrosh.jmusicbot.commands.general.SettingsCmd;
import com.jagrosh.jmusicbot.commands.MusicCommand;
import com.jagrosh.jmusicbot.commands.dj.*;
import com.jagrosh.jmusicbot.entities.Prompt;
import com.jagrosh.jmusicbot.playlist.PlaylistConfig;
import com.jagrosh.jmusicbot.playlist.PlaylistLoader;
import com.jagrosh.jmusicbot.settings.SettingsManager;
import com.jagrosh.jmusicbot.utils.OtherUtil;

import net.dv8tion.jda.core.Permission;

public class JUnitTestCodes {
    public final String PLAY_EMOJI  = "\u25B6"; // ▶
    public final String PAUSE_EMOJI = "\u23F8"; // ⏸
    public final String STOP_EMOJI  = "\u23F9"; // ⏹
    public final Permission[] RECOMMENDED_PERMS = new Permission[]{Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_HISTORY, Permission.MESSAGE_ADD_REACTION,
                                Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_MANAGE, Permission.MESSAGE_EXT_EMOJI,
                                Permission.MANAGE_CHANNEL, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK, Permission.NICKNAME_CHANGE};
    public String[] args = new String[] {};

    
    
    //Seung-hun
	@Test
	public void MuiscCommandsBuilder_Test() {
		// startup log
        Logger log = LoggerFactory.getLogger("Startup");
        
        // create prompt to handle startup
        Prompt prompt = new Prompt("JMusicBot", "Switching to nogui mode. You can manually start in nogui mode by including the -Dnogui=true flag.", 
                "true".equalsIgnoreCase(System.getProperty("nogui", "false")));
        
        // check deprecated nogui mode (new way of setting it is -Dnogui=true)
        for(String arg: args)
            if("-nogui".equalsIgnoreCase(arg))
            {
                prompt.alert(Prompt.Level.WARNING, "GUI", "The -nogui flag has been deprecated. "
                        + "Please use the -Dnogui=true flag before the name of the jar. Example: java -jar -Dnogui=true JMusicBot.jar");
                break;
            }
        
        // get and check latest version
        String version = OtherUtil.checkVersion(prompt);
        
		// load config
        BotConfig config = new BotConfig(prompt);
        config.load();
        if(!config.isValid())
            return;
        
     // set up the listener
        EventWaiter waiter = new EventWaiter();
        SettingsManager settings = new SettingsManager();
        Bot bot = new Bot.Builder().setEventWaiter(waiter).setBotConfig(config).setSettingsManager(settings).build();
        
        NowplayingHandler nowplaying = new NowplayingHandler((Nowplaying)bot, (NowplayingConfig)config);
        nowplaying.init();
        
        PlaylistLoader playlists = new PlaylistLoader((PlaylistConfig)config);

        PlayerManager playermanager = new PlayerManager((Player)bot, nowplaying, (PlayerConfig)config, playlists);
        playermanager.init();
        
		//<editor-fold desc="Create Commands">
        // configure all commands
        ArrayList<Command> commands = new ArrayList<>(); // container for delivering commands

        // about command
        AboutCommand aboutCommand = new AboutCommand(Color.BLUE.brighter(),
                                "a music bot that is [easy to host yourself!](https://github.com/jagrosh/MusicBot) (v"+version+")",
                                new String[]{"High-quality music playback", "FairQueue™ Technology", "Easy to host yourself"},
                                RECOMMENDED_PERMS);
        aboutCommand.setIsAuthor(false);
        aboutCommand.setReplacementCharacter("\uD83C\uDFB6"); // 🎶


        commands.add(aboutCommand);
        commands.add(new PingCommand());
        commands.add(new SettingsCmd());

        // Music command
        MusicCommandsBuilder musicCommandBuilder = new MusicCommandsBuilder(new MusicCommandArgument(playermanager, config.getLoading()));
        commands.addAll(
                //configure Music Commands
                musicCommandBuilder
                    // create normal music commands
                    .setEmogi("")
                    .addLyricsCmd()
                    .addNowPlayingCmd()

                    .setEmogi(config.getLoading())
                    .addPlayCmd()

                    .setEmogi("")
                    .addPlaylistsCmd()
                    .addQueueCmd()
                    .addRemoveCmd()

                    .setEmogi(config.getSearching())
                    .setEmogi(null)
                    .setEmogi("random")
                    
                    .addSearchCmd()
                    .addSCSearchCmd()

                    .setEmogi("")
                    .addShuffleCmd()
                    .addSkipCmd()

                    // create DJ Music Commands
                    .addForceskipCmd()
                    .addMoveTrackCmd()
                    .addPauseCmd()

                    .setEmogi(config.getLoading())
                    .addPlaynextCmd()

                    .setEmogi("")
                    .addRepeatCmd()
                    .addSkiptoCmd()
                    .addStopCmd()
                    .addVolumeCmd()
                .build()
        );
	}
	
	
	
	//Seung-min
    @Test
    public void Cmd_Test() {
    	//Han Seung-min
        MusicCommand musicCmd; 
        musicCmd = new ForceskipCmd();
        musicCmd.doCommand(null);
        
        musicCmd = new MoveTrackCmd();
        musicCmd.doCommand(null);
        
        musicCmd = new PauseCmd();
        musicCmd.doCommand(null);
        
        musicCmd = new PlaynextCmd();
        musicCmd.doCommand(null);
        
        musicCmd = new RepeatCmd();
        musicCmd.doCommand(null);
        
        musicCmd = new SkiptoCmd();
        musicCmd.doCommand(null);
        
        musicCmd = new StopCmd();
        musicCmd.doCommand(null);
        
        musicCmd = new VolumeCmd();
    	musicCmd.doCommand(null);
    }
	
}
