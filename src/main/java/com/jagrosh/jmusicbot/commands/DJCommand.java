/*
 * Copyright 2018 John Grosh <john.a.grosh@gmail.com>.
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
package com.jagrosh.jmusicbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.audio.PlayerManager;
import com.jagrosh.jmusicbot.settings.Settings;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public final class DJCommand extends Command implements IMusicCommand
{
    // HACK : DJCommand is used like proxy to avoid inherit MusicCommand.
    public DJCommand(MusicCommand musicCommand)
    {
        super();

        this.musicCommand = musicCommand;

        this.category = new Category("DJ", event -> 
        {
            if(event.getAuthor().getId().equals(event.getClient().getOwnerId()))
                return true;
            if(event.getGuild()==null)
                return true;
            if(event.getMember().hasPermission(Permission.MANAGE_SERVER))
                return true;
            Settings settings = event.getClient().getSettingsFor(event.getGuild());
            Role dj = settings.getRole(event.getGuild());
            return dj!=null && (event.getMember().getRoles().contains(dj) || dj.getIdLong()==event.getGuild().getIdLong());
        });
    }

    private MusicCommand musicCommand;

    @Override
    protected final void execute(CommandEvent event) {
        this.musicCommand.execute(event);
    }

    @Override
    public final void doCommand(CommandEvent event)
    {
        this.musicCommand.doCommand(event);
    }
}
