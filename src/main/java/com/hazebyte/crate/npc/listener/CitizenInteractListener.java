package com.hazebyte.crate.npc.listener;

import com.hazebyte.crate.api.CrateAPI;
import com.hazebyte.crate.api.crate.Crate;
import com.hazebyte.crate.api.crate.CrateAction;
import com.hazebyte.crate.api.util.Messenger;
import com.hazebyte.crate.npc.CorePlugin;
import com.hazebyte.crate.npc.events.NPCCrateClickEvent;
import com.hazebyte.crate.npc.util.PlayerUtil;
import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.stream.Collectors;

public class CitizenInteractListener implements Listener {

    private CorePlugin plugin;

    public CitizenInteractListener(CorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(NPCRightClickEvent event) {
        NPC npc = event.getNPC();
        Player player = event.getClicker();
        ItemStack item = PlayerUtil.getItemInMainHand(player);
        Crate seekingToOpen = CrateAPI.getCrateRegistrar().getCrate(item);

        if (!plugin.getNPCRegistrar().isRegistered(npc, seekingToOpen)) {
            return;
        }

        NPCCrateClickEvent eventCall = new NPCCrateClickEvent(event.getNPC(), event.getClicker(), seekingToOpen, CrateAction.OPEN);
        Bukkit.getServer().getPluginManager().callEvent(eventCall);
        if (eventCall.isCancelled()) {
            return;
        }

        Collection<Crate> crates = plugin.getNPCRegistrar().getAll(event.getNPC());
        if (crates.contains(seekingToOpen)) {
            CrateAPI.getCrateRegistrar().tryOpen(seekingToOpen, player, event.getNPC().getStoredLocation(), item);
        } else {
            Messenger.tell(player, CrateAPI.getMessage("core.invalid_action"));
        }
    }

    @EventHandler
    public void onLeftClick(NPCLeftClickEvent event) {
        NPC npc = event.getNPC();
        if (!plugin.getNPCRegistrar().isRegistered(npc)) {
            return;
        }

        Player player = event.getClicker();
        ItemStack item = PlayerUtil.getItemInMainHand(player);
        Crate seekingToOpen = CrateAPI.getCrateRegistrar().getCrate(item);

        if (!plugin.getNPCRegistrar().isRegistered(npc)) {
            return;
        }

        NPCCrateClickEvent eventCall = new NPCCrateClickEvent(event.getNPC(), event.getClicker(), seekingToOpen, CrateAction.PREVIEW);
        Bukkit.getServer().getPluginManager().callEvent(eventCall);
        if (eventCall.isCancelled()) {
            return;
        }

        Collection<Crate> crates = plugin.getNPCRegistrar().getAll(event.getNPC());
        if (crates.contains(seekingToOpen)) {
            CrateAPI.getCrateRegistrar().preview(seekingToOpen, player);
        } else {
            CrateAPI.getCrateRegistrar().previewAll(crates.stream().collect(Collectors.toList()), player);
        }
    }

}
