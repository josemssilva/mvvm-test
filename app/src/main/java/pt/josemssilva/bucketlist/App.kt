package pt.josemssilva.bucketlist

import android.app.Application
import android.content.Context
import pt.josemssilva.bucketlist.viewmodels.GroceriesListViewModel
import pt.josemssilva.bucketlist.model.repositories.GroceriesFirebaseRepositoryImpl
import pt.josemssilva.bucketlist.model.repositories.GroceriesRepository

/**
 * Created by josesilva on 04/04/18.
 */
class App : Application() {

    companion object {
        private val groceriesRepository by lazy { GroceriesFirebaseRepositoryImpl() }
        private val groceriesViewModel by lazy { GroceriesListViewModel(injectRepository()) }

        fun injectRepository() : GroceriesRepository {
            return groceriesRepository
        }

        fun injectViewModel() : GroceriesListViewModel {
            return groceriesViewModel
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
}