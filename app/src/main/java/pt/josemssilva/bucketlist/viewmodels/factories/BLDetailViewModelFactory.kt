package pt.josemssilva.bucketlist.viewmodels.factories

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import pt.josemssilva.bucketlist.model.repositories.GroceriesRepository
import pt.josemssilva.bucketlist.viewmodels.BLDetailViewModel
import pt.josemssilva.bucketlist.viewmodels.BLListViewModel
import pt.josemssilva.bucketlist.viewmodels.BLEditableViewModel
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


/**
 * Created by josesilva on 10/04/18.
 */
class BLDetailViewModelFactory(val itemId: String, val repo: GroceriesRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BLDetailViewModel::class.java)) {
            return BLDetailViewModel(itemId, repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}