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
import pt.josemssilva.bucketlist.ui.editable.EditableActivity
import pt.josemssilva.bucketlist.viewmodels.DetailViewModel
import pt.josemssilva.bucketlist.viewmodels.actions.DetailActions
import pt.josemssilva.bucketlist.viewmodels.factories.DetailViewModelFactory
import pt.josemssilva.bucketlist.viewmodels.states.DetailState

/**
 * Created by josesilva on 05/04/18.
 */
class DetailActivity : BaseActivity() {

    companion object {
        const val BUNDLE_DATA = "detail:bundle_data"
    }

    val viewModel: DetailViewModel by lazy {
        val vmFactory = DetailViewModelFactory(readBundle(), (application as App).getRepository())
        ViewModelProviders.of(this@DetailActivity, vmFactory).get(DetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindObservers()
        readBundle()
        setupActionBar(getString(R.string.detail_title), true)
    }

    private fun bindObservers() {

        viewModel.getStateObservable().observe(
                this@DetailActivity,
                Observer { state ->
                    when (state) {
                        is DetailState.DataFetch -> showLoadingView()
                        is DetailState.Data -> bindGroceryInfo(state.data)
                        is DetailState.Error -> showErrorView(state.message ?: "")
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
        bundle.putString(EditableActivity.BUNDLE_DATA, itemId)
        navigateTo(EditableActivity::class.java, bundle)
    }

    override fun handleSubscriptions() {
        addSubscription(viewModel.getActionsObservable().subscribeWith(object : DisposableObserver<DetailActions>(){
            override fun onComplete() {
                // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onNext(action: DetailActions) {
                when (action) {
                    is DetailActions.ItemDeleted -> finish()
                    is DetailActions.EditItem -> navigateToEditItem(action.itemId)
                }
            }

            override fun onError(e: Throwable) {
                // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }))
    }
}