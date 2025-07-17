package ca.xef5000.playermodelapi.api;

import ca.xef5000.playermodelapi.core.models.AttachmentImpl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IPlayerModelAPI {

    /**
     * Attaches a custom model to a player.
     *
     * @param player           The player to attach the model to.
     * @param item             The ItemStack with CustomModelData representing the model.
     * @param point            The point on the player's body to attach to.
     * @param offsetTranslation The base XYZ offset from the attachment point.
     * @param offsetRotation   The base rotational offset.
     */
    void attach(Player player, ItemStack item, AttachmentPoint point, Vector3f offsetTranslation, Vector3f offsetRotation, Vector3f scale);

    /**
     * Removes all custom attachments from a specific player.
     *
     * @param player The player whose attachments will be removed.
     */
    void removeAllAttachments(Player player);

    /**
     * Gets a list of all active attachments for a player.
     *
     * @param player The player to check.
     * @return A read-only list of attachments, or an empty list if none.
     */
    List<Attachment> getAttachments(Player player);

    Map<UUID, List<AttachmentImpl>> getActiveAttachments();
}
