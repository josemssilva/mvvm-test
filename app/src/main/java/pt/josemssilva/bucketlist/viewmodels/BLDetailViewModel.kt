package pt.josemssilva.bucketlist.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import pt.josemssilva.bucketlist.data.repositories.GroceriesRepository
import pt.josemssilva.bucketlist.viewmodels.actions.BLDetailActions
import pt.josemssilva.bucketlist.viewmodels.states.BLDetailState

/**
 * Created by josesilva on 10/04/18.
 */
class BLDetailViewModel(private val itemId: String, private val repo: GroceriesRepository) : ViewModel() {

    private val stateObservable = MutableLiveData<BLDetailState>()
    private val actionsObservable : PublishSubject<BLDetailActions> = PublishSubject.create()

    fun getStateObservable() = stateObservable
    fun getActionsObservable() = actionsObservable

    init {
        fetchData(itemId)
    }

    private fun fetchData(id: String) {
        repo.fetchData(id)
                .subscribeOn(Schedulers.io())
                .subscribe (
                        {item ->
                            stateObservable.postValue(BLDetailState.Data(item))
                        },
                        {e ->
                            stateObservable.postValue(BLDetailState.Error(e.message))
                        }
                )
    }

    fun editItem() {
        actionsObservable.onNext(BLDetailActions.EditItem(itemId))
    }

    fun deleteItem() {
        if (stateObservable.value is BLDetailState.Data) {
            val item = (stateObservable.value as BLDetailState.Data).data
            repo.deleteItem(item)
                    .subscribeOn(Schedulers.io())
                    .subscribe { _, e ->
                        if (e != null) {
                            stateObservable.postValue(BLDetailState.Error(e.message))
                        } else {
                            actionsObservable.onNext(BLDetailActions.ItemDeleted)
                        }
                    }
        } else {
            stateObservable.postValue(BLDetailState.Error(""))
        }
    }
}