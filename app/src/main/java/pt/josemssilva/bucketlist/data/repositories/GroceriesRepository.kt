package pt.josemssilva.bucketlist.data.repositories

import io.reactivex.Observable
import io.reactivex.Single
import pt.josemssilva.bucketlist.data.models.GroceryItem

/**
 * Created by josesilva on 04/04/18.
 */
interface GroceriesRepository {
    fun fetchData(): Observable<List<GroceryItem>>
    fun fetchData(id: String): Observable<GroceryItem>
    fun add(item: GroceryItem): Single<GroceryItem>
    fun updateItem(item: GroceryItem): Single<GroceryItem>
    fun deleteItem(item: GroceryItem): Single<String>
}