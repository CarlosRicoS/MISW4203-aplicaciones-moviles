package co.edu.uniandes.miso.vinilos.view.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import co.edu.uniandes.miso.vinilos.databinding.FragmentListArtistsBinding

class ListArtistsFragment : Fragment() {
    private lateinit var binding: FragmentListArtistsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListArtistsBinding.inflate(inflater, container, false)
        return binding.root
    }
}