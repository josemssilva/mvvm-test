package pt.josemssilva.bucketlist

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import pt.josemssilva.bucketlist.data.managers.SessionManager
import pt.josemssilva.bucketlist.data.repositories.GroceriesRepositoryImpl
import pt.josemssilva.bucketlist.utils.Logger

/**
 * Created by josesilva on 04/04/18.
 */
class App : Application() {

    private val groceriesRepository by lazy { GroceriesRepositoryImpl() }
    private val logger = Logger(this@App)
    val sessionManager by lazy { SessionManager.Factory.create() }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    fun getRepository() = groceriesRepository
    fun getLogger() = logger
}