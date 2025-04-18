package co.edu.uniandes.miso.vinilos.view.collector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import co.edu.uniandes.miso.vinilos.databinding.FragmentListCollectorsBinding

class ListCollectorsFragment : Fragment() {
    private lateinit var binding: FragmentListCollectorsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListCollectorsBinding.inflate(inflater, container, false)
        return binding.root
    }
}