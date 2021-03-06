package me.angrypostman.freeforall.data;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.angrypostman.freeforall.user.User;
import org.bukkit.Location;

public abstract class DataStorage{

    public abstract boolean initialize();

    public abstract void close();

    public abstract Optional<User> createUser(UUID uuid,
                                              String playerName);

    public abstract Optional<User> loadUser(UUID uuid);

    public abstract Optional<User> loadUser(String lookupName);

    public abstract void saveUser(User user);

    public abstract List<User> getLeaderboardTop(int page);

    public abstract void saveLocation(Location location);

    public abstract void deleteLocation(int spawnId);

    //public abstract void deleteLocation(Location location);

    public abstract List<Location> getLocations();

    public abstract boolean isLoaded();
}
