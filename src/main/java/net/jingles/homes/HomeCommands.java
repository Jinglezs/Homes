package net.jingles.homes;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HomeCommands extends BaseCommand {

  @Dependency
  private HomesMain plugin;

  @CommandAlias("home")
  @Conditions("hasBook")
  @Description("Teleport to the given location")
  public void onHomeTeleport(Player player, Location location) {
    player.sendMessage(Lang.TITLE + "Teleporting in 5 seconds!");
    plugin.getServer().getScheduler().runTaskLater(plugin, () -> player.teleport(location), 100L);
  }

  @CommandAlias("sethome")
  @Conditions("hasBook")
  @Description("Set a home with the given name at your current location.")
  public void onHomeSet(Player sender, HomesCatalog catalog, String name, @Optional String... description) {
    catalog.addHome(name, description != null ? String.join(" ", description) : null, sender.getLocation());
  }

  @CommandAlias("delhome")
  @Conditions("hasBook")
  @Description("Removes a home from the player's catalog")
  public void onHomeDeletion(HomesCatalog catalog, String name) {
    catalog.deleteHome(name);
  }

  @CommandAlias("pinhome")
  @Conditions("hasBook")
  @Description("Pin or unpin the given home. Pinned homes appear on the first page of your catalog.")
  public void onHomePin(HomesCatalog catalog, String name, boolean pinned) {
    catalog.pinHome(name, pinned);
  }

  @CommandAlias("setHomeDescription")
  @Conditions("hasBook")
  @Description("Edit the given home's description.")
  public void onEditDescription(HomesCatalog catalog, String name, String... description) {
    catalog.editDescription(name, String.join(" ", description));
  }

}


