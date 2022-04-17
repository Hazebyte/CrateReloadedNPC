package com.hazebyte.crate.npc.data;

import com.hazebyte.crate.npc.CorePlugin;
import com.hazebyte.crate.npc.registrar.NPCRegistrar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class YamlNPCStore implements NPCStore {

    private FileConfiguration configuration;
    private CorePlugin plugin;
    private final String ROOT_KEY = "data";
    private File file;
    private String fileName = "store.yml";

    public YamlNPCStore(CorePlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), fileName);
        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public CompletableFuture<NPCRegistrar> load() {
        NPCRegistrar registrar = (NPCRegistrar) this.configuration.get(ROOT_KEY);
        return CompletableFuture.completedFuture(registrar);
    }

    @Override
    public CompletableFuture<Void> save(NPCRegistrar npcRegistrar) {
        return CompletableFuture.runAsync(() -> {
            this.configuration.set(ROOT_KEY, npcRegistrar);
            try {
                this.configuration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
