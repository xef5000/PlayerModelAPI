package ca.xef5000.playermodelapi.core.models;

import ca.xef5000.playermodelapi.api.Attachment;
import ca.xef5000.playermodelapi.api.AttachmentPoint;
import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class AttachmentImpl implements Attachment {

    private final ItemDisplay displayEntity;
    private final AttachmentPoint attachmentPoint;

    // Base offsets provided on creation
    private final Vector3f baseTranslation;
    private final Quaternionf baseRotation;

    public AttachmentImpl(ItemDisplay displayEntity, AttachmentPoint attachmentPoint, Vector3f baseTranslation, Quaternionf baseRotation) {
        this.displayEntity = displayEntity;
        this.attachmentPoint = attachmentPoint;
        this.baseTranslation = baseTranslation;
        this.baseRotation = baseRotation;
    }

    @Override
    public void update(Player player) {
        Location playerLocation = player.getEyeLocation();
        float yaw = playerLocation.getYaw();
        float pitch = playerLocation.getPitch();

        // Start with the base rotation offset of the model
        Quaternionf finalRotation = new Quaternionf(this.getBaseRotation());

        // Apply player's rotation based on attachment point
        if (this.getAttachmentPoint() == AttachmentPoint.HEAD) {
            finalRotation.rotateY((float) Math.toRadians(-yaw)).rotateX((float) Math.toRadians(pitch));
        } else if (this.getAttachmentPoint() == AttachmentPoint.BODY) {
            finalRotation.rotateY((float) Math.toRadians(-yaw));
        }

        // For translation, you would apply a similar rotation to the offset vector
        // This part can get complex with trigonometry, let's start with a simple offset
        Vector3f finalTranslation = this.getBaseTranslation();

        // Apply the final transformation to the entity
        Transformation transformation = new Transformation(finalTranslation, finalRotation, new Vector3f(1, 1, 1), new Quaternionf());
        this.getDisplayEntity().setTransformation(transformation);
    }

    // Getters for all fields...
    @Override
    public ItemDisplay getDisplayEntity() { return displayEntity; }

    @Override
    public AttachmentPoint getAttachmentPoint() { return attachmentPoint; }

    @Override
    public Vector3f getBaseTranslation() { return baseTranslation; }

    @Override
    public Quaternionf getBaseRotation() { return baseRotation; }
}
