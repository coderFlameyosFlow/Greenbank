package org.flameyosflow.greenbank.economy.layers;

import org.flameyosflow.greenbank.GreenBankMain;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstraction layer for economy abstraction layers.
 */
public final class GreenbankLayers {
    private final Map<String, GreenbankLayer> registeredLayers = new HashMap<>();
    private final List<String> availableLayers = new ArrayList<>();
    private GreenbankLayer selectedLayer = null;
    private Plugin plugin = (Plugin) GreenBankMain.getPlugin();
    private static boolean serverStarted = false;

    private GreenbankLayers() {
    }

    public void init() {
        if (!registeredLayers.isEmpty()) {
            throw new IllegalStateException("Economy layers have already been registered!");
        }

        registerLayer(new VaultSupportLayer());
    }

    public void onEnableSupport(final GreenBankMain plugin) {
        plugin.scheduleAsyncDelayedTask(() -> {
            serverStarted = true;
            for (final Plugin plugin2 : Bukkit.getPluginManager().getPlugins()) {
                if (!plugin2.isEnabled()) {
                    continue;
                }
                final GreenbankLayer layer = onPluginEnable(plugin);
                if (layer != null) {
                    plugin.logger.info("Greenbank found a compatible payment method: " + layer.getName() + " (v" + layer.getPluginVersion() + ")!");
                }
            }

            onServerLoad();
        });
    }

    public void onEnable(final GreenBankMain plugin) {
        Thread thread = new Thread(() -> {
            new BukkitRunnable() {
                @Override
                public void run() {
                    onEnableSupport(plugin);     
                }   
            }.runTaskAsynchronously(plugin);
        });
        thread.start();
    }

    public boolean isServerStarted() {
        return serverStarted;
    }

    public GreenbankLayer getSelectedLayer() {
        return selectedLayer;
    }

    public boolean isLayerSelected() {
        return getSelectedLayer() != null;
    }

    public GreenbankLayer onPluginEnableCheck(final Plugin plugin) {
        if (registeredLayers.get((Object) plugin.getName()) == null) {
            return null;
        }

        final GreenbankLayer layer = registeredLayers.get(plugin.getName());
        layer.enable(plugin);
        availableLayers.add(plugin.getName());
        if (selectedLayer != null) {
            return null;
        }

        selectedLayer = layer;
        selectedLayer.enable(plugin);
        return selectedLayer;
    }

    public GreenbankLayer onPluginEnable(final Plugin plugin) {
        Thread thread = new Thread(() -> {
            new BukkitRunnable() {

                @Override
                public void run() {
                    onPluginEnableCheck(plugin);
                }
                
            }.runTaskAsynchronously(GreenBankMain.plugin);
        });
        thread.start();
        return selectedLayer;
    }



    public boolean onPluginDisableCheck(final Plugin plugin) {
        if (!availableLayers.contains(plugin.getName())) {
            return false;
        }

        registeredLayers.get(plugin.getName()).disable();
        availableLayers.remove(plugin.getName());

        if (selectedLayer.getPluginName().equals(plugin.getName())) {
            selectedLayer = availableLayers.isEmpty() ? null : registeredLayers.get(availableLayers.get(0));
            if (selectedLayer != null && serverStarted) {
                selectedLayer.onServerLoad();
            }
            return true;
        }
        return false;
    }

    public boolean onPluginDisable(final Plugin plugin) {
        Thread thread = new Thread(() -> {
            new BukkitRunnable() {
                @Override
                public void run() {
                    onPluginDisableCheck(plugin);
                }
            }.runTaskAsynchronously(GreenBankMain.plugin);
        });
        thread.start();
        return true;
    }

    public void onServerLoadSupport() {
        if (!isLayerSelected() || getSelectedLayer().onServerLoad()) {
            return;
        }

        availableLayers.remove(getSelectedLayer().getPluginName());
        selectedLayer = null;
        if (!availableLayers.isEmpty()) {
            selectedLayer = registeredLayers.get(availableLayers.get(0));
            onServerLoad();
        }
    }

    public void onServerLoad() {
        Thread thread = new Thread(() -> {
            new BukkitRunnable() {
                @Override
                public void run() {
                    onServerLoad();
                }
            }.runTaskAsynchronously(GreenBankMain.plugin);
        });
        thread.start();
        return;
    }

    public void registerLayer(final GreenbankLayer greenbankLayer) {
        registeredLayers.put(greenbankLayer.getPluginName(), greenbankLayer);
    }
}
