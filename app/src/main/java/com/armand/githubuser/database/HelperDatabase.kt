package com.armand.githubuser.database

import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.armand.githubuser.database.ContractDatabase.FavoriteColumns.Companion.TABLE

internal class HelperDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        try {
            db.execSQL(SQL_CREATE_TABLE_FAVORITE)
        } catch (e: SQLException) {
            // Handle SQLite exception
            Log.e(TAG, "Error creating table: ${e.message}")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        try {
            db.execSQL("DROP TABLE IF EXISTS $TABLE")
            onCreate(db)
        } catch (e: SQLException) {
            // Handle SQLite exception
            Log.e(TAG, "Error upgrading database: ${e.message}")
        }
    }

    companion object {
        private const val TAG = "HelperDatabase"
        private const val DATABASE_NAME = "favoriteuser.db"
        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE $TABLE (" +
                "${ContractDatabase.FavoriteColumns.ID} INTEGER PRIMARY KEY," +
                "${ContractDatabase.FavoriteColumns.AVATAR} TEXT NOT NULL," +
                "${ContractDatabase.FavoriteColumns.URL} TEXT NOT NULL," +
                "${ContractDatabase.FavoriteColumns.LOGIN} TEXT NOT NULL" +
                ")"
    }
}
