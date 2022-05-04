package de.verklickt.proxy.listeners;

import de.verklickt.proxy.Main;
import lombok.var;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PostLogin implements Listener {

    @EventHandler
    public void onLogin(PostLoginEvent e) {
        var p = e.getPlayer();

        if (Main.configFile.getBoolean("settings.maintenance")) {
            if (!p.hasPermission("management.maintenance")) {
                p.disconnect(new TextComponent("§cWir führen Wartungen durch!"));
            }
        } else {
            if (ProxyServer.getInstance().getOnlineCount() >= 100) {
                if (!p.hasPermission("management.fulljoin")) {
                    p.disconnect(new TextComponent("§cThe Server is full! Please wait before reconnecting..."));
                }
            }
        }
    }
}
