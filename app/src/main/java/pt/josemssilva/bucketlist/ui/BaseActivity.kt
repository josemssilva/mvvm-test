package pt.josemssilva.bucketlist.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.base_layout.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_layout)

        setupUi()
    }

    override fun onPause() {
        mCompositeDisposable.clear()
        super.onPause()
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