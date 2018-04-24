package pt.josemssilva.bucketlist

import android.app.Application
import pt.josemssilva.bucketlist.data.repositories.GroceriesRepositoryImpl
import pt.josemssilva.bucketlist.utils.Logger

/**
 * Created by josesilva on 04/04/18.
 */
class App : Application() {

    private val groceriesRepository by lazy { GroceriesRepositoryImpl() }
    private val logger = Logger(this@App)

    fun getRepository() = groceriesRepository
    fun getLogger() = logger
}