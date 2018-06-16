package pt.josemssilva.bucketlist.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import pt.josemssilva.bucketlist.data.models.GroceryItem
import java.lang.ClassCastException

/**
 * Created by josesilva on 04/04/18.
 */
class GroceriesRepositoryImpl : GroceriesRepository {

    companion object {
        private const val FB_BUCKETLIST_COLLECTION = "bucketlist"
    }

    override fun fetchData(): Observable<List<GroceryItem>> {

        return Observable.create { emitter ->
            val collection = FirebaseFirestore.getInstance()
                    .collection(FB_BUCKETLIST_COLLECTION)
            collection.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    emitter.onError(Exception("error loading data"))
                    return@addSnapshotListener
                }

                val list = ArrayList<GroceryItem>()
                snapshot?.documents?.map { it ->
                    val item = GroceryItem.mapper(item = it)
                    list.add(item)
                }

                emitter.onNext(list)
            }
        }
    }

    override fun fetchData(id: String): Observable<GroceryItem> {

        return Observable.create { emitter ->
            val document = FirebaseFirestore.getInstance()
                    .collection(FB_BUCKETLIST_COLLECTION)
                    .document(id)

            document.addSnapshotListener { documentSnapshot, e ->
                if (e != null) {
                    emitter.onError(e)
                } else {
                    try {
                        emitter.onNext(GroceryItem.mapper(documentSnapshot!!))
                    } catch (e1: ClassCastException) {
                        emitter.onError(e1)
                    }
                }
            }
        }
    }

    override fun add(item: GroceryItem): Single<GroceryItem> {
        val collection = FirebaseFirestore.getInstance()
                .collection(FB_BUCKETLIST_COLLECTION)

        val single = SingleOnSubscribe<GroceryItem> { emitter ->
            collection.add(item.toMap())
                    .addOnSuccessListener { documentReference ->
                        item.id = documentReference.id
                        emitter.onSuccess(item)
                    }
                    .addOnFailureListener { exception ->
                        emitter.onError(exception)
                    }
        }

        return Single.create(single)
    }

    override fun updateItem(item: GroceryItem): Single<GroceryItem> {
        val document = FirebaseFirestore.getInstance()
                .collection(FB_BUCKETLIST_COLLECTION)
                .document(item.id)

        val single = SingleOnSubscribe<GroceryItem> { emitter ->
            document.update(item.toMap())
                    .addOnSuccessListener { _ -> emitter.onSuccess(item) }
                    .addOnFailureListener { e -> emitter.onError(e) }
        }

        return Single.create(single)
    }

    override fun deleteItem(item: GroceryItem): Single<String> {
        val document = FirebaseFirestore.getInstance()
                .collection(FB_BUCKETLIST_COLLECTION)
                .document(item.id)

        val single = SingleOnSubscribe<String> { emitter ->
            document.delete()
                    .addOnSuccessListener { _ ->
                        emitter.onSuccess("")
                    }
                    .addOnFailureListener { e -> emitter.onError(e) }
        }

        return Single.create(single)
    }
}