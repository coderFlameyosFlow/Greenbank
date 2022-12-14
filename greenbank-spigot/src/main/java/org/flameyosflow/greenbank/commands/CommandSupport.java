package org.flameyosflow.greenbank.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

import org.flameyosflow.greenbank.GreenBankMain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Map;

/*
 * Adds response for certain checks, so this class
 * has all the variables to give us the check responses
 * and add simplicity to add/edit/remove the responses
 *
 * or add methods for some type of support.
 *
 * Added since 1.0.0 build 1
 */
public class CommandSupport {

    protected final static GreenBankMain greenBank = GreenBankMain.getPlugin();

    protected final static String NOT_ENOUGH_PERMISSIONS = greenBank.getMessagesConfigFile().getString("not-enough-permissions");

    protected final static String ONLY_PLAYER = greenBank.getMessagesConfigFile().getString("only-player-command");

    protected final static String PLAYER_DOES_NOT_EXIST = greenBank.getMessagesConfigFile().getString("player-does-not-exist");

    protected final static String NOT_ENOUGH_MONEY = greenBank.getMessagesConfigFile().getString("not-enough-money");

    protected static boolean notPlayer(CommandSender sender) {
        return !(sender instanceof Player);
    }

    /**
     * Checks if the Player has the permission, If the command is for ONLY player, use this.
     *
     * @return true if the Player has the permission, otherwise false.
     * <p>
     * Added since 1.0.0 build 1
     */
    protected static boolean playerDoesNotHavePermission(Player player, String permission) {
        return !player.hasPermission(permission);
    }

    /**
     * Checks if the CommandSender has the permission, If the command is for console AND player, use this.
     *
     * @return true if the CommandSender has the permission, otherwise false.
     * <p>
     * Added since 1.0.0 build 1
     */
    protected static boolean senderDoesNotHavePermission(CommandSender sender, String permission) {
        return !sender.hasPermission(permission);
    }

    protected static boolean playerHasNotPlayedBefore(Player player) {
        return !player.hasPlayedBefore();
    }

    /**
     * Retrieves the current command map instance
     *
     * @return the command map instance
     */
    public static @Nullable SimpleCommandMap retrieveMap() {
        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            return (SimpleCommandMap) field.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Adds a command to the Command Map
     *
     * @param alias   The alias
     * @param command The command instance
     * @return the command that was added
     */
    @SuppressWarnings({"unchecked", "unused"})
    public static Command add(@NotNull String alias, @NotNull Command command) {
        try {
            Field field = SimpleCommandMap.class.getDeclaredField("knownCommands");
            field.setAccessible(true);

            CommandMap map = retrieveMap();

            Map<String, Command> knownCommands
                    = (Map<String, Command>) field.get(map);

            Command prev1 = knownCommands.put("cf:" + alias, command);
            Command prev2 = knownCommands.put(alias, command);

            field.set(map, knownCommands);

            if (prev1 == null) return prev2;
            else return prev1;
        } catch (NoSuchFieldException ex) {
            greenBank.getLogger().severe("knownCommands field not found for registry");
            greenBank.getLogger().severe("update your server or switch to a supported server platform");
            greenBank.getLogger().severe(ex.toString());
            return null;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    /**
     * Unregister a command from the map.
     *
     * @param command The command
     */
    protected static void unregister(@NotNull Command command) {
        CommandMap map = retrieveMap();

        if (map != null) {
            command.unregister(map);
        }
    }
}
