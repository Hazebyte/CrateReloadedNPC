package com.hazebyte.crate.npc;

import co.aikar.commands.BukkitCommandManager;
import com.hazebyte.crate.api.CrateAPI;
import com.hazebyte.crate.api.util.Messenger;
import com.hazebyte.crate.npc.commands.Commands;
import com.hazebyte.crate.npc.commands.CrateResolver;
import com.hazebyte.crate.npc.commands.NPCResolver;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CorePlugin extends JavaPlugin {

    private NPCHandler npcHandler;
    private BukkitCommandManager commandManager;

    public NPCHandler getNpcHandler() {
        return npcHandler;
    }

    private void registerCommands() {
        commandManager = new BukkitCommandManager(this);
        commandManager.enableUnstableAPI("help");
        commandManager.getCommandContexts().registerContext(CrateResolver.class, CrateResolver.getContextResolver());
        commandManager.getCommandContexts().registerContext(NPCResolver.class, NPCResolver.getContextResolver());
        commandManager.getCommandCompletions().registerCompletion("crate", context -> CrateAPI.getCrateRegistrar()
                    .getCrates()
                    .stream()
                    .map(crate -> crate.getCrateName())
                    .collect(Collectors.toList()));
        commandManager.getCommandCompletions().registerCompletion("npc", context -> StreamSupport.stream(CitizensAPI.getNPCRegistry().spliterator(), false)
                    .map(npc -> Integer.toString(npc.getId()))
                    .collect(Collectors.toList()));
        commandManager.getCommandCompletions().registerCompletion("setnpc", context -> npcHandler.getNPCs()
                    .stream()
                    .map(npc -> Integer.toString(npc.getId()))
                    .collect(Collectors.toList()));
        commandManager.getCommandCompletions().registerCompletion("setcrate", context -> {
//            todo Fix this context value
//            NPCResolver resolver = context.getContextValue(NPCResolver.class);
//            if (resolver != null) {
//                return npcHandler.getCrates(resolver.getValue()).stream()
//                        .map(crate -> crate.getCrateName())
//                        .collect(Collectors.toList());
//            }
            return CrateAPI.getCrateRegistrar()
                    .getCrates()
                    .stream()
                    .map(crate -> crate.getCrateName())
                    .collect(Collectors.toList());
        });
        commandManager.registerCommand(new Commands(this));
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new NPCListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ChangeListener(this), this);
    }

    @Override
    public void onEnable() {
        npcHandler = new NPCHandler(this);
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        this.npcHandler.destroy();
        this.npcHandler = null;
        HandlerList.unregisterAll(this);
    }

}
