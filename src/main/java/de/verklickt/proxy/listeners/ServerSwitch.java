package de.verklickt.proxy.listeners;

import de.verklickt.proxy.utils.Datenbank;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerSwitch implements Listener {
    private Datenbank.DatenbankData joinme;
    public ServerSwitch (Datenbank.DatenbankData joinme) {
        this.joinme = joinme;
    }

    @EventHandler
    public void onSwitch(ServerSwitchEvent e) {
        if (this.joinme.exists("NAME", e.getPlayer().getName())) {
            this.joinme.deleteObject("NAME", e.getPlayer().getName());
        }
    }
}
