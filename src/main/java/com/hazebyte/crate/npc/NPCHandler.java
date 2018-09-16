package com.hazebyte.crate.npc;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.hazebyte.crate.api.CrateAPI;
import com.hazebyte.crate.api.crate.Crate;
import com.hazebyte.crate.api.util.Messenger;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

public class NPCHandler {

    private JavaPlugin plugin;
    private Multimap<Integer, Crate> crateMapping;
    private static final String BASE = "npc";

    public NPCHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        this.crateMapping = HashMultimap.create();
    }

    public void destroy() {
        crateMapping.clear();
        plugin = null;
        crateMapping = null;
    }

    public void parse() {
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection(BASE);
        if (section == null) {
            return;
        }

        Set<String> keys = section.getKeys(false);
        for (String key: keys) {
            int id = 0;
            try {
                id = Integer.parseInt(key);
            } catch (NumberFormatException exc) {
                Messenger.info(String.format("Saved NPC is invalid. Key: %s", key));
                config.set(key, null);
                continue;
            }

            NPC npc = CitizensAPI.getNPCRegistry().getById(id);
            if (npc == null) {
                // Did not find NPC by ID. NPC was removed (?)
                Messenger.info(String.format("Did not find the NPC by ID: %s", key));
                continue;
            }

            List<String> crateNames = config.getStringList(String.format("%s.%s", BASE, key));
            for (String name: crateNames) {
                Crate crate = CrateAPI.getCrateRegistrar().getCrate(name);
                if (crate != null) {
                    register(npc, crate);
                }
            }
        }
    }

    public Map<String, Object> serialize() {
        Map<String, Object> serialize = new LinkedHashMap<>();
        for (Integer integer: crateMapping.keySet()) {
            List<String> names = crateMapping.get(integer).stream()
                    .filter(crate -> crate != null)
                    .map(crate -> crate.getCrateName())
                    .collect(Collectors.toList());
            serialize.put(Integer.toString(integer), names);
        }
        return serialize;
    }

    public boolean register(NPC npc, Crate crate) {
        Integer id = npc.getId();
        if (!(crateMapping.containsEntry(id, crate))) {
            crateMapping.put(id, crate);
            save();
            return true;
        }
        return false;
    }

    public boolean deregister(NPC npc, Crate crate) {
        Integer id = npc.getId();
        boolean status = crateMapping.remove(id, crate);
        if (status) {
            save();
        }
        return status;
    }

    public void save() {
        plugin.getConfig().set(BASE, serialize());
        plugin.saveConfig();
    }

    public boolean isRegistered(NPC npc) {
        boolean bool = crateMapping.containsKey(npc.getId());
        return bool;
    }

    public boolean isRegistered(NPC npc, Crate crate) {
        boolean bool = crateMapping.containsEntry(npc.getId(), crate);
        return bool;
    }

    public Collection<NPC> getNPCs() {
        return crateMapping.keys().stream()
                .map(id -> CitizensAPI.getNPCRegistry().getById(id))
                .filter(npc -> npc != null)
                .collect(Collectors.toList());
    }

    public Collection<Crate> getCrates(NPC npc) {
        return crateMapping.get(npc.getId());
    }

    public Crate getCrate(NPC npc, String crateName) {
        Optional<Crate> optional = getCrates(npc).stream()
                .filter(current -> current.getCrateName().equals(crateName))
                .findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }
}
