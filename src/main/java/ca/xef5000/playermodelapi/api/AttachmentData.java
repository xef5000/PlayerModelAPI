package ca.xef5000.playermodelapi.api;

import org.bukkit.inventory.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.io.Serializable;

public record AttachmentData(ItemStack item,
                             AttachmentPoint point,
                             Vector3f offsetTranslation,
                             Vector3f offsetRotation,
                             Vector3f scale) implements Serializable {
}
