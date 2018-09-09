package pt.josemssilva.bucketlist.ui.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.groceries_layout.*
import pt.josemssilva.bucketlist.R
import pt.josemssilva.bucketlist.data.models.GroceryItem
import pt.josemssilva.bucketlist.ui.BaseActivity
import pt.josemssilva.bucketlist.ui.adapters.BLListAdapter
import pt.josemssilva.bucketlist.ui.adapters.BLListItemDecorator
import pt.josemssilva.bucketlist.ui.detail.DetailActivity
import pt.josemssilva.bucketlist.ui.editable.EditableActivity
import pt.josemssilva.bucketlist.viewmodels.ListViewModel
import pt.josemssilva.bucketlist.viewmodels.factories.ListViewModelFactory
import pt.josemssilva.bucketlist.viewmodels.actions.ListActions
import pt.josemssilva.bucketlist.viewmodels.states.ListState

/**
 * Created by josesilva on 04/04/18.
 */
class ListActivity : BaseActivity(), BLListAdapter.ItemListener {

    private val viewModel: ListViewModel by lazy {
        val viewModelFactory = ListViewModelFactory(getApp().getRepository())
        ViewModelProviders.of(this@ListActivity, viewModelFactory).get(ListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindObservers()
    }

    private fun bindObservers() {
        viewModel.getStateObservable().observe(
                this@ListActivity,
                Observer { state ->
                    when(state) {
                        is ListState.DataFetching -> { showLoadingView() }
                        is ListState.Error -> { showErrorView(state.message ?: "")}
                        is ListState.Data -> {
                            setupAdapter(state.data)
                        }
                    }
                }
        )

        fab.setOnClickListener { _ -> viewModel.addGrocery() }
    }

    override fun handleSubscriptions() {
        addSubscription(viewModel.getActionsObservable().subscribeWith(object : DisposableObserver<ListActions>() {
            override fun onComplete() {

            }

            override fun onNext(t: ListActions) {
                when (t) {
                        is ListActions.AddItem -> navigateTo(EditableActivity::class.java)
                        is ListActions.ItemDetail -> {
                            val bundle = Bundle()
                            bundle.putString(DetailActivity.BUNDLE_DATA, t.itemId)
                            navigateTo(DetailActivity::class.java, bundle)
                        }
                    }
            }

            override fun onError(e: Throwable) {

            }
        }))
    }

    override fun layoutId() = R.layout.groceries_layout

    private fun setupAdapter(items: List<GroceryItem>) {

        // TODO find a way to put this code inside Adapter, not here.
        // the adapter is the one that takes care of data, so it should be the one
        // observing the changes

        if (list.adapter == null) {
            list.adapter = BLListAdapter(this@ListActivity)
            list.layoutManager = LinearLayoutManager(this@ListActivity)

            if (list.itemDecorationCount == 0)
                list.addItemDecoration(BLListItemDecorator())
        }
        (list.adapter as BLListAdapter).updateItems(items)
        showContentView()
    }

    override fun onClick(item: GroceryItem) {
        viewModel.seeGroceryDetails(item.id)
    }
}