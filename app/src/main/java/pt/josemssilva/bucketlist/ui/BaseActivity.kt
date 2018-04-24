package pt.josemssilva.bucketlist.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.base_layout.*
import pt.josemssilva.bucketlist.App
import pt.josemssilva.bucketlist.R

/**
 * Created by josesilva on 04/04/18.
 */
abstract class BaseActivity : AppCompatActivity() {

    companion object {
        private const val FLIPPER_LOADING_CHILD = 0
        private const val FLIPPER_ERROR_CHILD = 1
        private const val FLIPPER_CONTENT_CHILD = 2
    }

    val mCompositeDisposable = CompositeDisposable()
    lateinit var mView: View

    protected fun getApp(): App = (application as App)

    private fun getClassName(): String = this.localClassName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getApp().getLogger().log(getClassName().plus(" - onCreate"))
        setContentView(R.layout.base_layout)

        setupUi()
    }

    override fun onStart() {
        super.onStart()
        getApp().getLogger().log(getClassName().plus(" - onStart"))
    }

    override fun onResume() {
        super.onResume()
        getApp().getLogger().log(getClassName().plus(" - onResume"))
        handleSubscriptions()
    }

    override fun onPause() {
        getApp().getLogger().log(getClassName().plus(" - onPause"))
        mCompositeDisposable.clear()
        super.onPause()
    }

    override fun onStop() {
        getApp().getLogger().log(getClassName().plus(" - onStop"))
        super.onStop()
    }

    override fun onDestroy() {
        getApp().getLogger().log(getClassName().plus(" - onDestroy"))
        super.onDestroy()
    }

    abstract fun layoutId(): Int

    fun setupUi() {
        base_stub.layoutResource = layoutId()
        mView = base_stub.inflate()
    }

    fun showContentView() {
        if (base_flipper.displayedChild != FLIPPER_CONTENT_CHILD)
            base_flipper.displayedChild = FLIPPER_CONTENT_CHILD
    }

    fun showErrorView(message: String) {
        if (base_flipper.displayedChild != FLIPPER_ERROR_CHILD)
            base_flipper.displayedChild = FLIPPER_ERROR_CHILD
    }

    fun showLoadingView() {
        if (base_flipper.displayedChild != FLIPPER_LOADING_CHILD)
            base_flipper.displayedChild = FLIPPER_LOADING_CHILD
    }

    abstract fun handleSubscriptions()

    fun addSubscription(d: Disposable) {
        mCompositeDisposable.add(d)
    }

    fun navigateTo(classToNavigate: Class<out BaseActivity>) {
        navigateTo(classToNavigate, null)
    }

    fun navigateTo(classToNavigate: Class<out BaseActivity>, bundle: Bundle?) {
        val intent = Intent(this@BaseActivity, classToNavigate)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }
}