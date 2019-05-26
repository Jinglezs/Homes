package net.jingles.homes.persistence;

import net.jingles.homes.Home;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HomesArrayDataType implements PersistentDataType<byte[], Home[]> {

  @NotNull
  @Override
  public Class<byte[]> getPrimitiveType() {
    return byte[].class;
  }

  @NotNull
  @Override
  public Class<Home[]> getComplexType() {
    return Home[].class;
  }

  @NotNull
  @Override
  public byte[] toPrimitive(@NotNull Home[] homes, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

      // Write the size of the array
      dataOutput.writeInt(homes.length);

      // Save every element in the list
      for (Home home : homes) {
        dataOutput.writeObject(home);
      }

      // Serialize that array
      dataOutput.close();
      return outputStream.toByteArray();

    } catch (Exception e) {
      throw new IllegalStateException("Unable to save homes.", e);
    }
  }

  @NotNull
  @Override
  public Home[] fromPrimitive(@NotNull byte[] bytes, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
    try {
      ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
      BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
      Home[] items = new Home[dataInput.readInt()];

      for (int i = 0; i < items.length; i++) {
        items[i] = (Home) dataInput.readObject();
      }

      dataInput.close();
      return items;
    } catch (ClassNotFoundException | IOException e) {
      throw new IllegalStateException("Unable to deserialize homes.", e);
    }
  }

}
