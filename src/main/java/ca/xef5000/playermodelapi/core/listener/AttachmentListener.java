package ca.xef5000.playermodelapi.core.listener;

import ca.xef5000.playermodelapi.PlayerModelAPI;
import ca.xef5000.playermodelapi.api.Attachment;
import ca.xef5000.playermodelapi.api.DataManager;
import ca.xef5000.playermodelapi.core.models.AttachmentImpl;
import ca.xef5000.playermodelapi.api.IPlayerModelAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class AttachmentListener implements Listener {

    private final IPlayerModelAPI api;
    private final DataManager dataManager;
    private final PlayerModelAPI pluginInstance;

    public AttachmentListener(IPlayerModelAPI api, DataManager dataManager, PlayerModelAPI pluginInstance) {
        this.api = api;
        this.dataManager = dataManager;
        this.pluginInstance = pluginInstance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Use a slight delay to ensure the player is fully loaded into the world
        new BukkitRunnable() {
            @Override
            public void run() {
                dataManager.loadAttachmentsFor(event.getPlayer());
            }
        }.runTaskLater(pluginInstance, 1L); // 1 tick delay
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        api.removeAllAttachments(event.getPlayer());
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.hasChangedOrientation()) {
            List<Attachment> attachments = api.getAttachments(event.getPlayer());
            if (attachments != null) {
                for (Attachment attachment : attachments) {
                    attachment.update(event.getPlayer());
                }
            }
        }
    }


}
