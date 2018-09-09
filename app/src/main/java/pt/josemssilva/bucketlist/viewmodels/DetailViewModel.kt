package pt.josemssilva.bucketlist.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import pt.josemssilva.bucketlist.data.repositories.GroceriesRepository
import pt.josemssilva.bucketlist.viewmodels.actions.DetailActions
import pt.josemssilva.bucketlist.viewmodels.states.DetailState

/**
 * Created by josesilva on 10/04/18.
 */
class DetailViewModel(private val itemId: String, private val repo: GroceriesRepository) : ViewModel() {

    private val stateObservable = MutableLiveData<DetailState>()
    private val actionsObservable : PublishSubject<DetailActions> = PublishSubject.create()

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
                            stateObservable.postValue(DetailState.Data(item))
                        },
                        {e ->
                            stateObservable.postValue(DetailState.Error(e.message))
                        }
                )
    }

    fun editItem() {
        actionsObservable.onNext(DetailActions.EditItem(itemId))
    }

    fun deleteItem() {
        if (stateObservable.value is DetailState.Data) {
            val item = (stateObservable.value as DetailState.Data).data
            repo.deleteItem(item)
                    .subscribeOn(Schedulers.io())
                    .subscribe { _, e ->
                        if (e != null) {
                            stateObservable.postValue(DetailState.Error(e.message))
                        } else {
                            actionsObservable.onNext(DetailActions.ItemDeleted)
                        }
                    }
        } else {
            stateObservable.postValue(DetailState.Error(""))
        }
    }
}