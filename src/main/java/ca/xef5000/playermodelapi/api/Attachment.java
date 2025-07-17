package ca.xef5000.playermodelapi.api;

import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface Attachment {

    ItemDisplay getDisplayEntity();
    AttachmentPoint getAttachmentPoint();
    Vector3f getBaseTranslation();
    Quaternionf getBaseRotation();
    void update(Player player);
}
