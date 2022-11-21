package org.flameyosflow.greenbank.api.entities;

import org.flameyosflow.greenbank.GreenBankMain;
import org.flameyosflow.greenbank.IGreenBank;
import org.flameyosflow.greenbank.commands.IGreenbankCommand;
import org.flameyosflow.greenbank.economy.layers.GreenbankLayer;
import org.flameyosflow.greenbank.economy.layers.GreenbankLayers;

import org.flameyosflow.greenbank.api.errors.MaximumBalException;
import org.flameyosflow.greenbank.events.custom.UpdatePlayerBalance;
import org.flameyosflow.greenbank.events.custom.UpdatePlayerBalance.Cause;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class Account {
    private long lastOnlineActivity;
    private Player base;

    public Player getBase() {
        return base;
    }

    public Account(final Player base, final GreenBankMain greenBank) {
        if (this.getBase().isOnline()) {
            lastOnlineActivity = System.currentTimeMillis();
        }
    }
}
