package co.edu.uniandes.miso.vinilos

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import co.edu.uniandes.miso.vinilos.databinding.ActivityMainBinding

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
        binding.navigationView.setupWithNavController(navController)
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
}