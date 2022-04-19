package eu.tuxcraft.translationprovider.spigot.model;

import org.bukkit.command.CommandSender;

import java.util.Map;

public interface Message {

  void sendTo(CommandSender cs);

  void sendTo(CommandSender cs, Map<String, String> params);

  String getFor(CommandSender cs);

  String getFor(CommandSender cs, Map<String, String> params);
}
