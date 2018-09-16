package com.hazebyte.crate.npc.commands;

import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.contexts.ContextResolver;
import com.hazebyte.crate.api.CrateAPI;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class NPCResolver {

    private final NPC value;

    public NPCResolver(NPC value) {
        this.value = value;
    }

    public static ContextResolver<NPCResolver, BukkitCommandExecutionContext> getContextResolver() {
        return (c) -> {
            String first = c.popFirstArg();

            Integer integer;
            try {
                integer = Integer.parseInt(first);
            } catch (NumberFormatException exc) {
                throw new InvalidCommandArgument(String.format("Invalid NPC ID: %s", first));
            }

            NPC npc = CitizensAPI.getNPCRegistry().getById(integer);

            if (npc == null) {
                String message = CrateAPI.getMessage("core.invalid_npc_id").replace("{number}", Integer.toString(integer));
                throw new InvalidCommandArgument(message);
            }

            return new NPCResolver(npc);
        };
    }

    public NPC getValue() {
        return value;
    }
}
