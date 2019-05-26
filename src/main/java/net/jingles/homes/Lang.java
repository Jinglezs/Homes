package net.jingles.homes;

import org.bukkit.ChatColor;

public class Lang {

  public static final String TITLE = ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Homes" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET;

  public static final String SAVED_HOME = TITLE + "Saved " + ChatColor.AQUA + "%s " + ChatColor.RESET + "to your home catalog.";

  public static final String DELETE_HOME = TITLE + "Removed " + ChatColor.AQUA + "%s " + ChatColor.RESET + "from your home catalog.";

  public static final String PINNED = TITLE + ChatColor.AQUA + "%s " + ChatColor.RESET + "is now pinned!";

  public static final String UNPINNED = TITLE + ChatColor.AQUA + "%s " + "has been unpinned.";

  public static final String DESCRIPTION = TITLE + "Description successfully set for " + ChatColor.AQUA + "%s";

  public static final String NOT_FOUND = TITLE + ChatColor.RED + "You do not have a home with that name.";

  public static final String MAX_PINNED = TITLE + ChatColor.RED + "You cannot have more than 10 pinned homes";

}
