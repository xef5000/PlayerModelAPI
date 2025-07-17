package ca.xef5000.playermodelapi.api;

import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.joml.Vector3f;

public interface Attachment {

    ItemDisplay getDisplayEntity();
    AttachmentPoint getAttachmentPoint();
    Vector3f getBaseTranslation();
    Vector3f getBaseRotation();
    void update(Player player);
}
