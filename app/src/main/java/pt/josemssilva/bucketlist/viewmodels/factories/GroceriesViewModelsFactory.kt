package pt.josemssilva.bucketlist.viewmodels.factories

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import pt.josemssilva.bucketlist.model.repositories.GroceriesRepository
import pt.josemssilva.bucketlist.viewmodels.GroceriesDetailViewModel
import pt.josemssilva.bucketlist.viewmodels.GroceriesListViewModel

/**
 * Created by josesilva on 10/04/18.
 */
class GroceriesViewModelsFactory(val repo: GroceriesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when (modelClass) {
            GroceriesListViewModel::class.java -> return GroceriesListViewModel(repo) as T
            GroceriesDetailViewModel::class.java -> return GroceriesDetailViewModel(repo) as T
            else -> throw IllegalArgumentException("Unknown ViewModel Class")
        }
//        if (modelClass.isAssignableFrom(GroceriesListViewModel::class.java)) {
//
//        }

    }
}