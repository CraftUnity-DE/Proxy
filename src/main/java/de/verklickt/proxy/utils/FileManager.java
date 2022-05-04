package de.verklickt.proxy.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
public abstract class FileManager {
    private File file;

    private Configuration config;

    public FileManager(File file) {
        this.file = file;
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        try {
            this.config = YamlConfiguration.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        create();
        load();
    }

    public void create() {}

    public void load() {}

    public final void save() {
        try {
            YamlConfiguration.getProvider(YamlConfiguration.class).save(this.config, this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final void reload() {
        try {
            this.config = YamlConfiguration.getProvider(YamlConfiguration.class).load(this.file);
            load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final Configuration getConfig() {
        return this.config;
    }

    public final String getString(String key) {
        return this.config.getString(key, key);
    }

    public final String getMessage(String key) {
        return ChatColor.translateAlternateColorCodes('&', this.config.getString(key));
    }

    public final boolean getBoolean(String key) {
        return this.config.getBoolean(key);
    }

    public final int getInt(String key) {
        return this.config.getInt(key, -1);
    }

    public final List<String> getStringList(String key) {
        return this.config.getStringList(key);
    }

    public final List<Integer> getIntList(String key) {
        return this.config.getIntList(key);
    }

    public final boolean sectionExists(String key) {
        return this.config.contains(key);
    }

    public final void set(String key, Object value) {
        this.config.set(key, value);
    }
}

