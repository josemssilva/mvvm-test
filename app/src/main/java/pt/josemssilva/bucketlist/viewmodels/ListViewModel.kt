package pt.josemssilva.bucketlist.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import pt.josemssilva.bucketlist.data.repositories.GroceriesRepository
import pt.josemssilva.bucketlist.viewmodels.actions.ListActions
import pt.josemssilva.bucketlist.viewmodels.states.ListState

/**
 * Created by josesilva on 04/04/18.
 */
class ListViewModel(private val repo: GroceriesRepository) : ViewModel() {

    private val stateObservable = MutableLiveData<ListState>()
    private val actionObservable : PublishSubject<ListActions> = PublishSubject.create()

    init {
        repo.fetchData()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {t ->
                            stateObservable.postValue(ListState.Data(t ?: listOf()))
                        },
                        {e ->
                            stateObservable.postValue(ListState.Error(e.message))
                        }
                )
    }

    fun getStateObservable() = stateObservable
    fun getActionsObservable() = actionObservable

    fun addGrocery() {
        actionObservable.onNext(ListActions.AddItem)
    }

    fun seeGroceryDetails(id: String) {
        actionObservable.onNext(ListActions.ItemDetail(id))
    }
}
