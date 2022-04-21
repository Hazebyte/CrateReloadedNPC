package com.hazebyte.crate.npc.events;

import com.hazebyte.crate.api.crate.Crate;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class NPCRegisterEvent extends NPCRegistrarEvent implements Cancellable {
    private static HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private NPC npc;
    private Crate crate;

    public NPCRegisterEvent(NPC npc, Crate crate) {
        this.npc = npc;
        this.crate = crate;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public NPC getNpc() {
        return npc;
    }

    public Crate getCrate() {
        return crate;
    }
}
