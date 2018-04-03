package com.example.maciej.shoppinglist

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class MySQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    val allProducts: ArrayList<Product>
        get() {
            val products = ArrayList<Product>()
            val query = "SELECT  * FROM " + TABLE_PRODUCTS
            val db = this.writableDatabase
            val cursor = db.rawQuery(query, null)
            var prod: Product
            if (cursor.moveToFirst()) {
                do {
                    prod = Product(cursor.getLong(0), cursor.getString(1))
                    products.add(prod)
                } while (cursor.moveToNext())
            }

            return products
        }

    override fun onCreate(db: SQLiteDatabase) {

        val CREATE_PRODUCT_TABLE = "CREATE TABLE products ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT )"

        db.execSQL(CREATE_PRODUCT_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS products")

        this.onCreate(db)
    }

    fun addProduct(prodName: String) : Product{
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_NAME, prodName)

        var id = db.insert(TABLE_PRODUCTS, null, values)

        db.close()

        return Product(id,prodName)
    }

    fun updateProduct(prod: Product): Int {

        val db = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_NAME, prod.Desc)

        val i = db.update(TABLE_PRODUCTS,
                values,
                KEY_ID + " = ?",
                arrayOf(prod.Id.toString()))

        db.close()
        return i
    }

    fun deleteProduct(prod: Product) {

        val db = this.writableDatabase

        db.delete(TABLE_PRODUCTS,
                KEY_ID + " = ?",
                arrayOf(prod.Id.toString()))

        db.close()
    }

    companion object {

        private val DATABASE_VERSION = 2
        private val DATABASE_NAME = "ProductDB"

        private val TABLE_PRODUCTS = "products"
        private val KEY_ID = "id"
        private val KEY_NAME = "name"
    }

}
