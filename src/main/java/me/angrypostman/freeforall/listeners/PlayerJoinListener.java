package me.angrypostman.freeforall.listeners;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import me.angrypostman.freeforall.FreeForAll;
import me.angrypostman.freeforall.data.DataStorage;
import me.angrypostman.freeforall.kit.FFAKit;
import me.angrypostman.freeforall.kit.KitManager;
import me.angrypostman.freeforall.user.User;
import me.angrypostman.freeforall.user.UserCache;
import me.angrypostman.freeforall.util.Message;
import me.angrypostman.freeforall.util.Updater;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.PlayerInventory;

import static me.angrypostman.freeforall.FreeForAll.doSyncLater;

public class PlayerJoinListener implements Listener{

    public PlayerJoinListener(final FreeForAll plugin){
        this.plugin=plugin;
        this.dataStorage=plugin.getDataStorage();
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event){

        final Player player=event.getPlayer();
        final Optional<User> optional=UserCache.getUserIfPresent(player);

        if(!optional.isPresent()){
            this.plugin.getLogger()
                       .info("Failed to load user data for '"+player.getName()+"("+player.getUniqueId()+") "+"during login validation.");
            player.kickPlayer(ChatColor.RED+"Failed to load your player data, please relog.");
            return;
        }

        final User user=optional.get();

        final PlayerInventory inventory=player.getInventory();
        inventory.clear();
        inventory.setArmorContents(null);

        player.setGameMode(GameMode.SURVIVAL);
        player.setFoodLevel(20);
        player.setHealth(getMaxHealth(player));
        player.setFireTicks(0);
        player.setLevel(0);
        player.setExp(0);
        player.setTotalExperience(0);

        Message message=Message.get("join-message-broadcast");
        if(message.getContent()!=null&&!message.getContent()
                                               .isEmpty()){
            message=message.replace("%player%", user.getName());
            event.setJoinMessage(message.getContent());
        }

        UserCache.getSpectators()
                 .forEach(online->{
                     if(player.canSee(online.getBukkitPlayer())){
                         player.hidePlayer(online.getBukkitPlayer());
                     }
                 });

        doSyncLater(()->{

            if(player.hasPermission("freeforall.viewupdates")){
                final Updater updater=this.plugin.getUpdater();

                final String latestVersion=updater.getLatestVersion();
                if(latestVersion!=null){
                    player.sendMessage(
                            ChatColor.GREEN+"A new version of FreeForAll is available for download (v"+latestVersion+")!");
                }
            }

            final Location location;

            final List<Location> locations=this.dataStorage.getLocations();
            if(locations==null||locations.size()==0){
                location=player.getWorld()
                               .getSpawnLocation();
            }else{
                final Random random=new Random();
                location=locations.get(random.nextInt(locations.size()));
            }

            player.teleport(location);

            Optional<FFAKit> kitOptional=KitManager.getKitOf(player);
            if(!kitOptional.isPresent()){
                kitOptional=KitManager.getDefaultKit();
                if(!kitOptional.isPresent()){ return; }
            }

            final FFAKit ffaKit=kitOptional.get();
            KitManager.giveItems(user, ffaKit);

            //A second later should be fine for this.
        }, 20L);
    }

    private double getMaxHealth(final Player player){
        try{
            return player.getAttribute(Attribute.GENERIC_MAX_HEALTH)
                         .getBaseValue();
        }catch(final NoClassDefFoundError ex){
            return player.getMaxHealth();
        }
    }

    private FreeForAll plugin=null;
    private DataStorage dataStorage=null;
}
