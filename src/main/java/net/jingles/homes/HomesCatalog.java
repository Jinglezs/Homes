package net.jingles.homes;

import net.jingles.homes.persistence.DataTypes;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.*;
import java.util.stream.Collectors;

public class HomesCatalog {

  private static final int MAX_PINNED_COUNT = 10;

  private final Player owner;
  private final ItemStack bookItem;
  private final Set<Home> homes = new HashSet<>();

  HomesCatalog(Player player, ItemStack item) {
    this.owner = player;
    this.bookItem = item;

    //Retrieve the array of Home objects from the data container.
    //This takes the byte[] saved to the item and writes it to Home objects.
    Home[] homeArray = bookItem.getItemMeta().getPersistentDataContainer()
            .get(HomesMain.HOMES, DataTypes.HOME_ARRAY);

    Collections.addAll(homes, homeArray);
  }

  Player getOwner() {
    return this.owner;
  }

  ItemStack getBookItem() {
    return this.bookItem;
  }

  //All of the editing commands are acted on the Home objects themselves.
  //When these methods are complete, all of the Homes are converted to a byte[] again
  //and saved to the item.
  void addHome(String name, String description, Location location) {
    getHomes().add(new Home(name, description, location, false));
    getOwner().sendMessage(String.format(Lang.SAVED_HOME, name));
    saveToItem();
  }

  void deleteHome(String name) {
    if (!hasHome(name)) return;
    getHomes().remove(getHome(name));
    getOwner().sendMessage(String.format(Lang.DELETE_HOME, name));
    saveToItem();
  }

  void pinHome(String name, boolean pinned) {
    int numberPinned = (int) getHomes().stream()
            .filter(Home::isPinned).count();

    if (numberPinned >= MAX_PINNED_COUNT) {
      getOwner().sendMessage(Lang.MAX_PINNED);
      return;
    }

    if (!hasHome(name)) getOwner().sendMessage(Lang.NOT_FOUND);
    else getHome(name).setPinned(this, pinned);
    saveToItem();
  }

  void editDescription(String name, String description) {
    if (!hasHome(name)) getOwner().sendMessage(Lang.NOT_FOUND);
    else getHome(name).setDescription(this, description);
    saveToItem();
  }

  boolean hasHome(String name) {
    return getHomes().stream().anyMatch(home -> home.getName().equalsIgnoreCase(name));
  }

  Set<Home> getHomes() {
    return this.homes;
  }

  Home getHome(String name) {
    return getHomes().stream()
            .filter(home -> home.getName().equalsIgnoreCase(name))
            .findAny().orElse(null);
  }

  void saveToItem() {

    final BookMeta meta = (BookMeta) getBookItem().getItemMeta();

    //Converts all of the Home objects to a byte[] and writes that to
    //the item's persistent data container (NBT tags)
    meta.getPersistentDataContainer().set(HomesMain.HOMES, DataTypes.HOME_ARRAY,
            getHomes().toArray(new Home[]{}));

    //Clears all pages of the book.
    meta.setPages(new ArrayList<>());

    //Books use JSON for formatting. BaseComponents and the ComponentBuilder
    //allows you to easily create JSON without writing any of it.
    final ComponentBuilder pinned = new ComponentBuilder("");
    pinned.append(new TextComponent("Pinned Homes:\n\n"));

    //Iterates over all of the homes and only collects those that are pinned
    List<Home> homes = getHomes().stream().filter(Home::isPinned).collect(Collectors.toList());

    if (!homes.isEmpty()) {

      //Add a line to the pinned page with clickable text that teleports the
      //player to the given home.
      homes.forEach(home -> {
        Location loc = home.getLocation();

        //This works by executing a command when the text is clicked. I added a custom
        //homes command so that a player does not need access to /teleport
        String command = "/home " + String.join(",", loc.getWorld().getName(),
                String.valueOf(loc.getX()), String.valueOf(loc.getY()), String.valueOf(loc.getZ()),
                String.valueOf(loc.getYaw()), String.valueOf(loc.getPitch()));

        pinned.append(new ComponentBuilder(" - " + home.getName() + "\n")
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)).create());
      });

      //If there are no pinned homes, add this message to the pinned page.
    } else pinned.append(new TextComponent("\nNo pinned homes! Use /pinHome <name> <true or false>"));

    meta.spigot().addPage(pinned.create());

    //This adds an information page for each home.
    getHomes().forEach(home -> {

      ComponentBuilder builder = new ComponentBuilder("");
      builder.append(new TextComponent("Name: " + home.getName()));

      Location loc = home.getLocation();
      TextComponent location = new TextComponent("\nLocation Info: ");
      location.addExtra("\n  World: " + loc.getWorld().getName());
      location.addExtra("\n  x: " + loc.getBlockX());
      location.addExtra("\n  y: " + loc.getBlockY());
      location.addExtra("\n  z: " + loc.getBlockZ());
      builder.append(location);

      builder.append(new TextComponent("\nDescription: " + home.getDescription()));

      String command = "/home " + String.join(",", loc.getWorld().getName(),
              String.valueOf(loc.getX()), String.valueOf(loc.getY()), String.valueOf(loc.getZ()),
              String.valueOf(loc.getYaw()), String.valueOf(loc.getPitch()));

      TextComponent teleport = new TextComponent("\n\nClick to teleport!");
      teleport.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
      builder.append(teleport);

      meta.spigot().addPage(builder.create());
    });

    //This saved all of the changes we made to the book's pages and
    //persistent data container.
    getBookItem().setItemMeta(meta);
  }

}
