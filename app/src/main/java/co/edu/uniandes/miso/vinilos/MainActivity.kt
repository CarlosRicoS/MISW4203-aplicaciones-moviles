package co.edu.uniandes.miso.vinilos

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavGraph
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val allTopLevelIds = getAllDestinationIds(navController.graph)
        appBarConfig = AppBarConfiguration(allTopLevelIds, binding.drawerLayout)
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
        }

        binding.searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {
                // Hide keyboard
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.searchInput.windowToken, 0)

                // Clear focus
                binding.searchInput.clearFocus()

                // Perform the search here
                // val query = binding.searchInput.text?.toString().orEmpty()
                // TODO: trigger your filter logic with 'query'

                true
            } else {
                false
            }
        }
    }

    private fun getAllDestinationIds(graph: NavGraph): Set<Int> {
        val ids = mutableSetOf<Int>()
        for (node in graph) {
            if (node is NavGraph) {
                ids.addAll(getAllDestinationIds(node))
            } else {
                ids.add(node.id)
            }
        }
        return ids
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


}