package com.hazebyte.crate.npc;

import co.aikar.commands.BukkitCommandManager;
import com.hazebyte.crate.api.crate.Crate;
import com.hazebyte.crate.api.util.Messenger;
import com.hazebyte.crate.npc.commands.CrateCommand;
import com.hazebyte.crate.npc.commands.completion.CrateCompletion;
import com.hazebyte.crate.npc.commands.completion.ExistingNPCCompletion;
import com.hazebyte.crate.npc.commands.completion.NPCCompletion;
import com.hazebyte.crate.npc.commands.resolver.CrateResolver;
import com.hazebyte.crate.npc.commands.resolver.NPCResolver;
import com.hazebyte.crate.npc.data.NPCStore;
import com.hazebyte.crate.npc.data.YamlNPCStore;
import com.hazebyte.crate.npc.listener.CitizenInteractListener;
import com.hazebyte.crate.npc.listener.RegistrarChangeListener;
import com.hazebyte.crate.npc.registrar.MapNPCRegistrar;
import com.hazebyte.crate.npc.registrar.NPCRegistrar;
import net.citizensnpcs.api.event.CitizensEnableEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class CorePlugin extends JavaPlugin implements Listener {

    private NPCRegistrar npcRegistrar;
    private NPCStore npcStore;
    private BukkitCommandManager commandManager;

    public NPCRegistrar getNPCRegistrar() {
        return npcRegistrar;
    }

    public NPCStore getNPCStore() {
        return npcStore;
    }

    private void registerCommands() {
        commandManager = new BukkitCommandManager(this);
        commandManager.enableUnstableAPI("help");
        commandManager.getCommandContexts().registerContext(Crate.class, new CrateResolver());
        commandManager.getCommandContexts().registerContext(NPC.class, new NPCResolver());
        commandManager.getCommandCompletions().registerCompletion("crate", new CrateCompletion());
        commandManager.getCommandCompletions().registerCompletion("npc", new NPCCompletion());
        commandManager.getCommandCompletions().registerCompletion("existing-npc", new ExistingNPCCompletion(this));
        commandManager.registerCommand(new CrateCommand(this));
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getPluginManager().registerEvents(new CitizenInteractListener(this), this);
        this.getServer().getPluginManager().registerEvents(new RegistrarChangeListener(this), this);
    }

    @EventHandler
    public void onCitizensLoad(CitizensEnableEvent event) {
        Messenger.info("Citizens Hook");
        npcStore = new YamlNPCStore(this);
        npcStore.load().thenAccept(npcRegistrar -> {
            if (npcRegistrar == null) {
                this.npcRegistrar = new MapNPCRegistrar();
            } else {
                this.npcRegistrar = npcRegistrar;
            }
        });
    }

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(MapNPCRegistrar.class);
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        npcRegistrar = null;
        commandManager = null;
    }

}
