package com.hazebyte.crate.npc.registrar;

import com.hazebyte.crate.api.CrateAPI;
import com.hazebyte.crate.api.crate.Crate;
import com.hazebyte.crate.api.util.Messenger;
import com.hazebyte.crate.npc.events.NPCRegisterEvent;
import com.hazebyte.crate.npc.events.NPCRemoveEvent;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;
import java.util.stream.Collectors;

public class MapNPCRegistrar implements NPCRegistrar, ConfigurationSerializable {

    Map<NPC, List<Crate>> npcToCrates;

    public MapNPCRegistrar() {
        npcToCrates = new HashMap<>();
    }

    public MapNPCRegistrar(Map<String, Object> serialized) throws Exception {
        Messenger.info("Loading NPC Registrar");
        npcToCrates = new HashMap<>();

        for (Map.Entry<String, Object> entry: serialized.entrySet()) {
            if (entry.getKey().equals("==")) {
                continue;
            }

            if (!(entry.getValue() instanceof Collection)) {
                throw new Exception("Unexpected datatype when deserializing");
            }

            String npcUUID = entry.getKey();
            List<String> crateNames = (List<String>) entry.getValue();

            NPC npc = CitizensAPI.getNPCRegistry().getByUniqueId(UUID.fromString(npcUUID));
            if (npc == null) {
                Messenger.info(String.format("Unable to find NPC with id: %s", npcUUID));
            }
            List<Crate> crates = crateNames.stream()
                    .map((crate) -> CrateAPI.getCrateRegistrar().getCrate(crate))
                    .collect(Collectors.toList());
            npcToCrates.put(npc, crates);
        }
    }

    @Override
    public void register(NPC npc, Crate crate) {
        NPCRegisterEvent toCall = new NPCRegisterEvent(npc, crate);
        Bukkit.getServer().getPluginManager().callEvent(toCall);

        if (toCall.isCancelled()) {
            return;
        }

        if (!npcToCrates.containsKey(npc)) {
            npcToCrates.put(npc, new ArrayList<>());
        }

        npcToCrates.get(npc).add(crate);
    }

    @Override
    public boolean remove(NPC npc, Crate crate) {
        NPCRemoveEvent toCall = new NPCRemoveEvent(npc, crate);
        Bukkit.getServer().getPluginManager().callEvent(toCall);

        if (toCall.isCancelled()) {
            return false;
        }

        if (!npcToCrates.containsKey(npc)) {
            return false;
        }

        boolean status = npcToCrates.get(npc).remove(crate);
        if (npcToCrates.get(npc).isEmpty()) {
            npcToCrates.remove(npc);
        }
        return status;
    }

    @Override
    public boolean isRegistered(NPC npc) {
        if (!npcToCrates.containsKey(npc)) {
            return false;
        }

        return !npcToCrates.get(npc).isEmpty();
    }

    @Override
    public boolean isRegistered(NPC npc, Crate crate) {
        if (!isRegistered(npc)) {
            return false;
        }

        return npcToCrates.get(npc).contains(crate);
    }

    @Override
    public Collection<Crate> getAll(NPC npc) {
        return npcToCrates.get(npc);
    }

    @Override
    public Map<NPC, List<Crate>> getAll() {
        return npcToCrates;
    }

    public static MapNPCRegistrar valueOf(Map<String, Object> serialized) throws Exception {
        return new MapNPCRegistrar(serialized);
    }

    public static MapNPCRegistrar deserialize(Map<String, Object> serialized) throws Exception {
        return new MapNPCRegistrar(serialized);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<>();
        for (Map.Entry<NPC, List<Crate>> entry: npcToCrates.entrySet()) {
            List<String> crates = entry.getValue().stream()
                    .map(Crate::getCrateName)
                    .collect(Collectors.toList());
            serialized.put(entry.getKey().getUniqueId().toString(), crates);
        }
        return serialized;
    }
}
