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
import pt.josemssilva.bucketlist.ui.BaseActivity
import pt.josemssilva.bucketlist.viewmodels.GroceryEditableViewModel
import pt.josemssilva.bucketlist.viewmodels.factories.GroceriesViewModelsFactory
import java.util.concurrent.TimeUnit

/**
 * Created by josesilva on 10/04/18.
 */
class GroceryEditableActivity : BaseActivity() {

    companion object {
        const val REQUEST_IMAGE_TAKE = 501
        const val REQUEST_IMAGE_SELECT = 502
    }

    val viewModel: GroceryEditableViewModel by lazy {
        val factory = GroceriesViewModelsFactory(App.injectRepository())
        ViewModelProviders.of(this@GroceryEditableActivity, factory).get(GroceryEditableViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindUiObservers()
    }

    override fun layoutId() = R.layout.groceries_editable_layout

    fun bindUiObservers() {
        viewModel.liveItem.observe(
                this@GroceryEditableActivity,
                Observer { item ->
                    description_field.setText(item?.description ?: "", TextView.BufferType.EDITABLE)
                    quantity_field.setText(item?.quantity ?: "", TextView.BufferType.EDITABLE)
                    comments_field.setText(item?.comments ?: "", TextView.BufferType.EDITABLE)
                    image.url = item?.image ?: ""
                }
        )

        image.fetchImage
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe { value ->
                    if (value) {
                        openImageDialog()
                    }
                }
    }

    private fun openImageDialog() {
        AlertDialog.Builder(this@GroceryEditableActivity)
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
}