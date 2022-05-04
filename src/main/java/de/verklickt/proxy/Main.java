package de.verklickt.proxy;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.verklickt.proxy.commands.JoinMe;
import de.verklickt.proxy.commands.Maintenance;
import de.verklickt.proxy.listeners.*;
import de.verklickt.proxy.utils.ConfigFile;
import de.verklickt.proxy.utils.Datenbank;
import de.verklickt.proxy.utils.FileManager;
import io.github.waterfallmc.waterfall.event.ProxyDefineCommandsEvent;
import lombok.var;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class Main extends Plugin {
    public static Main instance;
    public static FileManager configFile;
    private Datenbank datenbank;
    private Datenbank.DatenbankData joinme;
    private Datenbank.DatenbankData languages;

    @Override
    public void onEnable() {
        this.setInstance(this);
        setupFiles();
        setupDatabase();

        System.out.println("Syncproxy started :D");
    }

    private void registerEvents() {
        var manager = ProxyServer.getInstance().getPluginManager();
        var cloudmanager = CloudNetDriver.getInstance().getEventManager();

        cloudmanager.registerListener(new ServiceStart());
        cloudmanager.registerListener(new ServiceStop());

        manager.registerListener(this, new PostLogin());
        manager.registerListener(this, new ProxyPing());
        manager.registerListener(this, new Chat());
        manager.registerListener(this, new ServerSwitch(this.joinme));

        manager.registerCommand(this, new Maintenance(this));
        manager.registerCommand(this, new JoinMe("joinme", this.joinme));
    }

    private void setupDatabase() {
        this.datenbank = new Datenbank();
        this.datenbank.setServer("45.85.219.106", Datenbank.DatenbankType.MYSQL);
        this.datenbank.setUser("craftunity", "zFtf5zFTS*ncJrXT");
        this.datenbank.setTable("Players");
        this.datenbank.setExtra(Datenbank.URLType.AUTO_RECONNECT, Datenbank.URLType.USE_SSL);

        this.datenbank.connect(() -> {
            this.joinme = new Datenbank.DatenbankData(this.datenbank, "joinme");
            this.languages = new Datenbank.DatenbankData(this.datenbank, "langs");

            this.joinme.setArgument("NAME", Datenbank.DatenType.VARCHAR, 16);
            this.joinme.setArgument("SERVER", Datenbank.DatenType.VARCHAR, 80);

            this.languages.setArgument("LANG", Datenbank.DatenType.VARCHAR, 26);
            this.languages.setArgument("TEXT", Datenbank.DatenType.VARCHAR, 300);

            this.joinme.createTable();
            this.languages.createTable();
            registerEvents();
        });
    }

    private void setupFiles() {
        File dic = new File("plugins/Proxy");
        if (!dic.isDirectory())
            dic.mkdir();
        this.configFile = (FileManager)new ConfigFile();
    }

    @EventHandler
    public void onSuggestionSend(ProxyDefineCommandsEvent event) {
        ProxiedPlayer player = (ProxiedPlayer)event.getReceiver();
        if (this.configFile.getBoolean("settings.use-as-blacklist")) {
            List<String> blocked = new ArrayList<>();
            blocked.addAll(this.configFile.getStringList("global"));
            for (String group : this.configFile.getConfig().getSection("groups").getKeys()) {
                if (!player.hasPermission("command." + group))
                    blocked.addAll(this.configFile.getStringList("groups." + group));
            }
            event.getCommands().entrySet().removeIf(val -> blocked.contains(val.getKey()));
        } else {
            List<String> allowed = new ArrayList<>();
            allowed.addAll(this.configFile.getStringList("global"));
            for (String group : this.configFile.getConfig().getSection("groups").getKeys()) {
                if (!player.hasPermission("command." + group))
                    allowed.addAll(this.configFile.getStringList("groups." + group));
            }
            event.getCommands().entrySet().removeIf(val -> !allowed.contains(val.getKey()));
        }
    }

    public FileManager getConfigFile() {
        return this.configFile;
    }
    public void setInstance(Main instance) {Main.instance = instance; }
    public static Main getInstance() {return instance; }
}
