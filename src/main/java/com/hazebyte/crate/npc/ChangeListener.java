package com.hazebyte.crate.npc;

import net.citizensnpcs.api.event.CitizensEnableEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChangeListener implements Listener {

    private NPCHandler handler;

    public ChangeListener(CorePlugin plugin) {
        this.handler = plugin.getNpcHandler();
    }

    @EventHandler
    public void onReady(CitizensEnableEvent event) {
        handler.parse();
    }

}
