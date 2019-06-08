/*
 * Copyright 2016 John Grosh (jagrosh).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jagrosh.jmusicbot;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.examples.command.*;
import com.jagrosh.jmusicbot.audio.Nowplaying;
import com.jagrosh.jmusicbot.audio.NowplayingConfig;
import com.jagrosh.jmusicbot.audio.NowplayingHandler;
import com.jagrosh.jmusicbot.audio.Player;
import com.jagrosh.jmusicbot.audio.PlayerConfig;
import com.jagrosh.jmusicbot.audio.PlayerManager;
import com.jagrosh.jmusicbot.commands.MusicCommand;
import com.jagrosh.jmusicbot.commands.MusicCommandArgument;
import com.jagrosh.jmusicbot.commands.admin.*;
import com.jagrosh.jmusicbot.commands.commandBuilder.Builder.CommandsBuilder;
import com.jagrosh.jmusicbot.commands.commandBuilder.Builder.music.MusicCommandsBuilder;
import com.jagrosh.jmusicbot.commands.dj.*;
import com.jagrosh.jmusicbot.commands.general.*;
import com.jagrosh.jmusicbot.commands.music.*;
import com.jagrosh.jmusicbot.commands.owner.*;
import com.jagrosh.jmusicbot.entities.Prompt;
import com.jagrosh.jmusicbot.gui.GUI;
import com.jagrosh.jmusicbot.playlist.PlaylistLoader;
import com.jagrosh.jmusicbot.playlist.PlaylistConfig;
import com.jagrosh.jmusicbot.settings.SettingsManager;
import com.jagrosh.jmusicbot.shutdown.ShutdownListener;
import com.jagrosh.jmusicbot.utils.OtherUtil;
import java.awt.Color;
import java.util.ArrayList;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class JMusicBot 
{
    public final static String PLAY_EMOJI  = "\u25B6"; // ▶
    public final static String PAUSE_EMOJI = "\u23F8"; // ⏸
    public final static String STOP_EMOJI  = "\u23F9"; // ⏹
    public final static Permission[] RECOMMENDED_PERMS = new Permission[]{Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_HISTORY, Permission.MESSAGE_ADD_REACTION,
                                Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_MANAGE, Permission.MESSAGE_EXT_EMOJI,
                                Permission.MANAGE_CHANNEL, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK, Permission.NICKNAME_CHANGE};
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
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

        AboutCommand aboutCommand = new AboutCommand(Color.BLUE.brighter(),
                                "a music bot that is [easy to host yourself!](https://github.com/jagrosh/MusicBot) (v"+version+")",
                                new String[]{"High-quality music playback", "FairQueue™ Technology", "Easy to host yourself"},
                                RECOMMENDED_PERMS);
        aboutCommand.setIsAuthor(false);
        aboutCommand.setReplacementCharacter("\uD83C\uDFB6"); // 🎶


        // configure all commands
        ArrayList<Command> commands = new ArrayList<>();
        commands.add(aboutCommand);
        commands.add(new PingCommand());
        commands.add(new SettingsCmd());

        MusicCommandsBuilder musicCommandBuilder = new MusicCommandsBuilder(new MusicCommandArgument(playermanager, config.getLoading()));
        commands.addAll(
                musicCommandBuilder
                //region configure Music Commands
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
                    .addSearchCmd()
                    .addSCSearchCmd()

                    .setEmogi("")
                    .addShuffleCmd()
                    .addSkipCmd()
                    //endregion
                .Build()
        );

        commands.add(new ForceskipCmd());
        commands.add(new MoveTrackCmd());
        commands.add(new PauseCmd());
        commands.add(new PlaynextCmd(playermanager, config.getLoading()));
        commands.add(new RepeatCmd());
        commands.add(new SkiptoCmd());
        commands.add(new StopCmd());
        commands.add(new VolumeCmd());
        commands.add(new SetdjCmd());
        commands.add(new SettcCmd());
        commands.add(new SetvcCmd());

        commands.add(new AutoplaylistCmd(playlists));
        commands.add(new PlaylistCmd(playlists));
        commands.add(new SetavatarCmd());
        commands.add(new SetgameCmd());
        commands.add(new SetnameCmd());
        commands.add(new SetstatusCmd());

        // set up the command client
        CommandClientBuilder cb = new CommandClientBuilder()
                .setPrefix(config.getPrefix())
                .setAlternativePrefix(config.getAltPrefix())
                .setOwnerId(Long.toString(config.getOwnerId()))
                .setEmojis(config.getSuccess(), config.getWarning(), config.getError())
                .setHelpWord(config.getHelp())
                .setLinkedCacheSize(200)
                .setGuildSettingsManager(settings)
                .addCommands(commands.toArray(new Command[commands.size()]));

        if(config.useEval())
            cb.addCommand(new EvalCmd(bot));
        boolean nogame = false;
        if(config.getStatus()!=OnlineStatus.UNKNOWN)
            cb.setStatus(config.getStatus());
        if(config.getGame()==null)
            cb.useDefaultGame();
        else if(config.getGame().getName().equalsIgnoreCase("none"))
        {
            cb.setGame(null);
            nogame = true;
        }
        else
            cb.setGame(config.getGame());
        CommandClient client = cb.build();
        
        log.info("Loaded config from "+config.getConfigLocation());
        
        // attempt to log in and start
        try
        {
            JDA jda = new JDABuilder(AccountType.BOT)
                    .setToken(config.getToken())
                    .setAudioEnabled(true)
                    .setGame(nogame ? null : Game.playing("loading..."))
                    .setStatus(config.getStatus()==OnlineStatus.INVISIBLE||config.getStatus()==OnlineStatus.OFFLINE ? OnlineStatus.INVISIBLE : OnlineStatus.DO_NOT_DISTURB)
                    .addEventListener(client, waiter, new Listener(playermanager))
                    .setBulkDeleteSplittingEnabled(true)
                    .build();
            bot.setJDA(jda);
            nowplaying.setJDA(jda);
            
            ShutdownListener shutdownlistener = new ShutdownListener(jda);
            cb.addCommand(new ShutdownCmd(shutdownlistener));
            
            if(!prompt.isNoGUI())
            {
                try 
                {
                    GUI gui = new GUI(shutdownlistener);
                    shutdownlistener.setGUI(gui);
                    gui.init();
                } 
                catch(Exception e) 
                {
                    log.error("Could not start GUI. If you are "
                            + "running on a server or in a location where you cannot display a "
                            + "window, please run in nogui mode using the -Dnogui=true flag.");
                }
            }
            
            shutdownlistener.attach(bot);
            shutdownlistener.attach(nowplaying);
        }
        catch (LoginException ex)
        {
            log.error(ex+"\nPlease make sure you are "
                    + "editing the correct config.txt file, and that you have used the "
                    + "correct token (not the 'secret'!)");
            System.exit(1);
        }
        catch(IllegalArgumentException ex)
        {
            log.error("Some aspect of the configuration is invalid: "+ex);
            System.exit(1);
        }
    }
}
