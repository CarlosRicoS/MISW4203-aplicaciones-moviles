package co.edu.uniandes.miso.vinilos

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.edu.uniandes.miso.vinilos.databinding.ActivityMainBinding
import co.edu.uniandes.miso.vinilos.view.DrawerItem
import co.edu.uniandes.miso.vinilos.view.adapters.DrawerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var isSearchInputVisible = false
    private var currentMenu: Menu? = null

    private val topLevelDestinations = setOf(
        R.id.albumsListFragment,
        R.id.collectorsListFragment,
        R.id.performerListFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupNavigation()
        setupDrawer()
        setupSearchInput()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        currentMenu = menu
        updateFilterVisibility()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                toggleSearchBar()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfig = AppBarConfiguration(topLevelDestinations, binding.drawerLayout)
        binding.toolbar.setupWithNavController(navController, appBarConfig)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.toolbar.title = destination.label
            binding.searchInput.setText("")
            clearSearchTextBox(EditorInfo.IME_ACTION_DONE)
            if (isSearchInputVisible) toggleSearchBar()
            updateFilterVisibility()
        }
    }

    private fun setupDrawer() {
        binding.drawerRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.drawerRecyclerView.adapter = DrawerAdapter(getDrawerItems()) { destinationId ->
            binding.drawerLayout.postDelayed({
                binding.drawerLayout.closeDrawers()
                navController.navigate(destinationId)
            }, 150)
        }
    }

    private fun setupSearchInput() {
        binding.searchInputLayout.apply {
            visibility = View.GONE
            alpha = 0f
        }
        binding.searchInput.setOnEditorActionListener { _, actionId, _ ->
            clearSearchTextBox(actionId)
        }
    }

    private fun updateFilterVisibility() {
        currentMenu?.findItem(R.id.action_filter)?.isVisible =
            navController.currentDestination?.id in topLevelDestinations
    }

    private fun getDrawerItems(): List<DrawerItem> = listOf(
        DrawerItem.MenuItem(
            R.id.albumsListFragment,
            R.drawable.music_note_24dp,
            getString(R.string.albums_menu_title)
        ),
        DrawerItem.Divider,
        DrawerItem.MenuItem(
            R.id.collectorsListFragment,
            R.drawable.person_24dp,
            getString(R.string.collectors_menu_title)
        ),
        DrawerItem.Divider,
        DrawerItem.MenuItem(
            R.id.performerListFragment,
            R.drawable.artist_24dp,
            getString(R.string.performer_menu_title)
        )
    )

    private fun clearSearchTextBox(actionId: Int): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {
            hideKeyboard()
            binding.searchInput.clearFocus()
            // TODO: trigger filter logic with 'query'
            return true
        }
        return false
    }

    private fun toggleSearchBar() {
        val searchLayout = binding.searchInputLayout

        if (!isSearchInputVisible) {
            searchLayout.visibility = View.VISIBLE
            searchLayout.animate().alpha(1f).setDuration(200).start()
            binding.searchInput.requestFocus()
            showKeyboard()
        } else {
            searchLayout.animate()
                .alpha(0f)
                .setDuration(200)
                .withEndAction {
                    searchLayout.visibility = View.GONE
                    binding.searchInput.setText("")
                    binding.searchInput.clearFocus()
                    hideKeyboard()
                }.start()
        }

        isSearchInputVisible = !isSearchInputVisible
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchInput.windowToken, 0)
    }

    private fun showKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.searchInput, InputMethodManager.SHOW_IMPLICIT)
    }
}
