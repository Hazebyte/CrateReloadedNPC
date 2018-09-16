package com.hazebyte.crate.npc;

import com.hazebyte.crate.api.crate.Crate;
import com.hazebyte.crate.api.crate.CrateAction;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a player interacts with a crate.
 */
public class NPCCrateEvent extends Event implements Cancellable {
    
    private static HandlerList handlers = new HandlerList();
    protected Player player;
    protected Crate crate;
    protected NPC npc;
    protected CrateAction action;
    private boolean cancelled;

    public NPCCrateEvent(Crate crate, Player player, NPC npc, CrateAction action) {
        this.crate = crate;
        this.player = player;
        this.npc = npc;
        this.action = action;
    }

    public Player getPlayer() {
        return player;
    }
    
    public Crate getCrate() {
        return crate;
    }

    public NPC getNPC() {
        return npc;
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
