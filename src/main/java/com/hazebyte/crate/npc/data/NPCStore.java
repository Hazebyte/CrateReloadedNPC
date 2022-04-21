package com.hazebyte.crate.npc.data;

import com.hazebyte.crate.npc.registrar.NPCRegistrar;

import java.util.concurrent.CompletableFuture;

public interface NPCStore {

    CompletableFuture<NPCRegistrar> load();

    CompletableFuture<Void> save(NPCRegistrar npcRegistrar);
}
