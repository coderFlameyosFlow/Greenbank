# FEATURES:
If you want any more please make a feature list.
PLEASE NOTE (and it's a very important note):

**This plugin ONLY SUPPORTS MONGODB (it's 1.0.0, I'm not going to add every feature in the first version, that's too much work).**
Although I will add support for other databases.
Check the **TODO** list for more information.

### General:
- Fast and Asynchronous compilation and processing of code.
- BoostedYAML implementation of YAML files (Fast, and easy, Configured to use the SpigotSterilizer).
- Uses and Supports the VaultAPI (You need [Vault](https://www.spigotmc.org/resources/vault.34315/) plugin to work).

### Database:
- MongoDB, uses **asynchronous processing and drivers**.
- That's it, although **SQLite is the next database**.

### Administrative
- Ability to give money to players.
- Ability to take money from players.
- Ability to set a player's balance to a certain **amount of money**.
- Ability to reset a player's balance to the default starting balance (Configurable via config.yml).


### Economy:
- Ability to check your balance.
- Ability to check other player's balances.
- Ability to pay others money.
- Ability to configure custom symbols for currencies.

### UUID support:
- Using minecraft's UUIDS, you'll never have to worry about losing your progress after changing your name!

### Others:
- Needs to be configured minimally, although **THIS PLUGIN IS VERY CUSTOMIZABLE, EVEN THE MESSAGES**
- Balance ranges from -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807.


# TODO Features:
These are here because they were planned, but weren't made because they were hard to make, usually made without notice ;)

### Hooks:
- **PlaceholderAPI** hook
- **WorldServer** hook
- and most economy dependent plugins.

### Systems:
- GUI **banking and finance system**
- **Interest and Taxes** system
- **Experience and money Trading** system
- **Withdrawable and Depositable** system via items (using paper to withdraw money from your balance)
- **Multiple database support** system



**Multiple database support system** means to check the database, and if it's a certain database then use that database.

let's say I'm using SQLite:
The code checks if I'm using MongoDB, since I'm using SQLite, The check fails.
although the check that checks if I'm using SQLite does succeed

therefore the code does not run off MongoDB, it runs off SQLite.
This SHOULD be implemented after SQLite.



### Databases:
This is ranked top to bottom from the Databases that will be made first.
- SQLite
- MySQL
- MariaDB
- H2
- **OTHERS CAN BE REQUESTED OR MADE VIA PULL REQUEST.**
