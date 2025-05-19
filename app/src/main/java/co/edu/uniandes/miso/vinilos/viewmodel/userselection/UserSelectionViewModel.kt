package co.edu.uniandes.miso.vinilos.viewmodel.userselection

import androidx.lifecycle.ViewModel
import co.edu.uniandes.miso.vinilos.model.repository.VinylsUserSelectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserSelectionViewModel @Inject constructor(
    private val userSelectionRepository: VinylsUserSelectionRepository
) : ViewModel(){

    fun setAppUser() {

        userSelectionRepository.setRegularUserAsAppUser()
    }
}