package com.hazebyte.crate.npc.commands;

import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.contexts.ContextResolver;
import com.hazebyte.crate.api.CrateAPI;
import com.hazebyte.crate.api.crate.Crate;

public class CrateResolver {
    private final Crate value;

    public CrateResolver(Crate value) {
        this.value = value;
    }

    public static ContextResolver<CrateResolver, BukkitCommandExecutionContext> getContextResolver() {
        return (c) -> {
            String first = c.popFirstArg();
            Crate crate = CrateAPI.getCrateRegistrar().getCrate(first);

            if (crate == null) {
                String message = CrateAPI.getMessage("core.invalid_crate_name").replace("{name}", first);
                throw new InvalidCommandArgument(message);
            }

            return new CrateResolver(crate);
        };
    }

    public Crate getValue() {
        return value;
    }

}
