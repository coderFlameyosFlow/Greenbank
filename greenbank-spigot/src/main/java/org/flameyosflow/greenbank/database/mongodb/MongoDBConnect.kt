package org.flameyosflow.greenbank.database.mongodb

import org.flameyosflow.greenbank.database.DatabaseConnector

abstract class MongoDBConnect<T> : DatabaseConnector<T> {
    override fun edit(uuid: String, value: Any, identifier: String) {
        setDocumentPlayerData(value, identifier, uuid)
    }

    abstract fun getDocumentPlayerData(identifier: String, uuid: String): Any?
    abstract fun setDocumentPlayerData(value: Any, identifier: String, uuid: String): Void?
}