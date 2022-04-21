package com.hazebyte.crate.npc.commands.completion;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.InvalidCommandArgument;
import net.citizensnpcs.api.CitizensAPI;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class NPCCompletion implements CommandCompletions.CommandCompletionHandler<BukkitCommandCompletionContext> {

    @Override
    public Collection<String> getCompletions(BukkitCommandCompletionContext context) throws InvalidCommandArgument {
        return StreamSupport.stream(CitizensAPI.getNPCRegistry().spliterator(), false)
                .map(npc -> Integer.toString(npc.getId()))
                .collect(Collectors.toList());
    }
}
