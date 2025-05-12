package co.edu.uniandes.miso.vinilos.view.performer

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import co.edu.uniandes.miso.vinilos.R
import co.edu.uniandes.miso.vinilos.model.domain.PerformerType
import co.edu.uniandes.miso.vinilos.viewmodel.performer.PerformerDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PerformerDetailContainerFragment : Fragment(R.layout.performer_detail_container) {

    private val performerDetailViewModel: PerformerDetailViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressBar: ProgressBar = view.findViewById(R.id.performer_detail_progress_bar)
        val performerId = arguments?.getInt("performerId") ?: return
        val performerType = arguments?.getString("performerType") ?: return
        val performerDetailFragment = PerformerDetailFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.performer_detail_fragment_container, performerDetailFragment)
            .commit()

        performerDetailViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        performerDetailViewModel.performer.observe(viewLifecycleOwner) { performer ->
            (activity as? AppCompatActivity)?.supportActionBar?.title = performer.name
        }

        performerDetailViewModel.loadPerformerDetail(performerId, PerformerType.valueOf(performerType))
    }
}
