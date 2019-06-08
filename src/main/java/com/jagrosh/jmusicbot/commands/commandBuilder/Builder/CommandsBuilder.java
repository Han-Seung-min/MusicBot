package com.jagrosh.jmusicbot.commands.commandBuilder.Builder;

import com.jagrosh.jdautilities.command.Command;

import java.util.Collection;


public abstract class CommandsBuilder<CommandToBuild extends Command, ArgumentType> {

    protected abstract void Initialize(Argument<ArgumentType> argument);
    public abstract Collection<Command> Build();

    public class Argument <ArgumentType>
    {
        public Argument(ArgumentType argument)
        {
            this.argument = argument;
        }

        private final ArgumentType argument;
        public ArgumentType GetArgument()
        {
            return this.argument;
        }
    }
}