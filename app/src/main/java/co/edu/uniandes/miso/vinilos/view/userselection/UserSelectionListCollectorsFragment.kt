package co.edu.uniandes.miso.vinilos.view.userselection

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.edu.uniandes.miso.vinilos.MainActivity
import co.edu.uniandes.miso.vinilos.R
import co.edu.uniandes.miso.vinilos.databinding.FragmentListCollectorsBinding
import co.edu.uniandes.miso.vinilos.view.adapters.ListCollectorsAdapter
import co.edu.uniandes.miso.vinilos.viewmodel.userselection.UserSelectionListCollectorsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserSelectionListCollectorsFragment : Fragment() {
    private var _binding: FragmentListCollectorsBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private val viewModel: UserSelectionListCollectorsViewModel by viewModels()
    private var viewModelAdapter: ListCollectorsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCollectorsBinding.inflate(inflater, container, false)
        viewModelAdapter = ListCollectorsAdapter(){ collector ->

            viewModel.setCollectorAsAppUser(collector)
            val toast = Toast.makeText(context, "¡Ahora eres un usuario coleccionista!", Toast.LENGTH_SHORT) // in Activity
            toast.show()
            val mainActivity: MainActivity = activity as MainActivity
            mainActivity.updateNavigationOptions()
            findNavController().navigate(R.id.albumsListFragment, savedInstanceState)
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.collectorRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter

        viewModel.collectors.observe(viewLifecycleOwner) { collectors ->
            viewModelAdapter?.setCollectors(collectors)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.collectorListProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.loadCollectors()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}