package com.example.computershop.data.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.computershop.domain.Client
import com.example.computershop.domain.Component
import com.example.computershop.domain.Order

class DatabaseHelper(context: Context)
    : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "database.db"
        private const val DB_VERSION = 14
    }

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL(
            """
      CREATE TABLE User (
        idUser INTEGER PRIMARY KEY AUTOINCREMENT,
        Username TEXT UNIQUE, 
        Password TEXT
      )
    """
        )

        db?.execSQL(
            """
      CREATE TABLE Manager (
        idMananger INTEGER PRIMARY KEY AUTOINCREMENT,
        Username TEXT, 
        Password TEXT
      )
    """
        )


        db?.execSQL(
            """
      CREATE TABLE Client (
        idClient INTEGER PRIMARY KEY AUTOINCREMENT,
        Name TEXT NOT NULL,
        Surname TEXT NOT NULL,
        PhoneNumber TEXT NOT NULL,
        Email TEXT NOT NULL
      )
    """
        )



        db?.execSQL(
            """
      CREATE TABLE Component (
        idComponent INTEGER PRIMARY KEY AUTOINCREMENT,
        Type TEXT NOT NULL,          
        Name TEXT NOT NULL,          
        Price REAL NOT NULL,        
        ImageURI TEXT NOT NULL,
        idOrderInfo INTEGER,
        FOREIGN KEY (idOrderInfo) REFERENCES OrderInfo (idOrderInfo)
      )
    """
        )

        db?.execSQL(
            """
      CREATE TABLE OrderInfo (
        idOrderInfo INTEGER PRIMARY KEY AUTOINCREMENT,
        Description TEXT NOT NULL,
        Name TEXT NOT NULL,
        Price REAL NOT NULL,
        ImageURI TEXT,
        created_at TEXT,
        idEmployee INTEGER,
        idClient INTEGER,
        idStatus INTEGER,
        FOREIGN KEY (idEmployee) REFERENCES Employee(idEmployee),
        FOREIGN KEY (idClient) REFERENCES Client(idClient),
        FOREIGN KEY (idStatus) REFERENCES Status(idStatus)
      )
    """
        )

        db?.execSQL(
            """
            CREATE TRIGGER IF NOT EXISTS set_created_at
            AFTER INSERT ON OrderInfo
            BEGIN
                UPDATE OrderInfo
                SET created_at = STRFTIME('%Y-%m-%d', 'now')
                WHERE rowid = NEW.rowid;
            END;
            """
        )

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Client")
        db?.execSQL("DROP TABLE IF EXISTS Employee")
        db?.execSQL("DROP TABLE IF EXISTS Component")
        db?.execSQL("DROP TABLE IF EXISTS OrderInfo")
        db?.execSQL("DROP TABLE IF EXISTS User")
        db?.execSQL("DROP TABLE IF EXISTS Manager")
        onCreate(db)

    }


    fun getOrdersByStatus(status: Int): List<Order> {
        val db = readableDatabase
        val orders = mutableListOf<Order>()
        val cursor = db.rawQuery(
            "SELECT * FROM OrderInfo WHERE idStatus = ?",
            arrayOf(status.toString())
        )
        while (cursor.moveToNext()) {
            val orderId = cursor.getInt(cursor.getColumnIndexOrThrow("idOrderInfo"))
            orders.add(
                Order(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("idOrderInfo")),
                    description = cursor.getString(cursor.getColumnIndexOrThrow("Description")),
                    name = cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                    totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("Price")),
                    imageUri = cursor.getString(cursor.getColumnIndexOrThrow("ImageURI")),
                    idEmployee = cursor.getInt(cursor.getColumnIndexOrThrow("idEmployee")),
                    idClient = cursor.getInt(cursor.getColumnIndexOrThrow("idClient")),
                    idStatus = cursor.getInt(cursor.getColumnIndexOrThrow("idStatus")),
                    components = getComponentsByOrder(orderId),
                    createdAt = cursor.getString(cursor.getColumnIndexOrThrow("created_at"))
                )
            )
        }
        cursor.close()
        return orders
    }


    fun getOrdersByPriceRange(minPrice: Double = 0.0, maxPrice: Double = 10000000000.0): List<Order> {
        val db = readableDatabase
        val orders = mutableListOf<Order>()
        val cursor = db.rawQuery(
            "SELECT * FROM OrderInfo WHERE Price BETWEEN ? AND ?",
            arrayOf(minPrice.toString(), maxPrice.toString())
        )
        while (cursor.moveToNext()) {
            val orderId = cursor.getInt(cursor.getColumnIndexOrThrow("idOrderInfo"))
            orders.add(
                Order(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("idOrderInfo")),
                    description = cursor.getString(cursor.getColumnIndexOrThrow("Description")),
                    name = cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                    totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("Price")),
                    imageUri = cursor.getString(cursor.getColumnIndexOrThrow("ImageURI")),
                    idEmployee = cursor.getInt(cursor.getColumnIndexOrThrow("idEmployee")),
                    idClient = cursor.getInt(cursor.getColumnIndexOrThrow("idClient")),
                    idStatus = cursor.getInt(cursor.getColumnIndexOrThrow("idStatus")),
                    components = getComponentsByOrder(orderId),
                    createdAt = cursor.getString(cursor.getColumnIndexOrThrow("created_at"))
                )
            )
        }
        cursor.close()
        return orders
    }





    fun isUsernameTaken(username: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT 1 FROM User WHERE Username = ?", arrayOf(username))
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }

    fun insertUser(login: String, password: String, name: String, surname: String, phoneNumber: String, email: String): Long {
        val db = writableDatabase
        var userId: Long = -1
        db.beginTransaction()
        try {
            val userValues = ContentValues().apply {
                put("Username", login)
                put("Password", password)
            }
            userId = db.insertOrThrow("User", null, userValues)

            if (userId != -1L) {
                val clientValues = ContentValues().apply {
                    put("Name", name)
                    put("Surname", surname)
                    put("PhoneNumber", phoneNumber)
                    put("Email", email)
                }
                db.insert("Client", null, clientValues)
            }

            db.setTransactionSuccessful()
        } catch (e: SQLiteConstraintException) {
            throw Exception("Пользователь с таким логином уже существует")
        }

        finally {
            db.endTransaction()
        }
        return userId
    }


    fun readUser(login: String, password: String): Int? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT idUser FROM User WHERE username = ? AND password = ?", arrayOf(login, password))
        return if (cursor.moveToFirst()) {
            val userId = cursor.getInt(cursor.getColumnIndexOrThrow("idUser"))
            cursor.close()
            userId
        } else {
            cursor.close()
            null
        }
    }


    fun insertManager(login: String, password: String) : Long {
        val values = ContentValues().apply {
            put("Username", login)
            put("Password", password)
        }
        val db = writableDatabase
        return db.insert("Manager", null, values)
    }


    fun readManager(login: String, password: String) : Int? {
        val db = readableDatabase
        val selection = "Username = ? AND Password = ?"
        val selectionArgs = arrayOf(login, password)
        val cursor = db.query("Manager", null, selection, selectionArgs, null, null, null)
        return if (cursor.moveToFirst()) {
            val userId = cursor.getInt(cursor.getColumnIndexOrThrow("Username"))
            cursor.close()
            userId
        } else {
            cursor.close()
            null
        }
    }

    fun insertComponents(components: List<Component>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            for (component in components) {
                val values = ContentValues().apply {
                    put("Name", component.name)
                    put("ImageURI", component.imageUri)
                    put("Type", component.type)
                    put("Price", component.price)
                    put("idOrderInfo", component.idOrder)
                }
                val componentId = db.insert("Component", null, values)
                Log.d("insertComponents", "Inserted component with id: $componentId")

            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }


    fun insertOrder(
        description: String,
        name: String,
        price: Double,
        imageUri: String?,
        idEmployee: Int?,
        idClient: Int,
        idStatus: Int,
        components: List<Component>
    ): Long {
        val db = writableDatabase
        var orderId: Long = -1
        db.beginTransaction()
        try {
            val values = ContentValues().apply {
                put("Description", description)
                put("Name", name)
                put("Price", price)
                put("ImageURI", imageUri)
                put("idEmployee", idEmployee)
                put("idClient", idClient)
                put("idStatus", idStatus)

            }
            orderId = db.insert("OrderInfo", null, values)

            if (orderId != -1L) {
                val updatedComponents = components.map { it.copy(idOrder = orderId.toInt()) }
                insertComponents(updatedComponents)
            }

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
        return orderId
    }

    fun getComponentsByOrder(idOrder: Int): List<Component> {
        val db = readableDatabase
        val components = mutableListOf<Component>()
        val cursor = db.rawQuery(
            "SELECT * FROM Component WHERE idOrderInfo = ?",
            arrayOf(idOrder.toString())
        )
        while (cursor.moveToNext()) {
            val component = Component(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("idComponent")),
                name = cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                imageUri = cursor.getString(cursor.getColumnIndexOrThrow("ImageURI")),
                idOrder = cursor.getInt(cursor.getColumnIndexOrThrow("idOrderInfo")),
                type = cursor.getString(cursor.getColumnIndexOrThrow("Type")),
                price = cursor.getDouble(cursor.getColumnIndexOrThrow("Price"))
            )
            Log.d("getComponentsByOrder", "Fetched component: $component")
            components.add(component)
        }
        cursor.close()
        return components
    }


    fun getOrderById(orderId: Int): Order? {
        val db = readableDatabase
        var order: Order? = null
        val query = "SELECT * FROM OrderInfo WHERE idOrderInfo = ?"

        val cursor = db.rawQuery(query, arrayOf(orderId.toString()))
        if (cursor.moveToFirst()) {
            order = Order(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("idOrderInfo")),
                description = cursor.getString(cursor.getColumnIndexOrThrow("Description")),
                name = cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("Price")),
                imageUri = cursor.getString(cursor.getColumnIndexOrThrow("ImageURI")),
                idEmployee = cursor.getInt(cursor.getColumnIndexOrThrow("idEmployee")),
                idClient = cursor.getInt(cursor.getColumnIndexOrThrow("idClient")),
                idStatus = cursor.getInt(cursor.getColumnIndexOrThrow("idStatus")),
                components = getComponentsByOrder(orderId),
                createdAt = cursor.getString(cursor.getColumnIndexOrThrow("created_at"))
            )
        }
        cursor.close()
        return order
    }


    fun getAllOrders(): List<Order> {
        val db = readableDatabase
        val orders = mutableListOf<Order>()
        val cursor = db.rawQuery(
            "SELECT * FROM OrderInfo",
            null
        )
        while (cursor.moveToNext()) {
            val orderId = cursor.getInt(cursor.getColumnIndexOrThrow("idOrderInfo"))
            orders.add(
                Order(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("idOrderInfo")),
                    description = cursor.getString(cursor.getColumnIndexOrThrow("Description")),
                    name = cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                    totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("Price")),
                    imageUri = cursor.getString(cursor.getColumnIndexOrThrow("ImageURI")),
                    idEmployee = cursor.getInt(cursor.getColumnIndexOrThrow("idEmployee")),
                    idClient = cursor.getInt(cursor.getColumnIndexOrThrow("idClient")),
                    idStatus = cursor.getInt(cursor.getColumnIndexOrThrow("idStatus")),
                    components = getComponentsByOrder(orderId),
                    createdAt = cursor.getString(cursor.getColumnIndexOrThrow("created_at"))
                )
            )
        }
        cursor.close()
        return orders
    }


    fun getOrdersByClient(idClient: Int): List<Order> {
        val db = readableDatabase
        val orders = mutableListOf<Order>()
        val cursor = db.rawQuery(
            "SELECT * FROM OrderInfo WHERE idClient = ?",
            arrayOf(idClient.toString())
        )
        while (cursor.moveToNext()) {
            val orderId = cursor.getInt(cursor.getColumnIndexOrThrow("idOrderInfo"))
            orders.add(
                Order(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("idOrderInfo")),
                    description = cursor.getString(cursor.getColumnIndexOrThrow("Description")),
                    name = cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                    totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("Price")),
                    imageUri = cursor.getString(cursor.getColumnIndexOrThrow("ImageURI")),
                    idEmployee = cursor.getInt(cursor.getColumnIndexOrThrow("idEmployee")),
                    idClient = cursor.getInt(cursor.getColumnIndexOrThrow("idClient")),
                    idStatus = cursor.getInt(cursor.getColumnIndexOrThrow("idStatus")),
                    components = getComponentsByOrder(orderId),
                    createdAt = cursor.getString(cursor.getColumnIndexOrThrow("created_at"))
                )
            )
        }
        cursor.close()
        return orders
    }


    fun getClientInfo(idClient: Int): Client? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM Client WHERE idClient = ?",
            arrayOf(idClient.toString())
        )

        return if (cursor.moveToFirst()) {
            Client(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("idClient")),
                name = cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                surname = cursor.getString(cursor.getColumnIndexOrThrow("Surname")),
                phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("PhoneNumber")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("Email"))
            )
        } else {
            null
        }
    }

    fun updateOrderStatus(orderId: Int, newStatus: Int) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("idStatus", newStatus)
        }
        db.update("OrderInfo", contentValues, "idOrderInfo = ?", arrayOf(orderId.toString()))
    }

    fun deleteOrderById(orderId: Int): Boolean {
        val db = writableDatabase
        var success = false
        db.beginTransaction()
        try {
            db.delete("Component", "idOrderInfo = ?", arrayOf(orderId.toString()))

            val rowsDeleted = db.delete("OrderInfo", "idOrderInfo = ?", arrayOf(orderId.toString()))
            success = rowsDeleted > 0

            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
        return success
    }


}