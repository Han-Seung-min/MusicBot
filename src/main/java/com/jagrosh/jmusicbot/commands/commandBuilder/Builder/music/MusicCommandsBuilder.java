package com.jagrosh.jmusicbot.commands.commandBuilder.Builder.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jmusicbot.commands.MusicCommand;
import com.jagrosh.jmusicbot.commands.MusicCommandArgument;
import com.jagrosh.jmusicbot.commands.commandBuilder.Builder.CommandsBuilder;
import com.jagrosh.jmusicbot.commands.music.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MusicCommandsBuilder extends CommandsBuilder<MusicCommand, MusicCommandArgument> {

    public MusicCommandsBuilder(MusicCommandArgument argument)
    {
        Initialize(new Argument<MusicCommandArgument>(argument));
        createdCommands = new ArrayList<>();
    }

    private Argument<MusicCommandArgument> argument;
    private final List<MusicCommand> createdCommands;

    @Override
    protected void Initialize(Argument<MusicCommandArgument> argument) {
        this.argument = argument;
    }

    @Override
    public Collection<Command> Build() {
        Collection<Command> commands = new ArrayList<>(createdCommands);
        createdCommands.clear();

        return commands;
    }

    // TODO : Change Emogi to Null Object Pattern
    public MusicCommandsBuilder setEmogi(String emogi) {
        this.argument = new Argument<MusicCommandArgument>(new MusicCommandArgument(this.argument.GetArgument().getPlayers(), emogi));
        return this;
    }

    // Add Commands
    public MusicCommandsBuilder addLyricsCmd() {
        addCmd(new LyricsCmd());
        return this;
    }
    public MusicCommandsBuilder addNowPlayingCmd() {
        addCmd(new NowplayingCmd());
        return this;
    }
    public MusicCommandsBuilder addPlayCmd() {
        addCmd(new PlayCmd());
        return this;
    }
    public MusicCommandsBuilder addPlaylistsCmd() {
        addCmd(new PlaylistsCmd());
        return this;
    }
    public MusicCommandsBuilder addQueueCmd() {
        addCmd(new QueueCmd());
        return this;
    }
    public MusicCommandsBuilder addRemoveCmd() {
        addCmd(new RemoveCmd());
        return this;
    }
    public MusicCommandsBuilder addSCSearchCmd() {
        addCmd(new SCSearchCmd());
        return this;
    }
    public MusicCommandsBuilder addSearchCmd() {
        addCmd(new SearchCmd());
        return this;
    }
    public MusicCommandsBuilder addShuffleCmd() {
        addCmd(new ShuffleCmd());
        return this;
    }
    public MusicCommandsBuilder addSkipCmd() {
        addCmd(new SkipCmd());
        return this;
    }

    private void addCmd(MusicCommand cmd)
    {
        createdCommands.add(initializeCmd(cmd, this.argument));
    }
    private MusicCommand initializeCmd(MusicCommand cmd, Argument<MusicCommandArgument> argument) {
        cmd.Initialize(argument.GetArgument());
        return cmd;
    }
}


