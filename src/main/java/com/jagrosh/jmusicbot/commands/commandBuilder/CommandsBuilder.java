package com.jagrosh.jmusicbot.commands.commandBuilder;

import com.jagrosh.jdautilities.command.Command;

import java.util.Collection;


public abstract class CommandsBuilder<CommandToBuild extends Command, ArgumentType> {

    protected abstract void initialize(Argument<ArgumentType> argument);
    public abstract Collection<Command> Build();

    public class Argument <ArgumentType>
    {
        private final ArgumentType argument;
        public ArgumentType GetArgument()
        {
            return this.argument;
        }

        public Argument(ArgumentType argument)
        {
            this.argument = argument;
        }
    }
}