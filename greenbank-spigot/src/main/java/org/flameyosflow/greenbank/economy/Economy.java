package org.flameyosflow.greenbank.economy;

import org.bukkit.OfflinePlayer;

public interface Economy {
    /**
     * Format amount into a human-readable String This provides translation into
     * economy specific formatting to improve consistency between plugins.
     *
     * @param amount to format
     * @return Human-readable string describing amount
     */
    String format(long amount);

    /**
     * Checks if this player has an account on the server yet
     * This will always return true if the player has joined the server at least once
     * as all major economy plugins auto-generate a player account when the player joins the server
     *
     * @param playerId to check
     * @return if the player has an account
     */
    boolean hasAccount(String playerId);

    /**
     * Checks if this player has an account on the server yet
     * This will always return true if the player has joined the server at least once
     * as all major economy plugins auto-generate a player account when the player joins the server
     *
     * @param player to check
     * @return if the player has an account
     */
    boolean hasAccount(OfflinePlayer player);

    /**
     * Gets balance of a player
     *
     * @param playerId of the player
     * @return Amount currently held in players account
     */
    long getBalance(String playerId);

    /**
     * Gets balance of a player
     *
     * @param player of the player
     * @return Amount currently held in players account
     */
    long getBalance(OfflinePlayer player);

    /**
     * Checks if the player account has the amount - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param playerId to check
     * @param amount to check for
     * @return True if <b>player</b> has <b>amount</b>, False else wise
     */
    boolean has(String playerId, long amount);

    /**
     * Checks if the player account has the amount - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param player to check
     * @param amount to check for
     * @return True if <b>player</b> has <b>amount</b>, False else wise
     */
    boolean has(OfflinePlayer player, long amount);

    /**
     * Withdraw an amount from a player - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param player to withdraw from
     * @param amount Amount to withdraw
     */
    void withdrawPlayer(OfflinePlayer player, long amount);

    /**
     * Deposit an amount to a player - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param player to deposit to
     * @param amount Amount to deposit
     */
    void depositPlayer(OfflinePlayer player, long amount);

    /**
     * Attempts to create a player account for the given player
     *
     * @param player OfflinePlayer
     */
    void createPlayerAccount(OfflinePlayer player);
}
