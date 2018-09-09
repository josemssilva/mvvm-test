package pt.josemssilva.bucketlist.ui.editable

import android.app.Activity
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.TextView
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.groceries_editable_layout.*
import pt.josemssilva.bucketlist.App
import pt.josemssilva.bucketlist.R
import pt.josemssilva.bucketlist.data.models.GroceryItem
import pt.josemssilva.bucketlist.ui.BaseActivity
import pt.josemssilva.bucketlist.ui.detail.DetailActivity
import pt.josemssilva.bucketlist.viewmodels.EditableViewModel
import pt.josemssilva.bucketlist.viewmodels.factories.EditableViewModelFactory
import pt.josemssilva.bucketlist.viewmodels.actions.EditableActions
import pt.josemssilva.bucketlist.viewmodels.states.EditableState
import java.util.concurrent.TimeUnit

/**
 * Created by josesilva on 10/04/18.
 */
class EditableActivity : BaseActivity() {

    companion object {

        const val BUNDLE_DATA = "editable:bundle_data"

        private const val REQUEST_IMAGE_TAKE = 501
        private const val REQUEST_IMAGE_SELECT = 502
    }

    val viewModel: EditableViewModel by lazy {
        val factory = EditableViewModelFactory(readBundle(), (application as App).getRepository())
        ViewModelProviders.of(this@EditableActivity, factory).get(EditableViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindUiObservers()
        readBundle()
    }

    override fun layoutId() = R.layout.groceries_editable_layout

    private fun bindUiObservers() {

        viewModel.getStateObservable().observe(
                this@EditableActivity,
                Observer { state ->
                    when (state) {
                        is EditableState.DataProcessing   -> showLoadingView()
                        is EditableState.Error            -> showErrorView(state.message ?: "")
                        is EditableState.Data             -> {
                            bindData(state.data ?: GroceryItem.EMPTY)
                        }
                    }
                }
        )

        image.fetchImage
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe { value ->
                    if (value) {
                        openImageDialog()
                    }
                }

        button.setOnClickListener { _ ->
            viewModel.submitData(
                    description_field.text.toString() ?: "",
                    quantity_field.text.toString() ?: "",
                    comments_field.text.toString() ?: "",
                    image.url ?: "",
                    GroceryItem.State.NONE
            )
        }
    }

    private fun readBundle(): String? {
        var bundledData: String? = null
        val titleResourceId : Int
        if (intent.hasExtra(BUNDLE_DATA)) {
            bundledData = intent.getStringExtra(BUNDLE_DATA)
            showLoadingView()
            titleResourceId = R.string.editable_title
        } else {
            showContentView()
            titleResourceId = R.string.create_title
        }

        setupActionBar(getString(titleResourceId), true)
        return bundledData
    }

    private fun bindData(item: GroceryItem) {
        description_field.setText(item.description
                ?: "", TextView.BufferType.EDITABLE)
        quantity_field.setText(item.quantity
                ?: "", TextView.BufferType.EDITABLE)
        comments_field.setText(item.comments
                ?: "", TextView.BufferType.EDITABLE)
        image.url = item.image ?: ""
        showContentView()
    }

    private fun openImageDialog() {
        AlertDialog.Builder(this@EditableActivity)
                .setItems(
                        resources.getStringArray(R.array.camera_dialog_options),
                        DialogInterface.OnClickListener { dialog, i ->
                            when (i) {
                                0 -> openCameraApp()
                                1 -> openImageDialog()
                                else -> throw IllegalArgumentException("no value defined")
                            }
                            dialog.dismiss()
                        }
                )
    }

    private fun openCameraApp() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_TAKE)
        }
    }

    private fun openFileExplorer() {
        val intent = Intent()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Activity.RESULT_OK == resultCode && data != null) {
            when (requestCode) {
                REQUEST_IMAGE_TAKE -> {

                }
                REQUEST_IMAGE_SELECT -> {

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun navigateToCreatedItem(id: String) {
        val bundle = Bundle()
        bundle.putString(DetailActivity.BUNDLE_DATA, id)
        navigateTo(DetailActivity::class.java, bundle)
        finish()
    }

    override fun handleSubscriptions() {
        addSubscription(viewModel.getActionsObservable().subscribeWith(object: DisposableObserver<EditableActions>() {
            override fun onComplete() {
                // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onNext(action: EditableActions) {
                when (action) {
                    is EditableActions.DataUpdated    -> finish()
                    is EditableActions.DataAdded      -> navigateToCreatedItem(action.id)
                }
            }

            override fun onError(e: Throwable) {
                // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }))
    }
}