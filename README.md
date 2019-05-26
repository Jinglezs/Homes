# Homes

Homes is a simple plugin that allows players to save named locations to a book named Homes Catalog.
The homes are saved directly to the item, therefore they can be exchanged between players easily.

### Pinned Homes
The first page of the book is filled with the player's Pinned Homes. It only lists the homes' names, which teleport the player to the home's location when clicked. Pinned homes can be set by holding the Homes Catalog and executing the command "/pinhome <name> <true/false>. The amount of pinned homes the player can set is limited so that it fits on the first page.
  
### Home Information
Each page in the book, excluding the pinned page, is dedicated to one home each. It displays the home's name, location information (world, x, y, z, yaw, pitch), and an editable description (/setHomeDescription <home> <new description>). Each page is ended with
  a line that reads "Click here to teleport!" Hopefully that is self-explanatory...
  
### Commands

All commands require the player to be holding their Homes Catalog. The Homes Catalog is automatically updated after each command.

- /sethome <name> <description...> Creates a home with the given name and description at the player's current location.
- /pinhome <name> <true or false> If true, adds the home to the Pinned Homes page. Otherwise it is removed.
- /delhome <name> Removes the home from the catalog completely.
- /setHomeDescription <name> <description...> Sets the home's description.  
