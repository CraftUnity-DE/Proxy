package de.verklickt.proxy.listeners;

import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.service.CloudServiceStartEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ServiceStart {
    @EventListener
    public void onStart(CloudServiceStartEvent e) {
        for (ProxiedPlayer p: ProxyServer.getInstance().getPlayers()) {
            if (p.hasPermission("management.serverupdate")) {
                p.sendMessage(new TextComponent("§8» §b§lServer §8● §e" + e.getServiceInfo().getName() + " §astartet §7auf §e" + e.getDriver().getNodeUniqueId() + "§7."));
            }
        }
    }
}
