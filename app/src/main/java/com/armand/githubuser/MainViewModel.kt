package com.armand.githubuser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _searchList = MutableLiveData<ArrayList<Items>>()
    val getSearchList: LiveData<ArrayList<Items>> = _searchList

    private val _userDetail = MutableLiveData<ResponseDetailUser>()
    val getUserDetail: LiveData<ResponseDetailUser> = _userDetail

    private val _followers = MutableLiveData<ArrayList<ResponseFollow>>()
    val getFollowers: LiveData<ArrayList<ResponseFollow>> = _followers

    private val _following = MutableLiveData<ArrayList<ResponseFollow>>()
    val getFollowing: LiveData<ArrayList<ResponseFollow>> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val getIsLoading: LiveData<Boolean> = _isLoading

    fun searchUser(username: String) {
        setLoading(true)
        ApiConfig.getApiService().search(username).enqueue(object : Callback<ResponseSearch> {
            override fun onResponse(call: Call<ResponseSearch>, response: Response<ResponseSearch>) {
                setLoading(false)
                if (response.isSuccessful) {
                    _searchList.value = (response.body()?.items ?: arrayListOf()) as ArrayList<Items>?
                }
            }

            override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                setLoading(false)
                Log.e(TAG, "Failed to search users: ${t.message}")
            }
        })
    }

    fun detailUser(username: String) {
        setLoading(true)
        ApiConfig.getApiService().detailUser(username).enqueue(object : Callback<ResponseDetailUser> {
            override fun onResponse(call: Call<ResponseDetailUser>, response: Response<ResponseDetailUser>) {
                setLoading(false)
                if (response.isSuccessful) {
                    _userDetail.value = response.body()
                }
            }

            override fun onFailure(call: Call<ResponseDetailUser>, t: Throwable) {
                setLoading(false)
                Log.e(TAG, "Failed to get user details: ${t.message}")
            }
        })
    }

    fun followers(username: String) {
        setLoading(true)
        ApiConfig.getApiService().followers(username).enqueue(object : Callback<ArrayList<ResponseFollow>> {
            override fun onResponse(call: Call<ArrayList<ResponseFollow>>, response: Response<ArrayList<ResponseFollow>>) {
                setLoading(false)
                if (response.isSuccessful) {
                    _followers.value = response.body() ?: arrayListOf()
                }
            }

            override fun onFailure(call: Call<ArrayList<ResponseFollow>>, t: Throwable) {
                setLoading(false)
                Log.e(TAG, "Failed to get followers: ${t.message}")
            }
        })
    }

    fun following(username: String) {
        setLoading(true)
        ApiConfig.getApiService().following(username).enqueue(object : Callback<ArrayList<ResponseFollow>> {
            override fun onResponse(call: Call<ArrayList<ResponseFollow>>, response: Response<ArrayList<ResponseFollow>>) {
                setLoading(false)
                if (response.isSuccessful) {
                    _following.value = response.body() ?: arrayListOf()
                }
            }

            override fun onFailure(call: Call<ArrayList<ResponseFollow>>, t: Throwable) {
                setLoading(false)
                Log.e(TAG, "Failed to get following: ${t.message}")
            }
        })
    }

    private fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}
