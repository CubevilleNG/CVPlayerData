package org.cubeville.cvplayerdata;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.cubeville.cvplayerdata.playerdata.PlayerDataDao;
import org.cubeville.cvplayerdata.playerdata.PlayerDataManager;
import org.cubeville.cvplayerdata.playerdata.ProfilesDao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.Logger;

public class CVPlayerData extends Plugin {

    private Logger logger;
    private PlayerDataManager playerDataManager;

    @Override
    public void onEnable() {
        this.logger = this.getLogger();

        //Load config (creates blank config if doesn't exist)
        final File dataDir = getDataFolder();
        if(!dataDir.exists()) dataDir.mkdirs();
        File configFile = new File(dataDir, "config.yml");
        if(!configFile.exists()) generateNewConfig(configFile);
        Configuration config;
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch(IOException e) {
            throw new RuntimeException("Unable to load config file!", e);
        }
        String dbUser = config.getString("db_user");
        String dbPassword = config.getString("db_password");
        String dbDatabase = config.getString("db_database");
        if(dbUser.length() > 0 && dbPassword.length() > 0 && dbDatabase.length() > 0) {
            PlayerDataDao playerDataDao = new PlayerDataDao(dbUser, dbPassword, dbDatabase);
            new ProfilesDao(dbUser, dbPassword, dbDatabase);
            playerDataManager = new PlayerDataManager(playerDataDao);
        } else {
            throw new RuntimeException("db_user, db_password, or db_database not set in config.yml!");
        }
    }

    private void generateNewConfig(File file) {
        try {
            file.createNewFile();
            final InputStream inputStream = this.getResourceAsStream(file.getName());
            final FileOutputStream fileOutputStream = new FileOutputStream(file);
            final byte[] buffer = new byte[4096];
            int bytesRead;
            while((bytesRead = Objects.requireNonNull(inputStream).read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch(IOException e) {
            throw new RuntimeException("Unable to generate config file!", e);
        }
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }
}
