package com.hazebyte.crate.npc.commands.completion;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.InvalidCommandArgument;
import com.hazebyte.crate.api.CrateAPI;
import com.hazebyte.crate.api.crate.Crate;

import java.util.Collection;
import java.util.stream.Collectors;

public class CrateCompletion implements CommandCompletions.CommandCompletionHandler<BukkitCommandCompletionContext> {

    @Override
    public Collection<String> getCompletions(BukkitCommandCompletionContext context) throws InvalidCommandArgument {
        return CrateAPI.getInstance()
                .getCrateRegistrar()
                .getCrates()
                .stream()
                .map((Crate::getCrateName))
                .collect(Collectors.toList());
    }

}
