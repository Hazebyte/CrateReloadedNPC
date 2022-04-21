package com.hazebyte.crate.npc.commands.resolver;

import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.contexts.ContextResolver;
import com.hazebyte.crate.api.CrateAPI;
import com.hazebyte.crate.api.crate.Crate;

public class CrateResolver implements ContextResolver<Crate, BukkitCommandExecutionContext> {

    @Override
    public Crate getContext(BukkitCommandExecutionContext context) throws InvalidCommandArgument {
        final String search = context.popFirstArg();

        Crate crate = CrateAPI.getInstance().getCrateRegistrar().getCrate(search);

        if (crate == null)
            throw new InvalidCommandArgument(String.format("Not crate matching %s.", search));

        return crate;
    }

}
