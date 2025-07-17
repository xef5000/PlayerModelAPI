package ca.xef5000.playermodelapi.core.models;

import ca.xef5000.playermodelapi.api.Attachment;
import ca.xef5000.playermodelapi.api.AttachmentPoint;
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
        this.baseTranslation = new Vector3f(baseTranslation);
        this.baseRotation = baseRotation;
        this.scale = scale;
    }

    @Override
    public void update(Player player) {
        float yaw = (float) Math.toRadians(player.getLocation().getYaw());
        float pitch = (float) Math.toRadians(player.getLocation().getPitch());

        float rotX = (float) Math.toRadians(getBaseRotation().x);
        float rotY = (float) Math.toRadians(getBaseRotation().y);
        float rotZ = (float) Math.toRadians(getBaseRotation().z);
        Quaternionf rotationXQuat = new Quaternionf().rotateAxis(rotX, 1, 0, 0);
        Quaternionf rotationYQuat = new Quaternionf().rotateAxis(rotY, 0, 1, 0);
        Quaternionf rotationZQuat = new Quaternionf().rotateAxis(rotZ, 0, 0, 1);

        // Combine config rotations
        Quaternionf baseRotation = rotationXQuat.mul(rotationYQuat).mul(rotationZQuat);

        // Apply player's rotation based on attachment point
        Quaternionf combinedRotation = null;
        if (this.getAttachmentPoint() == AttachmentPoint.BODY) {
             combinedRotation = applyPlayerYawRotation(baseRotation, yaw);
        } else if (this.getAttachmentPoint() == AttachmentPoint.HEAD) {
            combinedRotation = applyPlayerPitchRotation(baseRotation, pitch);
            combinedRotation = applyPlayerYawRotation(combinedRotation, yaw);
        }

        // Create a rotation quaternion for the offset vector (only using yaw)
        Quaternionf offsetRotation = new Quaternionf().rotateY(-yaw);

        // Apply the rotation to the offset vector
        Vector3f rotatedOffset = offsetRotation.transform(new Vector3f(this.baseTranslation));

        // Apply the final transformation to the entity
        Transformation transformation = new Transformation(
                rotatedOffset,
                combinedRotation,
                new Vector3f(scale.x, scale.y, scale.z),
                new Quaternionf()
        );
        this.getDisplayEntity().setTransformation(transformation);
    }

    /**
     * Applies the player yaw rotation to the given quaternion.
     * @param baseRotation The base rotation quaternion (from config)
     * @param playerYaw The player's yaw in radians
     * @return The combined rotation quaternion
     */
    private Quaternionf applyPlayerYawRotation(Quaternionf baseRotation, float playerYaw) {
        // Create player rotation quaternion (around Y axis only - ignoring pitch)
        Quaternionf playerRotation = new Quaternionf().rotateAxis(-playerYaw, 0, 1, 0);
        // Combine player rotation with base rotation
        return playerRotation.mul(baseRotation);
    }

    private Quaternionf applyPlayerPitchRotation(Quaternionf baseRotation, float playerPitch) {
        // Create player rotation quaternion (around X axis only - ignoring yaw)
        Quaternionf playerRotation = new Quaternionf().rotateAxis(-playerPitch, 1, 0, 0);
        // Combine player rotation with base rotation
        return playerRotation.mul(baseRotation);
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
