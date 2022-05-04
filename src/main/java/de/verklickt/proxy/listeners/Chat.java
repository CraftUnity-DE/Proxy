package de.verklickt.proxy.listeners;

import lombok.var;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Locale;

public class Chat implements Listener {

    @EventHandler
    public void onCommand(ChatEvent e) {
        ProxiedPlayer p = (ProxiedPlayer) e.getSender();
        String error = "§8» §b§lServer §8● §7Unbekannter Command. Nutze /help, für eine Liste von Commands";
        var message = e.getMessage().toLowerCase();

        //Flamecord
        if (message.contains("/flamecord") && !p.hasPermission("command.cloudnet")) {
            if (e.isCommand()) {
                e.setCancelled(true);
                p.sendMessage(new TextComponent(error));
            }
        }
        //AdvancedGUI
        if (message.contains("/advancedgui") && !p.hasPermission("command.advancedgui")) {
            if (e.isCommand()) {
                e.setCancelled(true);
                p.sendMessage(new TextComponent(error));
            }
        }
        //AG
        if (message.contains("/ag".toLowerCase(Locale.ROOT)) && !p.hasPermission("command.advancedgui")) {
            if (e.isCommand()) {
                e.setCancelled(true);
                p.sendMessage(new TextComponent(error));
            }
        }
        //AdvancedGUI:AdvancedGUI
        if (message.contains("/advancedgui:advancedgui") && !p.hasPermission("command.advancedgui")) {
            if (e.isCommand()) {
                e.setCancelled(true);
                p.sendMessage(new TextComponent(error));
            }
        }
        //AdvancedGUI:AG
        if (message.contains("/advancedgui:ag") && !p.hasPermission("command.advancedgui")) {
            if (e.isCommand()) {
                e.setCancelled(true);
                p.sendMessage(new TextComponent(error));
            }
        }
        //Geyser
        if (message.contains("/geyser") && !p.hasPermission("command.geyser")) {
            if (e.isCommand()) {
                e.setCancelled(true);
                p.sendMessage(new TextComponent(error));
            }
        }
        //LP
        if (message.contains("/lp") && !p.hasPermission("command.luckperms")) {
            if (e.isCommand()) {
                e.setCancelled(true);
                p.sendMessage(new TextComponent(error));
            }
        }
        //Luckperms
        if (message.contains("/luckperms") && !p.hasPermission("command.luckperms")) {
            if (e.isCommand()) {
                e.setCancelled(true);
                p.sendMessage(new TextComponent(error));
            }
        }
        //LPB
        if (message.contains("/lpb") && !p.hasPermission("command.luckperms")) {
            if (e.isCommand()) {
                e.setCancelled(true);
            }
        }
        //Luckpermsbungee
        if (message.contains("/luckpermsbungee") && !p.hasPermission("command.luckperms")) {
            if (e.isCommand()) {
                e.setCancelled(true);
            }
        }
        //Cloud
        if (message.contains("/cloud") && !p.hasPermission("command.cloudnet")) {
            if (e.isCommand()) {
                e.setCancelled(true);
                p.sendMessage(new TextComponent(error));
            }
        }
        //Cloudnet
        if (message.contains("/cloudnet") && !p.hasPermission("command.cloudnet")) {
            if (e.isCommand()) {
                e.setCancelled(true);
            }
        }
        //Bungee (CloudNet)
        if (message.contains("/bungee") && !p.hasPermission("command.cloudnet")) {
            if (e.isCommand()) {
                e.setCancelled(true);
                p.sendMessage(new TextComponent(error));
            }
        }
        if (message.contains("/ac") && !p.hasPermission("command.anticheat")) {
            if (e.isCommand()) {
                e.setCancelled(true);
                p.sendMessage(new TextComponent(error));
            }
        }
        if (message.contains("/anticheat") && !p.hasPermission("command.anticheat")) {
            if (e.isCommand()) {
                e.setCancelled(true);
                p.sendMessage(new TextComponent(error));
            }
        }
        if (message.contains("/floodgate") && !p.hasPermission("command.geyser")) {
            if (e.isCommand()) {
                e.setCancelled(true);
                p.sendMessage(new TextComponent(error));
            }
        }
        //Placeholderapi
        if (message.contains("/placeholderapi") && !p.hasPermission("command.placeholderapi")) {
            if (e.isCommand()) {
                e.setCancelled(true);
                p.sendMessage(new TextComponent(error));
            }
        }
        //Papi
        if (message.contains("/papi") && !p.hasPermission("command.placeholderapi")) {
            if (e.isCommand()) {
                e.setCancelled(true);
                p.sendMessage(new TextComponent(error));
            }
        }
        if (message.contains("/plugman") && !p.hasPermission("command.plugman")) {
            if (e.isCommand()) {
                e.setCancelled(true);
                p.sendMessage(new TextComponent(error));
            }
        }
        if (message.contains("/plugmanx") && !p.hasPermission("command.plugman")) {
            if (e.isCommand()) {
                e.setCancelled(true);
                p.sendMessage(new TextComponent(error));
            }
        }
        if (message.contains("/plugins") && !p.hasPermission("command.plugins")) {
            if (e.isCommand()) {
                e.setCancelled(true);
                p.sendMessage(new TextComponent("Plugins (1): §aGeht dich nichts an"));
            }
        }
        if (message.contains("/pl") && !p.hasPermission("command.plugins")) {
            if (e.isCommand()) {
                e.setCancelled(true);
                p.sendMessage(new TextComponent("Plugins (1): §aGeht dich nichts an"));
            }
        }
        if (message.contains("/?") && !p.hasPermission("command.plugins")) {
            if (e.isCommand()) {
                e.setCancelled(true);
                p.sendMessage(new TextComponent(error));
            }
        }
        if (message.contains("/version") && !p.hasPermission("command.version")) {
            if (e.isCommand()) {
                e.setCancelled(true);
                p.sendMessage(new TextComponent(error));
            }
        }
    }
}
