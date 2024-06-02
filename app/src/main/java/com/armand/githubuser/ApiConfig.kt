package com.armand.githubuser

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


object ApiConfig {
    private const val BASE_URL = "https://api.github.com/"

    // Define your token securely (e.g., from secure storage or environment variables)
    private const val token = "GithubTokenHere"

    // Custom Interceptor to add Authorization header with token
    class TokenInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request: Request = chain.request().newBuilder()
                .header("Authorization", "Bearer $token")
                .build()

            return chain.proceed(request)
        }
    }

    fun getApiService(): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        val tokenInterceptor = TokenInterceptor()

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(tokenInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }

    interface ApiService {
        @GET("search/users")
        fun search(
            @Query("q") username: String
        ): Call<ResponseSearch>

        @GET("users/{username}")
        fun detailUser(
            @Path("username") username: String
        ): Call<ResponseDetailUser>

        @GET("users/{username}/followers")
        fun followers(
            @Path("username") username: String
        ): Call<ArrayList<ResponseFollow>>

        @GET("users/{username}/following")
        fun following(
            @Path("username") username: String
        ): Call<ArrayList<ResponseFollow>>
    }
}
