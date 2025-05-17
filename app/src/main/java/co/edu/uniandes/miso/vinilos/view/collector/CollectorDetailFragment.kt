package co.edu.uniandes.miso.vinilos.view.collector

import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import co.edu.uniandes.miso.vinilos.databinding.FragmentCollectorDetailBinding
import co.edu.uniandes.miso.vinilos.viewmodel.collector.CollectorDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectorDetailFragment : Fragment() {

    private val collectorDetailViewModel: CollectorDetailViewModel by viewModels()
    private var _binding: FragmentCollectorDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectorDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        val collectorId = arguments?.getInt("collectorId") ?: -1
        Log.d("CollectorDetailFragment", "Collector ID: $collectorId")
        loadData(collectorId)

        progressBar = binding.collectorDetailProgressBar
        progressBar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupObservers() {
        collectorDetailViewModel.collector.observe(viewLifecycleOwner) { collector ->
            binding.collector = collector
            (activity as? AppCompatActivity)?.supportActionBar?.title = collector.name
            progressBar.visibility = View.GONE
        }
        collectorDetailViewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadData(collectorId: Int) {
        collectorDetailViewModel.loadCollectorDetail(collectorId)
    }
}

@BindingAdapter("underlinedText")
fun setUnderlinedText(view: TextView, text: String?) {
    if (text != null) {
        val spannable = SpannableString(text)
        spannable.setSpan(UnderlineSpan(), 0, text.length, 0)
        view.text = spannable
    }
}
