package co.edu.uniandes.miso.vinilos.view.performer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.edu.uniandes.miso.vinilos.R
import co.edu.uniandes.miso.vinilos.databinding.FragmentListPerformersBinding
import co.edu.uniandes.miso.vinilos.view.adapters.ListPerformersAdapter
import co.edu.uniandes.miso.vinilos.viewmodel.performer.ListPerformersViewModel

class ListPerformersFragment : Fragment() {
    private var _binding: FragmentListPerformersBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private val viewModel: ListPerformersViewModel by viewModels()
    private var viewModelAdapter: ListPerformersAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListPerformersBinding.inflate(inflater, container, false)
        viewModelAdapter = ListPerformersAdapter(){ performerId, performerName, performerImage, performerType ->

            val bundle = Bundle().apply {
                putInt("performerId", performerId)
                putString("performerName", performerName)
                putString("performerImage", performerImage)
                putString("performerType", performerType.toString())
            }
            findNavController().navigate(R.id.performerDetailContainerFragment, bundle)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.performerRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter

        viewModel.performers.observe(viewLifecycleOwner) { performers ->
            viewModelAdapter?.setPerformers(performers)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.performerListProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.loadPerformers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}