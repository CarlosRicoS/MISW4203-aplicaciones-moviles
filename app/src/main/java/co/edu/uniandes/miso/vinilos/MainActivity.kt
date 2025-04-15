package co.edu.uniandes.miso.vinilos

import android.os.Bundle
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
            navController.navigate(destinationId)
            binding.drawerLayout.closeDrawers()
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.toolbar.title = destination.label
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
                R.drawable.ic_launcher_background,
                getString(R.string.albums_menu_title)
            ),
            DrawerItem.Divider,
            DrawerItem.MenuItem(
                R.id.collectorsListFragment,
                R.drawable.ic_launcher_background,
                getString(R.string.collectors_menu_title)
            ),
            DrawerItem.Divider,
            DrawerItem.MenuItem(
                R.id.artistsListFragment,
                R.drawable.ic_launcher_background,
                getString(R.string.artists_menu_title)
            )
        )
    }
}