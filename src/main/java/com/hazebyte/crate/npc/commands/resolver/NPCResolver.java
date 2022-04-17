package com.hazebyte.crate.npc.commands.resolver;

import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.contexts.ContextResolver;
import com.hazebyte.crate.api.CrateAPI;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class NPCResolver implements ContextResolver<NPC, BukkitCommandExecutionContext> {

    @Override
    public NPC getContext(BukkitCommandExecutionContext context) throws InvalidCommandArgument {
        final String search = context.popFirstArg();

        // \\d+  one or more digit
        if (!search.matches("\\d+")) {
            throw new InvalidCommandArgument(String.format("Expected a npc id, received: %s", search));
        }

        int num = Integer.parseInt(search);
        NPC npc = CitizensAPI.getNPCRegistry().getById(num);

        if (npc == null) {
            throw new InvalidCommandArgument(CrateAPI.getMessage("core.invalid_npc_id").replace("{number}", search));
        }

        return npc;
    }
}
