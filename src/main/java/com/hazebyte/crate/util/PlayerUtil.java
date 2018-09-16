package com.hazebyte.crate.util;

import com.hazebyte.crate.api.util.ReflectionUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.lang.reflect.Method;

public class PlayerUtil {

    public static ItemStack getItemInMainHand(Player player) {
        try {
            Method method = ReflectionUtils.getMethod(PlayerInventory.class, "getItemInMainHand");
            Object object = method.invoke(player.getInventory());
            if (object instanceof ItemStack) {
                return (ItemStack) object;
            }
        } catch (Exception e) {
        }
        return player.getItemInHand();
    }
}
