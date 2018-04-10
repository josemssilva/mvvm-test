package pt.josemssilva.bucketlist.ui.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import kotlinx.android.synthetic.main.groceries_detail_layout.*
import pt.josemssilva.bucketlist.App
import pt.josemssilva.bucketlist.R
import pt.josemssilva.bucketlist.model.models.GroceryItem
import pt.josemssilva.bucketlist.ui.BaseActivity
import pt.josemssilva.bucketlist.viewmodels.GroceriesDetailViewModel
import pt.josemssilva.bucketlist.viewmodels.factories.GroceriesViewModelsFactory

/**
 * Created by josesilva on 05/04/18.
 */
class GroceryDetailActivity : BaseActivity() {

    val viewModel: GroceriesDetailViewModel by lazy {
        val vmFactory = GroceriesViewModelsFactory(App.injectRepository())
        ViewModelProviders.of(this@GroceryDetailActivity, vmFactory).get(GroceriesDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindObservers()
        viewModel.fetchData("5OVb5CN2cHBrtF8KWYXr")
    }

    fun bindObservers() {
        viewModel.groceryItem.observe(
                this@GroceryDetailActivity,
                Observer<GroceryItem> { t ->
                    if (t != null) {
                        bindGroceryInfo(t)
                    }
                }
        )

        viewModel.alertListener.observe(
                this@GroceryDetailActivity,
                Observer<String> { message ->
                    showErrorView(message ?: "single error")
                }
        )
    }

    override fun layoutId() = R.layout.groceries_detail_layout

    fun bindGroceryInfo(item: GroceryItem) {
        description.text = item.description
        quantity.text = item.quantity
        showContentView()
    }
}