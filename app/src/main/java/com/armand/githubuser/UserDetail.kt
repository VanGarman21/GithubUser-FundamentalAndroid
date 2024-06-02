package com.armand.githubuser

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.armand.githubuser.database.ContractDatabase
import com.armand.githubuser.database.ContractDatabase.FavoriteColumns.Companion.TABLE
import com.armand.githubuser.database.HelperDatabase
import com.armand.githubuser.database.HelperFavorite
import com.armand.githubuser.databinding.UserDetailBinding
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator

class UserDetail : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
    private lateinit var viewModelSetting: SettingViewModel

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: UserDetailBinding

    private var listFavorite = ArrayList<ResponseDetailUser>()
    private lateinit var helper: HelperFavorite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupViewPager()
        observeLoadingState()
        ThemeCheck()

        username = intent.getStringExtra(EXTRA_USER).toString()
        showViewModel()

        helper = HelperFavorite.getInstance(applicationContext)
        helper.open()
    }

    private fun ThemeCheck() {
        val setting = Setting.getInstance(dataStore)
        viewModelSetting = ViewModelProvider(this, ViewModel(setting))[SettingViewModel::class.java]

        viewModelSetting.getThemeSettings().observe(this@UserDetail) { isDarkModeActive ->
            val nightMode = if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            title = getString(R.string.detail_user)
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupViewPager() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = getString(TAB_TITLES[position])
        }.attach()
    }

    private fun observeLoadingState() {
        viewModel.getIsLoading.observe(this, this::showLoading)
    }

    private fun showViewModel() {
        viewModel.detailUser(username)
        viewModel.getUserDetail.observe(this) { detailUser ->
            Glide.with(this)
                .load(detailUser.avatarUrl)
                .skipMemoryCache(true)
                .into(binding.imgAvatar)

            binding.tvName.text = detailUser.name
            binding.tvUsername.text = detailUser.login
            binding.tvFollowersValue.text = detailUser.followers
            binding.tvFollowingValue.text = detailUser.following

            binding.imageFavorite.isFavorite = favoriteExist(username)

            binding.imageFavorite.setOnFavoriteChangeListener { _, favorite ->
                if (favorite) {
                    listFavorite = helper.queryAll()
                    helper.insert(detailUser)
                } else {
                    listFavorite = helper.queryAll()
                    helper.delete(username)
                }
                helper.close()
            }

        }
    }

    private fun favoriteExist(user: String): Boolean {
        val dbHelper = HelperDatabase(this)
        val database = dbHelper.readableDatabase

        val projection = arrayOf(ContractDatabase.FavoriteColumns.LOGIN)
        val selection = "${ContractDatabase.FavoriteColumns.LOGIN} = ?"
        val selectionArgs = arrayOf(user)

        val cursor = database.query(
            TABLE,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null,
            "1"
        )

        val exists = cursor.count > 0

        cursor.close()
        database.close()

        return exists
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        var username = String()

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }
}