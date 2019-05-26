package net.jingles.homes;

import co.aikar.commands.BukkitCommandIssuer;
import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import net.jingles.homes.persistence.DataTypes;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class HomesMain extends JavaPlugin implements Listener {

  private PaperCommandManager commandManager;
  public static ItemStack HOMES_BOOK;
  public static NamespacedKey HOMES;

  @Override
  public void onEnable() {
    commandManager = new PaperCommandManager(this);
    getServer().getPluginManager().registerEvents(this, this);
    registerCommands();
    HOMES = new NamespacedKey(this, "homes");

    //Create the template for the Homes Catalog item.
    ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
    BookMeta meta = (BookMeta) book.getItemMeta();
    meta.setTitle(ChatColor.GOLD + "Homes Catalog");
    meta.setGeneration(BookMeta.Generation.TATTERED);
    meta.setAuthor("Homes");

    //Add an empty array of Homes to the item's persistent data container
    meta.getPersistentDataContainer().set(HOMES, DataTypes.HOME_ARRAY, new Home[]{});
    book.setItemMeta(meta);
    HOMES_BOOK = book;

    //Add the Homes Catalog item recipe to the server
    NamespacedKey key = new NamespacedKey(this, "catalog");
    ShapedRecipe recipe = new ShapedRecipe(key, HOMES_BOOK);
    recipe.shape(" E ", "ECE", " E ");
    recipe.setIngredient('E', Material.ENDER_PEARL);
    recipe.setIngredient('C', Material.COMPASS);

    //This is to catch annoying "Duplicate recipe" exceptions when
    //you reload the plugin. Removing them doesn't work anymore apparently.
    try {
      getServer().addRecipe(recipe);
    } catch (IllegalStateException e) {
    }

  }

  private void registerCommands() {

    //Command contexts basically convert command arguments (Strings) or information
    // about the player to the given class. This makes use of aikar's Annotation Command
    // Framework and really simplifies command code.

    //Gets the item the player is currently holding in their hand and
    //uses it to create a HomesCatalog class.
    commandManager.getCommandContexts().registerContext(HomesCatalog.class, context -> {
      Player player = context.getPlayer();
      ItemStack stack = player.getInventory().getItemInMainHand();
      if (!isHomeCatalog(stack)) throw new InvalidCommandArgument("You must be holding a catalog to use this command.");
      return new HomesCatalog(player, stack);
    });

    //Translates a String argument to a Location object
    commandManager.getCommandContexts().registerContext(Location.class, context -> {
      String[] loc = context.popFirstArg().split(",");
      return new Location(Bukkit.getWorld(loc[0]), Double.valueOf(loc[1]), Double.valueOf(loc[2]),
              Double.valueOf(loc[3]), Float.valueOf(loc[4]), Float.valueOf(loc[5]));
    });

    //Command Conditions are basically requirements that the player has to meet
    //in order to execute the command. Again, this removes the need to
    //check in each command individually and can be used on any command.
    commandManager.getCommandConditions().addCondition("hasBook", context -> {
      BukkitCommandIssuer issuer = context.getIssuer();
      if (!issuer.isPlayer()) throw new ConditionFailedException("Only players can use this command.");
      if (!isHomeCatalog(context.getIssuer().getPlayer().getInventory().getItemInMainHand()))
        throw new ConditionFailedException("You must be holding a Homes Catalog to use this command.");
    });

    commandManager.registerCommand(new HomeCommands());
  }

  //Only Home Catalog items will contain the correct NamespacedKey in their
  //persistent data container.
  private boolean isHomeCatalog(ItemStack item) {
    return item.getItemMeta() != null && item.getItemMeta()
            .getPersistentDataContainer().has(HOMES, DataTypes.HOME_ARRAY);
  }

}
