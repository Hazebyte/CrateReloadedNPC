package com.hazebyte.crate.npc.events;

import com.hazebyte.crate.api.crate.Crate;
import com.hazebyte.crate.api.crate.CrateAction;
import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a player interacts with a crate.
 */
public class NPCCrateClickEvent extends NPCClickEvent implements Cancellable {
    
    private static HandlerList handlers = new HandlerList();
    protected Crate crate;
    protected CrateAction action;
    private boolean cancelled;

    public NPCCrateClickEvent(NPC npc, Player clicker, Crate crate, CrateAction action) {
        super(npc, clicker);
        this.crate = crate;
        this.action = action;
    }

    public Crate getCrate() {
        return crate;
    }

    public CrateAction getAction() {
        return action;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
