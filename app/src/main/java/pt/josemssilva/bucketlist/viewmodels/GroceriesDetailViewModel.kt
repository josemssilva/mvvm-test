package pt.josemssilva.bucketlist.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.schedulers.Schedulers
import pt.josemssilva.bucketlist.model.models.GroceryItem
import pt.josemssilva.bucketlist.model.repositories.GroceriesRepository

/**
 * Created by josesilva on 10/04/18.
 */
class GroceriesDetailViewModel(val repo: GroceriesRepository) : ViewModel() {

    var groceryItem = MutableLiveData<GroceryItem>()
    val alertListener = MutableLiveData<String>()

    fun fetchData(id: String) {
        repo.fetchData(id)
                .subscribeOn(Schedulers.io())
                .subscribe { item, e ->
                    if (e != null) {
                        alertListener.postValue("error loading data")
                    } else {
                        groceryItem.postValue(item)
                    }
                }
    }
}