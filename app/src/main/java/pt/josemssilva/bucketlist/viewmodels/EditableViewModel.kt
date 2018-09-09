package pt.josemssilva.bucketlist.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import pt.josemssilva.bucketlist.data.models.GroceryItem
import pt.josemssilva.bucketlist.data.repositories.GroceriesRepository
import pt.josemssilva.bucketlist.viewmodels.actions.EditableActions
import pt.josemssilva.bucketlist.viewmodels.states.EditableState

/**
 * Created by josesilva on 10/04/18.
 */
class EditableViewModel(private val itemId: String?, private val repo: GroceriesRepository) : ViewModel() {

    private val actionsObservable : PublishSubject<EditableActions> = PublishSubject.create()
    private val stateObservable = MutableLiveData<EditableState>()

    fun getActionsObservable() = actionsObservable
    fun getStateObservable() = stateObservable

    init {
        fetchData()
    }

    private fun fetchData() {
        if (itemId != null) {
            repo.fetchData(itemId)
                    .subscribeOn(Schedulers.io())
                    .subscribe({ item -> stateObservable.postValue(EditableState.Data(item)) },
                            { e -> stateObservable.postValue(EditableState.Error(e.message)) })
        }
    }

    fun submitData(description: String, quantity: String, comments: String, image: String, state: GroceryItem.State) {
        val item = GroceryItem("", description, quantity, image, comments, state)
        if (itemId != null) {
            item.id = itemId
            repo.updateItem(item)
                    .subscribeOn(Schedulers.io())
                    .subscribe { item, e ->
                        if (e != null) {
                            stateObservable.postValue(EditableState.Error(e.message))
                        } else {
                            actionsObservable.onNext(EditableActions.DataUpdated)
                        }
                    }
        } else {
            repo.add(item)
                    .subscribeOn(Schedulers.io())
                    .subscribe { item, e ->
                        if (e != null) {
                            stateObservable.postValue(EditableState.Error(e.message))
                        } else {
                            actionsObservable.onNext(EditableActions.DataAdded(item.id))
                        }
                    }
        }
    }

}