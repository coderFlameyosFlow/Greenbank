package org.flameyosflow.greenbank.economy.layers;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.math.BigDecimal;

public interface GreenbankLayer {
    String getName();

    String getBackendName();

    void enable(Plugin plugin);

    void disable();

    String getPluginName();

    String getPluginVersion();

    boolean hasAccount(OfflinePlayer player);

    boolean hasWorldAccount(OfflinePlayer player, String worldName);

    boolean createPlayerAccount(OfflinePlayer player);

    BigDecimal getPlayerBalance(OfflinePlayer player);

    BigDecimal getWorldPlayerBalance(OfflinePlayer player, String worldName);

    boolean depositToPlayer(OfflinePlayer player, BigDecimal amount);

    boolean depositToPlayerWorld(OfflinePlayer player, BigDecimal amount, String worldName);

    boolean withdrawToPlayer(OfflinePlayer player, BigDecimal amount);

    boolean withdrawToPlayerWorld(OfflinePlayer player, BigDecimal amount, String worldName);;

    default boolean set(OfflinePlayer player, BigDecimal amount) {
        if (!withdrawToPlayer(player, getPlayerBalance(player))) {
            return false;
        }
        return amount.equals(BigDecimal.ZERO) || depositToPlayer(player, amount);
    }
}
