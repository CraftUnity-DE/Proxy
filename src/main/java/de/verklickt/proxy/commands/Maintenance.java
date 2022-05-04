package de.verklickt.proxy.commands;

import de.verklickt.proxy.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Maintenance extends Command {
    public Maintenance(Main name) {
        super("maintenance");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("command.maintenance")) {
            if (Main.configFile.getBoolean("settings.maintenance")) {
                Main.configFile.set("settings.maintenance", false);
                sender.sendMessage(new TextComponent("§8» §b§lServer §8● §cWartungsmodus deaktiviert!"));

            } else if (!Main.configFile.getBoolean("settings.maintenance")) {
                Main.configFile.set("settings.maintenance", true);
                sender.sendMessage(new TextComponent("§8» §b§lServer §8● §cWartungsmodus aktiviert!"));

                for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
                    if (!p.hasPermission("management.maintenance")) {
                        p.disconnect(new TextComponent("§cWir führen jetzt Wartungsarbeiten durch!"));
                    }
                }
            }
        } else {
            sender.sendMessage(new TextComponent("§8» §b§lServer §8● §7Unbekannter Command. Nutze /help, für eine Liste von Commands"));
        }
    }
}
