package co.edu.uniandes.miso.vinilos.view.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import co.edu.uniandes.miso.vinilos.databinding.FragmentListAlbumsBinding

class ListAlbumsFragment : Fragment() {
    private lateinit var binding: FragmentListAlbumsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListAlbumsBinding.inflate(inflater, container, false)
        return binding.root
    }
}