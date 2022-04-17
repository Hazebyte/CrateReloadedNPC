package com.hazebyte.crate.npc.listener;

import com.hazebyte.crate.npc.CorePlugin;
import com.hazebyte.crate.npc.events.NPCRegisterEvent;
import com.hazebyte.crate.npc.registrar.NPCRegistrar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RegistrarChangeListener implements Listener {

    private CorePlugin plugin;

    public RegistrarChangeListener(CorePlugin plugin) {
        this.plugin = plugin;
    }

    public void saveConfig() {
        NPCRegistrar registrar = this.plugin.getNPCRegistrar();
        this.plugin.getNPCStore().save(registrar);
    }

    @EventHandler
    public void onNPCRegister(NPCRegisterEvent event) {
        saveConfig();
    }

    @EventHandler
    public void onNPCRemove(NPCRegisterEvent event) {
        saveConfig();
    }
}
