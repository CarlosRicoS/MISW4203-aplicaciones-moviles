package co.edu.uniandes.miso.vinilos

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.edu.uniandes.miso.vinilos.databinding.ActivityMainBinding
import co.edu.uniandes.miso.vinilos.view.DrawerItem
import co.edu.uniandes.miso.vinilos.view.adapters.DrawerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfig: AppBarConfiguration
    private var isSearchVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val topLevelDestinations = setOf(
            R.id.albumsListFragment,
            R.id.collectorsListFragment,
            R.id.artistsListFragment
        )
        appBarConfig = AppBarConfiguration(topLevelDestinations, binding.drawerLayout)
        binding.toolbar.setupWithNavController(navController, appBarConfig)

        // Setup RecyclerView with binding
        binding.drawerRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.drawerRecyclerView.adapter = DrawerAdapter(getDrawerItems()) { destinationId ->
            binding.drawerLayout.postDelayed({
                binding.drawerLayout.closeDrawers()
                val navController = (supportFragmentManager
                    .findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
                navController.navigate(destinationId)
            }, 150)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.toolbar.title = destination.label
            binding.searchInput.setText("")
            clearSearchTextBox(EditorInfo.IME_ACTION_DONE)
            if (isSearchVisible) {
                toggleSearchBar()
            }
        }

        binding.searchInput.setOnEditorActionListener { _, actionId, _ ->
            clearSearchTextBox(actionId)
        }

        binding.searchInputLayout.apply {
            visibility = View.GONE
            alpha = 0f
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
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
                R.id.artistsListFragment,
                R.drawable.artist_24dp,
                getString(R.string.artists_menu_title)
            )
        )
    }

    private fun clearSearchTextBox(actionId: Int): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {
            // Hide keyboard
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.searchInput.windowToken, 0)

            // Clear focus
            binding.searchInput.clearFocus()

            // Perform the search here
            // val query = binding.searchInput.text?.toString().orEmpty()
            // TODO: trigger your filter logic with 'query'

            return true
        } else {
            return false
        }
    }

    private fun toggleSearchBar() {
        val searchLayout = binding.searchInputLayout

        if (!isSearchVisible) {
            searchLayout.visibility = View.VISIBLE
            searchLayout.animate()
                .alpha(1f)
                .setDuration(200)
                .withStartAction { searchLayout.visibility = View.VISIBLE }
                .start()

            binding.searchInput.requestFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.searchInput, InputMethodManager.SHOW_IMPLICIT)

        } else {
            searchLayout.animate()
                .alpha(0f)
                .setDuration(200)
                .withEndAction {
                    searchLayout.visibility = View.GONE
                    binding.searchInput.setText("")
                    binding.searchInput.clearFocus()

                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.searchInput.windowToken, 0)
                }.start()
        }

        isSearchVisible = !isSearchVisible
    }
}