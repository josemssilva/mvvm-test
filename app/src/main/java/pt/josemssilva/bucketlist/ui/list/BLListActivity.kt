package pt.josemssilva.bucketlist.ui.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.groceries_layout.*
import pt.josemssilva.bucketlist.App
import pt.josemssilva.bucketlist.R
import pt.josemssilva.bucketlist.model.models.GroceryItem
import pt.josemssilva.bucketlist.ui.BaseActivity
import pt.josemssilva.bucketlist.ui.adapters.BLListAdapter
import pt.josemssilva.bucketlist.ui.detail.BLDetailActivity
import pt.josemssilva.bucketlist.ui.editable.BLEditableActivity
import pt.josemssilva.bucketlist.viewmodels.BLListViewModel
import pt.josemssilva.bucketlist.viewmodels.factories.BLListViewModelFactory
import pt.josemssilva.bucketlist.viewmodels.actions.BLListActions
import pt.josemssilva.bucketlist.viewmodels.states.BLListState

/**
 * Created by josesilva on 04/04/18.
 */
class BLListActivity : BaseActivity(), BLListAdapter.ItemListener {

    private val viewModel: BLListViewModel by lazy {
        val viewModelFactory = BLListViewModelFactory(App.injectRepository())
        ViewModelProviders.of(this@BLListActivity, viewModelFactory).get(BLListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindObservers()
    }

    private fun bindObservers() {
        viewModel.getActionsObservable().observe(
                this@BLListActivity,
                Observer { state ->
                    when (state) {
                        is BLListActions.AddItem -> navigateTo(BLEditableActivity::class.java)
                        is BLListActions.ItemDetail -> {
                            val bundle = Bundle()
                            bundle.putString(BLDetailActivity.BUNDLE_DATA, state.itemId)
                            navigateTo(BLDetailActivity::class.java, bundle)
                        }
                    }
                }
        )

        viewModel.getStateObservable().observe(
                this@BLListActivity,
                Observer { state ->
                    when(state) {
                        is BLListState.DataFetching -> { showLoadingView() }
                        is BLListState.Error -> { showErrorView(state.message ?: "")}
                        is BLListState.Data -> {
                            setupAdapter(state.data)
                        }
                    }
                }
        )

        fab.setOnClickListener { _ -> viewModel.addGrocery() }
    }

    override fun layoutId() = R.layout.groceries_layout

    private fun setupAdapter(items: List<GroceryItem>) {

        // TODO find a way to put this code inside Adapter, not here.
        // the adapter is the one that takes care of data, so it should be the one
        // observing the changes

        if (list.adapter == null) {
            list.adapter = BLListAdapter(this@BLListActivity)
            list.layoutManager = LinearLayoutManager(this@BLListActivity)
        }
        (list.adapter as BLListAdapter).updateItems(items)
        showContentView()
    }

    override fun onClick(item: GroceryItem) {
        viewModel.seeGroceryDetails(item.id)
    }

    override fun onResume() {
        super.onResume()
    }
}