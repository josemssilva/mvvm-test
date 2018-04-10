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
import pt.josemssilva.bucketlist.ui.adapters.GroceriesAdapter
import pt.josemssilva.bucketlist.ui.detail.GroceryDetailActivity
import pt.josemssilva.bucketlist.ui.editable.GroceryEditableActivity
import pt.josemssilva.bucketlist.viewmodels.GroceriesListViewModel
import pt.josemssilva.bucketlist.viewmodels.factories.GroceriesViewModelsFactory

/**
 * Created by josesilva on 04/04/18.
 */
class GroceriesListActivity : BaseActivity(), GroceriesAdapter.ItemListener {

    private val viewModel: GroceriesListViewModel by lazy {
        val viewModelFactory = GroceriesViewModelsFactory(App.injectRepository())
        ViewModelProviders.of(this@GroceriesListActivity, viewModelFactory).get(GroceriesListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindObservers()
    }

    private fun bindObservers() {
        viewModel.groceries.observe(
                this@GroceriesListActivity,
                Observer<List<GroceryItem>> { items ->
                    setupAdapter(items.orEmpty())
                }
        )

        viewModel.addGroceryListener.observe(
                this@GroceriesListActivity,
                Observer { _ ->
                    navigateTo(GroceryEditableActivity::class.java)
                }
        )

        viewModel.seeGroceryListener.observe(
                this@GroceriesListActivity,
                Observer { _ ->
                    navigateTo(GroceryDetailActivity::class.java)
                }
        )
    }

    override fun layoutId() = R.layout.groceries_layout

    private fun setupAdapter(items: List<GroceryItem>) {

        // TODO find a way to put this code inside Adapter, not here.
        // the adapter is the one that takes care of data, so it should be the one
        // observing the changes

        if (list.adapter == null) {
            list.adapter = GroceriesAdapter(this@GroceriesListActivity)
            list.layoutManager = LinearLayoutManager(this@GroceriesListActivity)
        }
        (list.adapter as GroceriesAdapter).updateItems(items)
        showContentView()
    }

    override fun onClick(item: GroceryItem) {
        viewModel.seeGroceryDetails(item.id)
    }
}