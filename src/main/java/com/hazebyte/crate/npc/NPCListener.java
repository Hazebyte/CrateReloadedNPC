package com.hazebyte.crate.npc;

import com.hazebyte.crate.api.CrateAPI;
import com.hazebyte.crate.api.crate.Crate;
import com.hazebyte.crate.api.crate.CrateAction;
import com.hazebyte.crate.api.util.Messenger;
import com.hazebyte.crate.util.MoreObjects;
import com.hazebyte.crate.util.PlayerUtil;
import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NPCListener implements Listener {

    private NPCHandler handler;
    public NPCListener(CorePlugin plugin) {
        this.handler = plugin.getNpcHandler();
    }

    @EventHandler
    public void onRightClick(NPCRightClickEvent event) {
        onClick(event, CrateAction.OPEN);
    }

    @EventHandler
    public void onLeftClick(NPCLeftClickEvent event) {
        onClick(event, CrateAction.PREVIEW);
    }

    private void onClick(NPCClickEvent event, CrateAction action) {
        NPC npc = event.getNPC();
        if (handler.isRegistered(npc)) {
            Player player = event.getClicker();
            ItemStack item = PlayerUtil.getItemInMainHand(player);
            Crate seeking = CrateAPI.getCrateRegistrar().getCrate(item);
            Collection<Crate> collection = handler.getCrates(npc);

            NPCCrateEvent call = new NPCCrateEvent(seeking, player, npc, action);
            Bukkit.getServer().getPluginManager().callEvent(call);

            if (call.isCancelled()) {
                return;
            }

            // PREVIEW
            if (!(collection.isEmpty()) && action == CrateAction.PREVIEW) {
                List<Crate> crates = new ArrayList<>(collection);
                if (collection.contains(seeking) || collection.size() <= 1) {
                    CrateAPI.getCrateRegistrar().preview(MoreObjects.firstNonNull(seeking, crates.get(0)), player);
                } else {
                    CrateAPI.getCrateRegistrar().previewAll(crates, player);
                }
                return;
            }

            // OPEN
            if (handler.isRegistered(npc, seeking) && action == CrateAction.OPEN) {
                CrateAPI.getCrateRegistrar().tryOpen(seeking, player, npc.getEntity().getLocation());
            } else {
                // todo pushback
                Messenger.tell(player, CrateAPI.getMessage("core.invalid_action"));
            }
        }
    }
}
