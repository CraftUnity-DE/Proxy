package de.verklickt.proxy.listeners;

import de.verklickt.proxy.Main;
import lombok.var;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ProxyPing implements Listener {

    @EventHandler
    public void onPing(ProxyPingEvent e) {
        ServerPing ping = e.getResponse();
        ServerPing.Players players = ping.getPlayers();
        ServerPing.Protocol protocol = ping.getVersion();

        if (Main.configFile.getBoolean("settings.maintenance")) {
            var description = Main.configFile.getString("maintenance.description");
            var hoverInfo = Main.configFile.getString("maintenance.hover");
            var info = Main.configFile.getString("maintenance.info");

            protocol.setProtocol(1);
            ping.getPlayers().setSample(new ServerPing.PlayerInfo[] { new ServerPing.PlayerInfo(hoverInfo, "") });
            ping.setDescriptionComponent(new TextComponent(description));
            protocol.setName(info);
            ping.setVersion(protocol);
            ping.setPlayers(players);
        } else {
            var description = Main.configFile.getString("motd.description");
            var info = Main.configFile.getString("motd.hover");

            ping.setDescriptionComponent(new TextComponent(description));
            ping.setVersion(protocol);
            ping.setPlayers(players);
            if (ping.getPlayers().getOnline() >= 100) {
                players.setMax(100);
            } else {
                players.setMax(ping.getPlayers().getOnline() + 1);
            }
        }
    }
}
