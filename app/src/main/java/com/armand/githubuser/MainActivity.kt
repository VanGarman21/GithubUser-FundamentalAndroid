package com.armand.githubuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.armand.githubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
    private lateinit var viewModelSetting: SettingViewModel

    private val viewModel: MainViewModel by viewModels()
    private val adapter = UserAdapter()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ThemeCheck()
        setupViewModel()
        setupRecyclerView()
        observeLoading()
        viewModel.searchUser("A")

        binding.fabAdd.setOnClickListener {
            val i = Intent(this, FavoriteActivity::class.java)
            startActivity(i)
        }
    }

    private fun ThemeCheck() {
        val setting = Setting.getInstance(dataStore)
        viewModelSetting = ViewModelProvider(this, ViewModel(setting))[SettingViewModel::class.java]

        viewModelSetting.getThemeSettings().observe(this@MainActivity) { isDarkModeActive ->
            val nightMode = if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }
    }


    private fun setupViewModel() {
        viewModel.getSearchList.observe(this, { searchList ->
            handleSearchList(searchList)
        })
    }

    private fun handleSearchList(searchList: List<Items>) {
        if (searchList.isNotEmpty()) {
            showSearchResults(searchList)
        } else {
            showNoSearchResults()
        }
    }

    private fun showSearchResults(searchList: List<Items>) {
        binding.tvNotFound.visibility = View.GONE
        binding.rvUser.visibility = View.VISIBLE
        adapter.setData(searchList)
    }

    private fun showNoSearchResults() {
        binding.tvNotFound.visibility = View.VISIBLE
        binding.rvUser.visibility = View.GONE
        showToast("Not Found!")
    }


    private fun setupRecyclerView() {
        val layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(this, 2)
        } else {
            LinearLayoutManager(this)
        }

        binding.rvUser.layoutManager = layoutManager
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback { data -> selectedUser(data) }
    }

    private fun observeLoading() {
        viewModel.getIsLoading.observe(this, this::showLoading)
    }

    private fun selectedUser(user: Items) {
        val message = "Selected ${user.login}"
        showToast(message)
        navigateToUserDetail(user.login)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToUserDetail(username: String?) {
        val intent = Intent(this, UserDetail::class.java)
        intent.putExtra(UserDetail.EXTRA_USER, username)
        startActivity(intent)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        setupSearchView(searchManager, searchView)

        return true
    }

    private fun setupSearchView(searchManager: SearchManager, searchView: SearchView) {
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchUser(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.setting) {
            startActivity(Intent(this, SettingActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}