package de.verklickt.proxy.utils;

import java.io.File;
import java.util.Arrays;

public class ConfigFile extends FileManager {
    private boolean save = false;

    public ConfigFile() {
        super(new File("plugins/Proxy", "config.yml"));
    }

    public void create() {
        //Wartungen
        if (!sectionExists("maintenance")) {
            set("maintenance.description", "§e§lCraftunity §8● §bServer Network   §8[§71.18.1§8] \n§b§bDa ist etwas...");
            set("maintenance.info", "§bComing soon....");
            set("maintenance.hover", "§bComing soon...");
        }
        //Normale MOTD
        if (!sectionExists("motd")) {
            set("motd.description", "§e§lCraftunity §8● §bServer Network   §8[§71.18.1§8] \n§cKeine Wartungen...");
            set("motd.hover", "§bCraftunity Network");
        }
        if (!sectionExists("settings")) {
            set("settings.use-as-blacklist", Boolean.valueOf(true));
            set("settings.maintenance", Boolean.valueOf(true));
            this.save = true;
        }
        if (!sectionExists("global")) {
            set("global", Arrays.asList(new String[] { "randomcommand" }));
            this.save = true;
        }
        if (!sectionExists("groups")) {
            set("groups.default", Arrays.asList(new String[] { "beispielcommand" }));
            this.save = true;
        }
        if (this.save)
            save();
    }
}
