package pt.josemssilva.bucketlist.model.repositories

import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by josesilva on 04/04/18.
 */
interface Repository<T> {
    fun fetchData(): Observable<List<T>>
    fun fetchData(id: String): Single<T>
    fun add(item: T): Single<T>
    fun updateItem(item: T): Single<T>
    fun deleteItem(item: T): Single<Void>
}