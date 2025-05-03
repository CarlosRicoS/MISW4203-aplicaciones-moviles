package co.edu.uniandes.miso.vinilos.view.performer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import co.edu.uniandes.miso.vinilos.databinding.PerformerDetailBinding
import co.edu.uniandes.miso.vinilos.viewmodel.performer.PerformerDetailViewModel
import com.bumptech.glide.Glide
import kotlin.getValue

class PerformerDetailFragment : Fragment() {

    private var _binding: PerformerDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PerformerDetailViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PerformerDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.performer.observe(viewLifecycleOwner) { performer ->
            binding.performer = performer
            Glide.with(binding.root).load(performer.image).into(binding.performerImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
