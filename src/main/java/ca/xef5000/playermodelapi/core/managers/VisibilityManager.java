package ca.xef5000.playermodelapi.core.managers;

import ca.xef5000.playermodelapi.PlayerModelAPI;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class VisibilityManager  {

    public static void hideEntity(Entity display) {
        PlayerModelAPI plugin = PlayerModelAPI.getInstance();
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            player.hideEntity(plugin, display);
        }
    }

    public static void hideEntityTo(Entity display, Player player) {
        PlayerModelAPI plugin = PlayerModelAPI.getInstance();
        player.hideEntity(plugin, display);
    }

    public static void showEntity(Entity display) {
        PlayerModelAPI plugin = PlayerModelAPI.getInstance();
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            player.showEntity(plugin, display);
        }
    }

    public static void showEntityTo(Entity display, Player player) {
        PlayerModelAPI plugin = PlayerModelAPI.getInstance();
        player.showEntity(plugin, display);
    }

}
