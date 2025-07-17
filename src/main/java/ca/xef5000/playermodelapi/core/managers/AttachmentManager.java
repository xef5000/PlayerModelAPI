package ca.xef5000.playermodelapi.core.managers;

import ca.xef5000.playermodelapi.PlayerModelAPI;
import ca.xef5000.playermodelapi.api.Attachment;
import ca.xef5000.playermodelapi.api.AttachmentData;
import ca.xef5000.playermodelapi.api.AttachmentPoint;
import ca.xef5000.playermodelapi.api.IPlayerModelAPI;
import ca.xef5000.playermodelapi.core.models.AttachmentImpl;
import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AttachmentManager implements IPlayerModelAPI {

    private final PlayerModelAPI plugin;

    //private final DataManager dataManager;
    private final Map<UUID, List<AttachmentImpl>> activeAttachments = new ConcurrentHashMap<>();

    public AttachmentManager(PlayerModelAPI plugin) {
        this.plugin = plugin;
    }

    @Override
    public void attach(Player player, ItemStack item, AttachmentPoint point, Vector3f offsetTranslation, Vector3f offsetRotation, Vector3f scale) {
        // 1. Spawn the ItemDisplay entity
        Location spawnLocation = player.getEyeLocation();
        spawnLocation.setYaw(180f);
        spawnLocation.setPitch(0);
        ItemDisplay display = player.getWorld().spawn(spawnLocation, ItemDisplay.class, (e) -> {
            e.setItemStack(item);
            e.setInvulnerable(true);
            e.setPersistent(false); // Don't save it to disk
            e.setInterpolationDuration(5); // Smooths movement over 1 tick
            e.setInterpolationDelay(-1); // Start smoothing immediately
        });

        // 2. Make it a passenger for perfect positional sync
        player.addPassenger(display);

        // 3. Create and store our attachment object
        AttachmentImpl attachment = new AttachmentImpl(display, point, offsetTranslation, offsetRotation, scale);
        activeAttachments.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>()).add(attachment);

        // 4. Set its initial transformation
        attachment.update(player);

        AttachmentData data = new AttachmentData(item, point, offsetTranslation, offsetRotation, scale);
        plugin.getDataManager().saveAttachmentData(player, data);
    }

    @Override
    public void removeAllAttachments(Player player) {
        List<AttachmentImpl> attachments = activeAttachments.remove(player.getUniqueId());
        if (attachments != null) {
            for (AttachmentImpl attachment : attachments) {
                attachment.getDisplayEntity().remove();
            }
        }
    }

    @Override
    public Map<UUID, List<AttachmentImpl>> getActiveAttachments() {
        return activeAttachments;
    }

    @Override
    public List<Attachment> getAttachments(Player player) {
        List<AttachmentImpl> attachments = activeAttachments.get(player.getUniqueId());
        return attachments == null ? List.of() : new ArrayList<>(attachments);
    }


}
