package pt.josemssilva.bucketlist.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import pt.josemssilva.bucketlist.data.models.GroceryItem
import pt.josemssilva.bucketlist.data.repositories.GroceriesRepository
import pt.josemssilva.bucketlist.viewmodels.actions.BLEditableActions
import pt.josemssilva.bucketlist.viewmodels.states.BLEditableState

/**
 * Created by josesilva on 10/04/18.
 */
class BLEditableViewModel(private val itemId: String?, private val repo: GroceriesRepository) : ViewModel() {

    private val actionsObservable : PublishSubject<BLEditableActions> = PublishSubject.create()
    private val stateObservable = MutableLiveData<BLEditableState>()

    fun getActionsObservable() = actionsObservable
    fun getStateObservable() = stateObservable

    init {
        fetchData()
    }

    private fun fetchData() {
        if (itemId != null) {
            repo.fetchData(itemId)
                    .subscribeOn(Schedulers.io())
                    .subscribe({ item -> stateObservable.postValue(BLEditableState.Data(item)) },
                            { e -> stateObservable.postValue(BLEditableState.Error(e.message)) })
        }
    }

    fun submitData(description: String, quantity: String, comments: String, image: String) {
        val item = GroceryItem("", description, quantity, image, comments)
        if (itemId != null) {
            item.id = itemId
            repo.updateItem(item)
                    .subscribeOn(Schedulers.io())
                    .subscribe { item, e ->
                        if (e != null) {
                            stateObservable.postValue(BLEditableState.Error(e.message))
                        } else {
                            actionsObservable.onNext(BLEditableActions.DataUpdated)
                        }
                    }
        } else {
            repo.add(item)
                    .subscribeOn(Schedulers.io())
                    .subscribe { item, e ->
                        if (e != null) {
                            stateObservable.postValue(BLEditableState.Error(e.message))
                        } else {
                            actionsObservable.onNext(BLEditableActions.DataAdded(item.id))
                        }
                    }
        }
    }

}