package ca.xef5000.playermodelapi.api;

import org.bukkit.entity.Player;

public interface DataManager {

    void loadAttachmentsFor(Player player);

    void saveAttachmentData(Player player, AttachmentData newData);

    void clearAttachmentData(Player player);
}
