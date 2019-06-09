package com.jagrosh.jmusicbot.commands.commandbuilder.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jmusicbot.commands.DJCommand;
import com.jagrosh.jmusicbot.commands.MusicCommand;
import com.jagrosh.jmusicbot.commands.MusicCommandArgument;
import com.jagrosh.jmusicbot.commands.commandbuilder.CommandsBuilder;
import com.jagrosh.jmusicbot.commands.dj.*;
import com.jagrosh.jmusicbot.commands.music.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MusicCommandsBuilder extends CommandsBuilder<Command, MusicCommandArgument> {

    public enum CATEGORY {
        NORMAL              ,
        DJ
    }

    private Argument<MusicCommandArgument> argument;
    private final List<Command> createdCommands;

    public MusicCommandsBuilder(MusicCommandArgument argument)
    {
        initialize(new Argument<MusicCommandArgument>(argument));
        createdCommands = new ArrayList<>();
    }

    @Override
    protected void initialize(Argument<MusicCommandArgument> argument) {
        this.argument = argument;
    }

    @Override
    public Collection<Command> build() {
        Collection<Command> commands = new ArrayList<>(createdCommands);
        createdCommands.clear();

        return commands;
    }

    public MusicCommandsBuilder setEmogi(String emogi) {
        this.argument = new Argument<MusicCommandArgument>(new MusicCommandArgument(this.argument.getArgument().getPlayers(), emogi));
        return this;
    }

    // Add Commands
    public MusicCommandsBuilder addLyricsCmd() {
        addCmd(CATEGORY.NORMAL, new LyricsCmd());
        return this;
    }
    public MusicCommandsBuilder addNowPlayingCmd() {
        addCmd(CATEGORY.NORMAL, new NowplayingCmd());
        return this;
    }
    public MusicCommandsBuilder addPlayCmd() {
        addCmd(CATEGORY.NORMAL, new PlayCmd());
        return this;
    }
    public MusicCommandsBuilder addPlaylistsCmd() {
        addCmd(CATEGORY.NORMAL, new PlaylistsCmd());
        return this;
    }
    public MusicCommandsBuilder addQueueCmd() {
        addCmd(CATEGORY.NORMAL, new QueueCmd());
        return this;
    }
    public MusicCommandsBuilder addRemoveCmd() {
        addCmd(CATEGORY.NORMAL, new RemoveCmd());
        return this;
    }
    public MusicCommandsBuilder addSCSearchCmd() {
        addCmd(CATEGORY.NORMAL, new SCSearchCmd());
        return this;
    }
    public MusicCommandsBuilder addSearchCmd() {
        addCmd(CATEGORY.NORMAL, new SearchCmd());
        return this;
    }
    public MusicCommandsBuilder addShuffleCmd() {
        addCmd(CATEGORY.NORMAL, new ShuffleCmd());
        return this;
    }
    public MusicCommandsBuilder addSkipCmd() {
        addCmd(CATEGORY.NORMAL, new SkipCmd());
        return this;
    }

    public MusicCommandsBuilder addForceskipCmd() {
        addCmd(CATEGORY.DJ, new ForceskipCmd());
        return this;
    }
    public MusicCommandsBuilder addMoveTrackCmd() {
        addCmd(CATEGORY.DJ, new MoveTrackCmd());
        return this;
    }
    public MusicCommandsBuilder addPauseCmd() {
        addCmd(CATEGORY.DJ, new PauseCmd());
        return this;
    }
    public MusicCommandsBuilder addPlaynextCmd() {
        addCmd(CATEGORY.DJ, new PlaynextCmd());
        return this;
    }
    public MusicCommandsBuilder addRepeatCmd() {
        addCmd(CATEGORY.DJ, new RepeatCmd());
        return this;
    }
    public MusicCommandsBuilder addSkiptoCmd() {
        addCmd(CATEGORY.DJ, new SkiptoCmd());
        return this;
    }
    public MusicCommandsBuilder addStopCmd() {
        addCmd(CATEGORY.DJ, new StopCmd());
        return this;
    }
    public MusicCommandsBuilder addVolumeCmd() {
        addCmd(CATEGORY.DJ, new VolumeCmd());
        return this;
    }

    private void addCmd(CATEGORY category , MusicCommand cmd)
    {
        switch (category)
        {
            case NORMAL:
                createdCommands.add(initializeCmd(cmd, this.argument));
                break;

            case DJ:
                createdCommands.add(new DJCommand(initializeCmd(cmd, this.argument)));
                break;

            default:
                throw new IllegalArgumentException("Invalid Music Commands Category");
        }
    }
    private MusicCommand initializeCmd(MusicCommand cmd, Argument<MusicCommandArgument> argument) {
        cmd.initialize(argument.getArgument());
        return cmd;
    }
}


