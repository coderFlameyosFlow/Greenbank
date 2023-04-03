package com.greenbank.api.settings;

import com.greenbank.api.IGreenbank;
import dev.dejvokep.boostedyaml.YamlDocument;

public class MessagesSettings {
    private final YamlDocument messages;

    public MessagesSettings(IGreenbank plugin) {
        this.messages = plugin.getConfigFile();
    }

    public String getPlayerBalance() {
        return messages.getString("messages.player-balance");
    }

    public String getOtherPlayerBalance() {
        return messages.getString("messages.other-player-balance");
    }

    public String getMoneyGivenSuccess() {
        return messages.getString("messages.money-given-success");
    }

    public String getReceivedMoneySuccess() {
        return messages.getString("messages.received-money-success");
    }

    public String getGivenMoneySuccess() {
        return messages.getString("messages.given-money-success");
    }

    public String getSetMoneySuccess() {
        return messages.getString("messages.set-money-success");
    }

    public String getPlayerSetMoneySuccess() {
        return messages.getString("messages.player-money-set-success");
    }

    public String getPaidMoneySuccess() {
        return messages.getString("messages.money-paid-success");
    }

    public String getRemovedMoneySuccess() {
        return messages.getString("messages.removed-money-success");
    }

    public String getPlayerRemovedMoneySuccess() {
        return messages.getString("messages.player-removed-money-success");
    }

    public String getResetMoneySuccess() {
        return messages.getString("messages.reset-money-success");
    }

    public String getPlayerResetMoneySuccess() {
        return messages.getString("messages.player-reset-money-success");
    }

    public String getNotEnoughMoney() {
        return messages.getString("messages.not-enough-money");
    }

    public String getOnlyPlayerCommand() {
        return messages.getString("messages.only-player-command");
    }

    public String getPlayerDoesNotExist() {
        return messages.getString("messages.player-does-not-exist");
    }

    public String getNotEnoughPermissions() {
        return messages.getString("messages.not-enough-permissions");
    }
}