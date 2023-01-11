Create table if not exists account (
    uuid Text(36),
    hasInfiniteMoney boolean default false,
    balance BigInt(9223372036854755807) default 0,
    bankaccount BigInt(9223372036854755807) default 0
)