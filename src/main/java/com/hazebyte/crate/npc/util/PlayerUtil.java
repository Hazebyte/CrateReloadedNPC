package com.hazebyte.crate.npc.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PlayerUtil {

    private static Map<String, Class<?>> classCache = new HashMap<>();
    private static Map<String, Method> methodCache = new HashMap<>();

    static {
        classCache.put("PlayerInventory", PlayerInventory.class);

        try {
            methodCache.put("getItemInMainHand", classCache.get("PlayerInventory").getMethod("getItemInMainHand"));
        } catch (NoSuchMethodException ignored) {}
    }

    public static ItemStack getItemInMainHand(Player player) {
        try {
            return player.getItemInHand();
        } catch (Exception ex) {
            Method method = methodCache.get("getItemInMainHand");
            Object object = null;
            try {
                object = method.invoke(player.getInventory());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return (ItemStack) object;
        }
    }
}
