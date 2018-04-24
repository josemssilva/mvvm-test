package pt.josemssilva.bucketlist.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import pt.josemssilva.bucketlist.data.repositories.GroceriesRepository
import pt.josemssilva.bucketlist.viewmodels.actions.BLListActions
import pt.josemssilva.bucketlist.viewmodels.states.BLListState

/**
 * Created by josesilva on 04/04/18.
 */
class BLListViewModel(private val repo: GroceriesRepository) : ViewModel() {

    private val stateObservable = MutableLiveData<BLListState>()
    private val actionObservable : PublishSubject<BLListActions> = PublishSubject.create()

    init {
        repo.fetchData()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {t ->
                            stateObservable.postValue(BLListState.Data(t ?: listOf()))
                        },
                        {e ->
                            stateObservable.postValue(BLListState.Error(e.message))
                        }
                )
    }

    fun getStateObservable() = stateObservable
    fun getActionsObservable() = actionObservable

    fun addGrocery() {
        actionObservable.onNext(BLListActions.AddItem)
    }

    fun seeGroceryDetails(id: String) {
        actionObservable.onNext(BLListActions.ItemDetail(id))
    }
}
