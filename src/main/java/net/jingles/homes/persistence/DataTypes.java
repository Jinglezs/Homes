package net.jingles.homes.persistence;

import net.jingles.homes.Home;
import org.bukkit.persistence.PersistentDataType;

public interface DataTypes {

  PersistentDataType<byte[], Home[]> HOME_ARRAY = new HomesArrayDataType();

}
