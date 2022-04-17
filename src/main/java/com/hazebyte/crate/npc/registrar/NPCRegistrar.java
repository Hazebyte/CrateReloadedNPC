package com.hazebyte.crate.npc.registrar;

import com.hazebyte.crate.api.crate.Crate;
import net.citizensnpcs.api.npc.NPC;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface NPCRegistrar {

    void register(NPC npc, Crate crate);

    boolean remove(NPC npc, Crate crate);

    boolean isRegistered(NPC npc);

    boolean isRegistered(NPC npc, Crate crate);

    Collection<Crate> getAll(NPC npc);

    Map<NPC, List<Crate>> getAll();
}
