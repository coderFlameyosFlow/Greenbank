/*
 * The balancetop command to show the people with the highest balances in the world or the server.
 *
 * This is not currently implemented, and I don't exactly know how to implement it
 * Although this is a base for when I figure out how to make this or someone else contributes.
 *
 * The file has no usages due to me not being able to make this.
 * Please consider finishing this if you know how to, or this will take a tad bit longer.
 */

package org.flameyosflow.greenbank.commands;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import static com.mongodb.client.model.Filters.lt;

import org.jetbrains.annotations.NotNull;

import org.flameyosflow.greenbank.GreenBankMain;

public final class BalanceTopCommand extends GreenBankCommand {
    private final GreenBankMain greenBank;

    public BalanceTopCommand(GreenBankMain greenBank) {
        this.greenBank = greenBank;
    }

    private final byte playersPerPage = 10;

    private Bson projectionHandler() {
        return Projections.fields(
               Projections.include("balance"),
               Projections.excludeId());
    }

    private MongoCursor<Document> cursorHandler(Bson projectionFields) {
        return greenBank.getMongoConnect().getPlayerDataCollection().find(lt("runtime", 15))
                .projection(projectionFields)
                .sort(Sorts.descending("balance"))
                .iterator();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, Command command, String label, String[] args) {
        // TODO: there should be a better and more readable way to implement this command.
        // TODO: implement this command before doing the above.

        try (MongoCursor<Document> cursor = cursorHandler(projectionHandler())) {
            try {
                while (cursor.hasNext()) {
                    Document document = cursor.next();
                    document.append(null, null);
                }
            } finally {
                cursor.close();
            }
        }

        return false;
    }
}
