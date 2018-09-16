package com.hazebyte.crate.npc.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.hazebyte.crate.api.CrateAPI;
import com.hazebyte.crate.api.crate.Crate;
import com.hazebyte.crate.api.util.Messenger;
import com.hazebyte.crate.npc.CorePlugin;
import com.hazebyte.crate.npc.NPCHandler;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.command.CommandSender;

@CommandAlias("cnpc")
public class Commands extends BaseCommand {

    private NPCHandler handler;

    public Commands(CorePlugin plugin) {
        this.handler = plugin.getNpcHandler();
    }

    @Subcommand("set")
    @CommandPermission("cr.npc.set")
    @CommandCompletion("@npc @crate")
    @Syntax("<npc> <crate>")
    @Description("Register a NPC to handle crate interactions")
    public void onNPCSet(CommandSender sender, NPCResolver npcID, CrateResolver crateName) {
        NPC npc = npcID.getValue();
        Crate crate = crateName.getValue();

        if (handler.isRegistered(npc, crate)) {
            Messenger.tell(sender, CrateAPI.getMessage("core.invalid_npc_set"));
        } else {
            handler.register(npc, crate);
            Messenger.tell(sender, CrateAPI.getMessage("core.successful_npc_set"));
        }
    }

    @Subcommand("remove")
    @CommandPermission("cr.npc.remove")
    @CommandCompletion("@setnpc @setcrate")
    @Syntax("<npc> <crate>")
    @Description("Unregister a NPC from registering crate actions")
    public void onNPCRemove(CommandSender sender, NPCResolver npcID, CrateResolver crateName) {
        NPC npc = npcID.getValue();
        Crate crate = crateName.getValue();

        if (handler.isRegistered(npc, crate)) {
            handler.deregister(npc, crate);
            Messenger.tell(sender, CrateAPI.getMessage("core.successful_npc_remove"));
        } else {
            Messenger.tell(sender, CrateAPI.getMessage("core.invalid_npc_remove"));
        }
    }

    @HelpCommand
    public static void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}
