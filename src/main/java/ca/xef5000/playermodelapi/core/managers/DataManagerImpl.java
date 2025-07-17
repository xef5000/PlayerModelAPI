package ca.xef5000.playermodelapi.core.managers;

import ca.xef5000.playermodelapi.PlayerModelAPI;
import ca.xef5000.playermodelapi.api.AttachmentData;
import ca.xef5000.playermodelapi.api.DataManager;
import ca.xef5000.playermodelapi.api.IPlayerModelAPI;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static ca.xef5000.playermodelapi.PlayerModelAPI.getApi;

public class DataManagerImpl implements DataManager {

    private final PlayerModelAPI plugin;
    private final NamespacedKey attachmentDataKey;

    public DataManagerImpl(PlayerModelAPI plugin) {
        this.plugin = plugin;
        this.attachmentDataKey = new NamespacedKey(plugin, "attachment_data");
    }

    @Override
    public void loadAttachmentsFor(Player player) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        if (pdc.has(attachmentDataKey, PersistentDataType.BYTE_ARRAY)) {
            byte[] bytes = pdc.get(attachmentDataKey, PersistentDataType.BYTE_ARRAY);
            try {
                // Deserialize the byte array back into our list of snapshots
                List<AttachmentData> dataList = deserializeAttachmentData(bytes);
                // Re-create the live attachments
                for (AttachmentData data : dataList) {
                    getApi().attach(player, data.item(), data.point(), data.offsetTranslation(), data.offsetRotation(), data.scale());
                }
            } catch (IOException | ClassNotFoundException e) {
                plugin.getLogger().severe("Failed to load attachment data for " + player.getName());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveAttachmentData(Player player, AttachmentData newData) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        List<AttachmentData> existingData = new ArrayList<>();

        // Get existing data if it's there
        if (pdc.has(attachmentDataKey, PersistentDataType.BYTE_ARRAY)) {
            try {
                existingData = deserializeAttachmentData(pdc.get(attachmentDataKey, PersistentDataType.BYTE_ARRAY));
            } catch (IOException | ClassNotFoundException e) { /* Handle error */ }
        }

        // Add the new attachment and save it back
        existingData.add(newData);
        try {
            pdc.set(attachmentDataKey, PersistentDataType.BYTE_ARRAY, serializeAttachmentData(existingData));
        } catch (IOException e) { /* Handle error */ }
    }

    @Override
    public void clearAttachmentData(Player player) {
        player.getPersistentDataContainer().remove(attachmentDataKey);
    }

    private byte[] serializeAttachmentData(List<AttachmentData> data) throws IOException {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(data);
            return byteStream.toByteArray();
        }
    }

    private List<AttachmentData> deserializeAttachmentData(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectStream = new ObjectInputStream(byteStream)) {
            return (List<AttachmentData>) objectStream.readObject();
        }
    }
}
