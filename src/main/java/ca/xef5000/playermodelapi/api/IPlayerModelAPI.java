package ca.xef5000.playermodelapi.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.joml.Vector3f;

import java.util.List;

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
    void attach(Player player, ItemStack item, AttachmentPoint point, Vector3f offsetTranslation, Vector3f offsetRotation);

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
}
