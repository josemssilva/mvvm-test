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
import kotlinx.android.synthetic.main.groceries_editable_layout.*
import pt.josemssilva.bucketlist.App
import pt.josemssilva.bucketlist.R
import pt.josemssilva.bucketlist.model.models.GroceryItem
import pt.josemssilva.bucketlist.ui.BaseActivity
import pt.josemssilva.bucketlist.ui.detail.BLDetailActivity
import pt.josemssilva.bucketlist.viewmodels.BLEditableViewModel
import pt.josemssilva.bucketlist.viewmodels.factories.BLEditableViewModelFactory
import pt.josemssilva.bucketlist.viewmodels.actions.BLEditableActions
import pt.josemssilva.bucketlist.viewmodels.states.BLEditableState
import java.util.concurrent.TimeUnit

/**
 * Created by josesilva on 10/04/18.
 */
class BLEditableActivity : BaseActivity() {

    companion object {

        const val BUNDLE_DATA = "editable:bundle_data"

        private const val REQUEST_IMAGE_TAKE = 501
        private const val REQUEST_IMAGE_SELECT = 502
    }

    val viewModel: BLEditableViewModel by lazy {
        val factory = BLEditableViewModelFactory(readBundle(), App.injectRepository())
        ViewModelProviders.of(this@BLEditableActivity, factory).get(BLEditableViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindUiObservers()
        readBundle()
    }

    override fun layoutId() = R.layout.groceries_editable_layout

    private fun bindUiObservers() {

        viewModel.getStateObservable().observe(
                this@BLEditableActivity,
                Observer { state ->
                    when (state) {
                        is BLEditableState.DataProcessing   -> showLoadingView()
                        is BLEditableState.Error            -> showErrorView(state.message ?: "")
                        is BLEditableState.Data             -> {
                            bindData(state.data ?: GroceryItem())
                        }
                    }
                }
        )

        viewModel.getActionsObservable().observe(
                this@BLEditableActivity,
                Observer { action ->
                    when (action) {
                        is BLEditableActions.DataUpdated    -> finish()
                        is BLEditableActions.DataAdded      -> navigateToCreatedItem(action.id)
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
                    image.url ?: ""
            )
        }
    }

    private fun readBundle(): String? {
        var bundledData: String? = null
        if (intent.hasExtra(BUNDLE_DATA)) {
            bundledData = intent.getStringExtra(BUNDLE_DATA)
            showLoadingView()
        } else {
            showContentView()
        }
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
        AlertDialog.Builder(this@BLEditableActivity)
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
        bundle.putString(BLDetailActivity.BUNDLE_DATA, id)
        navigateTo(BLDetailActivity::class.java, bundle)
        finish()
    }
}