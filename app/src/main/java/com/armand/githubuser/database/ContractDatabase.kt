package com.armand.githubuser.database

import android.provider.BaseColumns

internal class ContractDatabase {

    internal class FavoriteColumns : BaseColumns {
        companion object {
            const val TABLE = "favorite"
            const val ID = "id"
            const val AVATAR = "avatar_url"
            const val URL = "html_url"
            const val LOGIN = "login"
        }
    }
}