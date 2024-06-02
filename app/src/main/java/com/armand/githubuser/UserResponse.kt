package com.armand.githubuser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
    var username: String,
    var name: String,
    var avatar: Int,
    var repository: Int,
    var follower: Int,
    var following: Int
) : Parcelable
