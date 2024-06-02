package com.armand.githubuser.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.armand.githubuser.ResponseDetailUser
import com.armand.githubuser.database.ContractDatabase.FavoriteColumns.Companion.AVATAR
import com.armand.githubuser.database.ContractDatabase.FavoriteColumns.Companion.ID
import com.armand.githubuser.database.ContractDatabase.FavoriteColumns.Companion.LOGIN
import com.armand.githubuser.database.ContractDatabase.FavoriteColumns.Companion.TABLE
import com.armand.githubuser.database.ContractDatabase.FavoriteColumns.Companion.URL

class HelperFavorite(context: Context) {

    private val databaseHelper: HelperDatabase = HelperDatabase(context)
    private lateinit var database: SQLiteDatabase

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()
    }

    fun queryAll(): ArrayList<ResponseDetailUser> {
        val arrayList: ArrayList<ResponseDetailUser> = ArrayList()
        val cursor: Cursor = database.query(
            DATABASE_TABLE, null,
            null,
            null,
            null,
            null,
            "$ID ASC",
            null
        )
        cursor.use {
            while (it.moveToNext()) {
                val favoriteUser = ResponseDetailUser()
                favoriteUser.id = it.getInt(it.getColumnIndexOrThrow(ID))
                favoriteUser.avatarUrl = it.getString(it.getColumnIndexOrThrow(AVATAR))
                favoriteUser.htmlUrl = it.getString(it.getColumnIndexOrThrow(URL))
                favoriteUser.login = it.getString(it.getColumnIndexOrThrow(LOGIN))

                arrayList.add(favoriteUser)
            }
        }
        return arrayList
    }

    fun insert(response: ResponseDetailUser): Long {
        val values = ContentValues().apply {
            put(ID, response.id)
            put(AVATAR, response.avatarUrl)
            put(URL, response.htmlUrl)
            put(LOGIN, response.login)
        }

        return database.insert(DATABASE_TABLE, null, values)
    }

    fun delete(username: String): Int {
        return database.delete(TABLE, "$LOGIN = ?", arrayOf(username))
    }

    companion object {
        private const val DATABASE_TABLE = TABLE

        @Volatile
        private var INSTANCE: HelperFavorite? = null

        fun getInstance(context: Context): HelperFavorite =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HelperFavorite(context)
            }
    }
}