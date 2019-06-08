package com.jagrosh.jmusicbot.commands.commandBuilder.Builder;

import com.jagrosh.jdautilities.command.Command;


public abstract class CommandBuilder <CommandToBuild extends Command, ArgumentType> {

    protected abstract void Initialize(Argument<ArgumentType> argument);
    public abstract Command[] Build();
    
    public abstract class Argument <ArgumentType>
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