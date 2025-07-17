package ca.xef5000.playermodelapi;

import ca.xef5000.playermodelapi.api.DataManager;
import ca.xef5000.playermodelapi.api.IPlayerModelAPI;
import ca.xef5000.playermodelapi.core.managers.AttachmentManager;
import ca.xef5000.playermodelapi.core.listener.AttachmentListener;
import ca.xef5000.playermodelapi.core.managers.DataManagerImpl;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerModelAPI extends JavaPlugin {

    private static IPlayerModelAPI apiInstance;

    private DataManager dataManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("PlayerModsAPI has been enabled!");
        apiInstance = new AttachmentManager(this);
        dataManager = new DataManagerImpl(this);
        this.getServer().getPluginManager().registerEvents(new AttachmentListener(apiInstance, dataManager, this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("PlayerModsAPI has been disabled!");
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    /**
     * Gets the active instance of the PlayerModelAPI.
     *
     * @return The API instance.
     * @throws IllegalStateException if the API is not yet enabled.
     */
    public static IPlayerModelAPI getApi() {
        if (apiInstance == null) {
            throw new IllegalStateException("PlayerModsAPI is not enabled yet!");
        }
        return apiInstance;
    }
}
