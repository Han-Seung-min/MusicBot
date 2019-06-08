package com.jagrosh.jmusicbot.commands.commandBuilder.Builder.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jmusicbot.commands.MusicCommand;
import com.jagrosh.jmusicbot.commands.commandBuilder.Builder.CommandBuilder;
import com.jagrosh.jmusicbot.commands.music.LyricsCmd;
import com.jagrosh.jmusicbot.commands.music.NowplayingCmd;

import java.util.ArrayList;
import java.util.List;

public class MusicCommandBuilder extends CommandBuilder <MusicCommand, MusicCommand.MusicCommandArgument>  {

    protected MusicCommandBuilder(Argument<MusicCommand.MusicCommandArgument> argument)
    {
        Initialize(argument);

        createdCommands = new ArrayList<>();
    }

    private Argument<MusicCommand.MusicCommandArgument> argument;
    private final List<MusicCommand> createdCommands;

    @Override
    protected void Initialize(Argument<MusicCommand.MusicCommandArgument> argument) {
        this.argument = argument;
    }

    @Override
    public Command Build() {
        return createdCommands.toArray();
    }

    public void addLyricsCommand()
    {

    }
    public MusicCommand build(LyricsCmd command, Argument<MusicCommand.MusicCommandArgument> argument)
    {
        return initializeCmd(command, argument);
    }
    public MusicCommand build(NowplayingCmd command, Argument<MusicCommand.MusicCommandArgument> argument)
    {
        return initializeCmd(command, argument);
    }

    private MusicCommand initializeCmd(MusicCommand cmd, Argument<MusicCommand.MusicCommandArgument> argument)
    {
        return cmd.Initialize(argument.GetArgument());
    }
}


