package pt.josemssilva.bucketlist.ui.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import kotlinx.android.synthetic.main.groceries_detail_layout.*
import pt.josemssilva.bucketlist.App
import pt.josemssilva.bucketlist.R
import pt.josemssilva.bucketlist.model.models.GroceryItem
import pt.josemssilva.bucketlist.ui.BaseActivity
import pt.josemssilva.bucketlist.ui.editable.BLEditableActivity
import pt.josemssilva.bucketlist.viewmodels.BLDetailViewModel
import pt.josemssilva.bucketlist.viewmodels.actions.BLDetailActions
import pt.josemssilva.bucketlist.viewmodels.factories.BLDetailViewModelFactory
import pt.josemssilva.bucketlist.viewmodels.states.BLDetailState

/**
 * Created by josesilva on 05/04/18.
 */
class BLDetailActivity : BaseActivity() {

    companion object {
        const val BUNDLE_DATA = "detail:bundle_data"
    }

    val viewModel: BLDetailViewModel by lazy {
        val vmFactory = BLDetailViewModelFactory(readBundle(), App.injectRepository())
        ViewModelProviders.of(this@BLDetailActivity, vmFactory).get(BLDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindObservers()
        readBundle()
    }

    private fun bindObservers() {

        viewModel.getStateObservable().observe(
                this@BLDetailActivity,
                Observer { state ->
                    when (state) {
                        is BLDetailState.DataFetch -> showLoadingView()
                        is BLDetailState.Data -> bindGroceryInfo(state.data)
                        is BLDetailState.Error -> showErrorView(state.message ?: "")
                    }
                }
        )

        viewModel.getActionsObservable().observe(
                this@BLDetailActivity,
                Observer { action ->
                    when (action) {
                        is BLDetailActions.ItemDeleted -> finish()
                        is BLDetailActions.EditItem -> navigateToEditItem(action.itemId)
                    }
                }
        )

        detailsEditBtn.setOnClickListener { _ -> viewModel.editItem() }
        detailsDeleteBtn.setOnClickListener { _ -> viewModel.deleteItem() }
    }

    override fun layoutId() = R.layout.groceries_detail_layout

    private fun readBundle(): String {
        var bundleData: String = ""
        if (intent.hasExtra(BUNDLE_DATA)) {
            bundleData = intent.getStringExtra(BUNDLE_DATA)
        }
        return bundleData
    }

    private fun bindGroceryInfo(item: GroceryItem) {
        description.text = item.description
        quantity.text = item.quantity
        showContentView()
    }

    private fun navigateToEditItem(itemId: String) {
        val bundle = Bundle()
        bundle.putString(BLEditableActivity.BUNDLE_DATA, itemId)
        navigateTo(BLEditableActivity::class.java, bundle)
    }

}