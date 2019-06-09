package com.jagrosh.jmusicbot.commands.commandbuilder;

import com.jagrosh.jdautilities.command.Command;

import java.util.Collection;


public abstract class CommandsBuilder<RETURNTYPE extends Command, ARGUMENT> {

    protected abstract void initialize(Argument<ARGUMENT> argument);
    public abstract Collection<RETURNTYPE> build();


    public class Argument <ARGUMENT>
    {
        private final ARGUMENT argument;
        public ARGUMENT getArgument()
        {
            return this.argument;
        }

        public Argument(ARGUMENT argument)
        {
            this.argument = argument;
        }
    }
}