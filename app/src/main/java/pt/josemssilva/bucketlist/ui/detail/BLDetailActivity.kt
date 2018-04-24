package pt.josemssilva.bucketlist.ui.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.groceries_detail_layout.*
import pt.josemssilva.bucketlist.App
import pt.josemssilva.bucketlist.R
import pt.josemssilva.bucketlist.data.models.GroceryItem
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
        val vmFactory = BLDetailViewModelFactory(readBundle(), (application as App).getRepository())
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

    override fun handleSubscriptions() {
        addSubscription(viewModel.getActionsObservable().subscribeWith(object : DisposableObserver<BLDetailActions>(){
            override fun onComplete() {
                // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onNext(action: BLDetailActions) {
                when (action) {
                    is BLDetailActions.ItemDeleted -> finish()
                    is BLDetailActions.EditItem -> navigateToEditItem(action.itemId)
                }
            }

            override fun onError(e: Throwable) {
                // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }))
    }
}