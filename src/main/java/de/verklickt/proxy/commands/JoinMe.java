package de.verklickt.proxy.commands;

import de.verklickt.proxy.utils.Datenbank;
import lombok.var;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class JoinMe extends Command {
    private Datenbank.DatenbankData joinme;
    public JoinMe(String name, Datenbank.DatenbankData joinme) {
        super(name);
        this.joinme = joinme;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            var p = (ProxiedPlayer) sender;
            if (args.length == 0) {
                if (p.hasPermission("management.joinme")) {
                    sendJoinMe(p);
                } else {
                    p.sendMessage(new TextComponent("§8» §bJoinMe §8● §cDu darfst nur §e/joinme <Spieler> §cnutzen"));
                }
            } else {
                var joinMeSender = ProxyServer.getInstance().getPlayer(args[0]);
                if (ProxyServer.getInstance().getPlayers().contains(joinMeSender)) {
                    if (!this.joinme.exists("NAME", joinMeSender)) {
                        sender.sendMessage(new TextComponent("§8» §bJoinMe §8● §cDas Joinme ist abgelaufen!"));
                    } else {
                        connect(p, getServer(joinMeSender), joinMeSender);
                    }
                } else {
                    sender.sendMessage(new TextComponent("§8» §bJoinMe §8● §cDer Spieler existiert nicht!"));
                }
            }
        } else {
            sender.sendMessage(new TextComponent("Hmmmm... Ich glaube du bist kein Spieler..."));
        }
    }

    private void sendJoinMe(ProxiedPlayer sender) {
        this.joinme.createObject(sender.getName(), sender.getServer().getInfo().getName());
        sender.sendMessage(new TextComponent("§8» §bJoinMe §8● §aJoinme wurde gesendet!"));
        var serverName = sender.getServer().getInfo().getName();

        var firstLine = new TextComponent("§8» §bJoinMe §8● §e" + sender.getName() + " spielt " + serverName);
        var secondLine = new TextComponent("§8» §bJoinMe §8● §bKlicke um mitzuspielen!");

        firstLine.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/joinme " + sender.getName()));
        secondLine.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/joinme " + sender.getName()));

        for (ProxiedPlayer p: ProxyServer.getInstance().getPlayers()) {
            if (!p.getServer().getInfo().getName().equalsIgnoreCase(serverName)) {
                p.sendMessage(new TextComponent("§8§l■§m---------------------------------------§r§8§l■"));
                p.sendMessage(new TextComponent(""));
                p.sendMessage(new TextComponent(firstLine));
                p.sendMessage(new TextComponent(secondLine));
                p.sendMessage(new TextComponent(""));
                p.sendMessage(new TextComponent("§8§l■§m---------------------------------------§r§8§l■"));
            }
        }
    }

    private ServerInfo getServer(ProxiedPlayer p) {
        var entry = this.joinme.getString("SERVER", "NAME", p.getName());
        var service = ProxyServer.getInstance().getServerInfo(entry);
        return service;
    }

    private void connect(ProxiedPlayer p, ServerInfo server, ProxiedPlayer sender) {
        if (p.getServer().getInfo() == server) {
            p.sendMessage(new TextComponent("§8» §bJoinMe §8● §cDu bist bereits auf dem Server!"));
        } else {
            p.sendMessage(new TextComponent("§8» §bJoinMe §8● §7Du wurdest zu §e" + sender.getName() + " §7gesendet!"));
            p.connect(server);
        }
    }
}
