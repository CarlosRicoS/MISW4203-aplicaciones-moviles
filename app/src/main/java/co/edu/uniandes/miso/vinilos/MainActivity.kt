package co.edu.uniandes.miso.vinilos

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.edu.uniandes.miso.vinilos.databinding.ActivityMainBinding
import co.edu.uniandes.miso.vinilos.view.DrawerItem
import co.edu.uniandes.miso.vinilos.view.adapters.DrawerAdapter
import co.edu.uniandes.miso.vinilos.view.interfaces.ToolbarActionHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var isFilterVisible = true
    private var isSaveVisible = false
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var actionFilter: MenuItem? = null
    private var actionSave: MenuItem? = null
    private var isSearchInputVisible = false

    private val topLevelDestinations = setOf(
        R.id.albumsListFragment,
        R.id.collectorsListFragment,
        R.id.performerListFragment,
        R.id.userSelectionFragment
    )

    private val formDestinations = setOf(
        R.id.new_album
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        actionFilter = menu.findItem(R.id.action_filter)
        actionSave = menu.findItem(R.id.action_save)
        actionFilter?.contentDescription = getString(R.string.toolbar_search_text)
        val icon = actionFilter?.icon
        icon?.mutate()?.setTint(ContextCompat.getColor(this, R.color.iconTint))
        updateFilterVisibility()
        updateSaveVisibility()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                toggleSearchBar()
                true
            }
            R.id.action_save -> {
                val toolbarActionHandler = getCurrentToolbarActionHandler()
                toolbarActionHandler?.onToolbarAction()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getCurrentToolbarActionHandler(): ToolbarActionHandler? {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        return navHostFragment
            ?.childFragmentManager
            ?.fragments
            ?.firstOrNull { it.isVisible && it is ToolbarActionHandler } as? ToolbarActionHandler
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.action_filter)?.isVisible = isFilterVisible
        menu?.findItem(R.id.action_save)?.isVisible = isSaveVisible
        return super.onPrepareOptionsMenu(menu)
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
            updateSaveVisibility()
        }
    }

    private fun setupDrawer() {
        binding.drawerRecyclerView.layoutManager = LinearLayoutManager(this)
        updateNavigationOptions()
    }

    fun updateNavigationOptions() {

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
        actionFilter?.isVisible =
            navController.currentDestination?.id in topLevelDestinations
    }

    private fun updateSaveVisibility() {
        actionSave?.isVisible =
            navController.currentDestination?.id in formDestinations
    }

    private fun getDrawerItems(): List<DrawerItem> {
        return listOf(
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
            ),
            DrawerItem.Divider,
            DrawerItem.MenuItem(
                R.id.userSelectionFragment,
                R.drawable.person_24dp,
                getString(R.string.user_selection_menu_title)
            )
        )
    }

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
