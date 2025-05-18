package co.edu.uniandes.miso.vinilos.view.userselection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.edu.uniandes.miso.vinilos.MainActivity
import co.edu.uniandes.miso.vinilos.R
import co.edu.uniandes.miso.vinilos.databinding.FragmentUserSelectionBinding
import co.edu.uniandes.miso.vinilos.viewmodel.performer.ListPerformersViewModel
import co.edu.uniandes.miso.vinilos.viewmodel.userselection.UserSelectionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserSelectionFragment : Fragment() {

    private var _binding: FragmentUserSelectionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserSelectionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val regularUserButton = view.findViewById<Button>(R.id.user_selection_regular_user_btn)
        val collectorButton = view.findViewById<Button>(R.id.user_selection_collector_btn)

        regularUserButton.setOnClickListener {

            viewModel.setAppUser()
            val toast = Toast.makeText(context, "¡Ahora eres un usuario normal!", Toast.LENGTH_SHORT) // in Activity
            toast.show()
            findNavController().navigate(R.id.albumsListFragment, savedInstanceState)
            val mainActivity:MainActivity = activity as MainActivity
            mainActivity.updateNavigationOptions()
        }

        collectorButton.setOnClickListener{

            findNavController().navigate(R.id.userSelectionCollectorFragment, savedInstanceState)

        }
    }
}