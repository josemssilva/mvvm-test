package pt.josemssilva.bucketlist.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.schedulers.Schedulers
import pt.josemssilva.bucketlist.model.models.GroceryItem
import pt.josemssilva.bucketlist.model.repositories.GroceriesRepository

/**
 * Created by josesilva on 04/04/18.
 */
class GroceriesListViewModel(val groceriesRepository: GroceriesRepository) : ViewModel() {

    val groceries = MutableLiveData<List<GroceryItem>>()

    // val addGroceryListener = PublishSubject.create<String>()
    val addGroceryListener = MutableLiveData<Void>()
    val seeGroceryListener = MutableLiveData<GroceryItem>()

    init {
        groceriesRepository.fetchData()
                .subscribeOn(Schedulers.io())
                .subscribe { t: List<GroceryItem>? ->
                    groceries.postValue(t)
                }
    }

    fun addGrocery() {
        addGroceryListener.postValue(null)
    }

    fun seeGroceryDetails(id: String) {
        groceriesRepository.fetchData(id)
                .subscribeOn(Schedulers.io())
                .subscribe { t -> seeGroceryListener.postValue(t) }
    }
}
