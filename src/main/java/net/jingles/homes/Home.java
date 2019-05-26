package net.jingles.homes;

import org.bukkit.Location;

import java.io.Serializable;

//A bunch of simple getters and setters. Must implement Serializable
//in order to be converted to a byte[]
public class Home implements Serializable {

  private String name;
  private String description;
  private Location location;
  private boolean pinned;

  public Home(String name, String description, Location location, boolean pinned) {
    this.name = name;
    this.description = description;
    this.location = location;
    this.pinned = pinned;
  }

  public String getName() {
    return this.name;
  }

  public String getDescription() {
    return this.description;
  }

  public Location getLocation() {
    return this.location;
  }

  public boolean isPinned() {
    return this.pinned;
  }

  public void setDescription(HomesCatalog catalog, String description) {
    this.description = description;
    catalog.getOwner().sendMessage(String.format(Lang.DESCRIPTION, getName()));
  }

  public void setPinned(HomesCatalog catalog, boolean pinned) {
    this.pinned = pinned;
    catalog.getOwner().sendMessage(String.format(pinned ? Lang.PINNED : Lang.UNPINNED, getName()));
  }

  public boolean hasDescription() {
    return this.description != null;
  }

}
