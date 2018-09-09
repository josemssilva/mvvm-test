package pt.josemssilva.bucketlist.viewmodels.factories

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import pt.josemssilva.bucketlist.data.repositories.GroceriesRepository
import pt.josemssilva.bucketlist.viewmodels.EditableViewModel


/**
 * Created by josesilva on 10/04/18.
 */
class EditableViewModelFactory(val itemId: String?, val repo: GroceriesRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditableViewModel::class.java)) {
            return EditableViewModel(itemId, repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}