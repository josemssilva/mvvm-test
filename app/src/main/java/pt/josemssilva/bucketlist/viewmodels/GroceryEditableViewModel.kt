package pt.josemssilva.bucketlist.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.schedulers.Schedulers
import pt.josemssilva.bucketlist.model.models.GroceryItem
import pt.josemssilva.bucketlist.model.repositories.GroceriesRepository

/**
 * Created by josesilva on 10/04/18.
 */
class GroceryEditableViewModel(val repo: GroceriesRepository) : ViewModel() {

    val liveItem = MutableLiveData<GroceryItem>()

    fun fetchData(id: String) {
        repo.fetchData(id)
                .subscribeOn(Schedulers.io())
                .subscribe { item, e ->
                    if (e != null) {

                    } else {
                        liveItem.postValue(item)
                    }
                }
    }

    fun submitData() {

    }

}