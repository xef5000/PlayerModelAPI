package ca.xef5000.playermodelapi.core.models;

import ca.xef5000.playermodelapi.api.Attachment;
import ca.xef5000.playermodelapi.api.AttachmentPoint;
import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class AttachmentImpl implements Attachment {

    private final ItemDisplay displayEntity;
    private final AttachmentPoint attachmentPoint;

    // Base offsets provided on creation
    private final Vector3f baseTranslation;
    private final Vector3f baseRotation;
    private final Vector3f scale;

    public AttachmentImpl(ItemDisplay displayEntity, AttachmentPoint attachmentPoint, Vector3f baseTranslation, Vector3f baseRotation, Vector3f scale) {
        this.displayEntity = displayEntity;
        this.attachmentPoint = attachmentPoint;
        this.baseTranslation = baseTranslation;
        this.baseRotation = baseRotation;
        this.scale = scale;
    }

    @Override
    public void update(Player player) {
        Location playerLocation = player.getEyeLocation();
        float yaw = playerLocation.getYaw();
        float pitch = playerLocation.getPitch();

        float rotX = (float) Math.toRadians(getBaseRotation().x);
        float rotY = (float) Math.toRadians(getBaseRotation().y);
        float rotZ = (float) Math.toRadians(getBaseRotation().z);
        Quaternionf rotationXQuat = new Quaternionf().rotateAxis(rotX, 1, 0, 0);
        Quaternionf rotationYQuat = new Quaternionf().rotateAxis(rotY, 0, 1, 0);
        Quaternionf rotationZQuat = new Quaternionf().rotateAxis(rotZ, 0, 0, 1);

        // Combine config rotations
        Quaternionf baseRotation = rotationXQuat.mul(rotationYQuat).mul(rotationZQuat);

        // Apply player's rotation based on attachment point
        if (this.getAttachmentPoint() == AttachmentPoint.HEAD) {
            baseRotation.rotateY((float) Math.toRadians(-yaw)).rotateX((float) Math.toRadians(pitch));
        } else if (this.getAttachmentPoint() == AttachmentPoint.BODY) {
            baseRotation.rotateY((float) Math.toRadians(-yaw));
        }

        Vector3f offsetVector = new Vector3f(getBaseTranslation().x, getBaseTranslation().y, getBaseTranslation().z);

        // Create a rotation quaternion for the offset vector (only using yaw)
        Quaternionf offsetRotation = new Quaternionf().rotateY(-yaw);

        // Apply the rotation to the offset vector
        Vector3f rotatedOffset = offsetRotation.transform(offsetVector);

        // Apply the final transformation to the entity
        Transformation transformation = new Transformation(
                rotatedOffset,
                new AxisAngle4f(baseRotation),
                new Vector3f(scale.x, scale.y, scale.z),
                new AxisAngle4f()
        );
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
    public Vector3f getBaseRotation() { return baseRotation; }
}
