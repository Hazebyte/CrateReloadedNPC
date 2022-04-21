package com.hazebyte.crate.npc.commands.completion;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.InvalidCommandArgument;
import com.hazebyte.crate.api.crate.Crate;
import com.hazebyte.crate.npc.CorePlugin;
import net.citizensnpcs.api.npc.NPC;

import java.util.Collection;
import java.util.stream.Collectors;

public class ExistingNPCCompletion implements CommandCompletions.CommandCompletionHandler<BukkitCommandCompletionContext> {

    private CorePlugin plugin;

    public ExistingNPCCompletion(CorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Collection<String> getCompletions(BukkitCommandCompletionContext context) throws InvalidCommandArgument {
        return plugin.getNPCRegistrar()
                .getAll()
                .keySet()
                .stream()
                .map(npc -> Integer.toString(npc.getId())).collect(Collectors.toList());
    }
}
