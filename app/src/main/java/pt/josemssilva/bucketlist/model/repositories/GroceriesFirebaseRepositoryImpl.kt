package pt.josemssilva.bucketlist.model.repositories

import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import pt.josemssilva.bucketlist.model.models.GroceryItem
import java.lang.ClassCastException

/**
 * Created by josesilva on 04/04/18.
 */
class GroceriesFirebaseRepositoryImpl : GroceriesRepository {

    companion object {
        private const val FB_BUCKETLIST_COLLECTION = "bucketlist"
    }

    override fun fetchData(): Observable<List<GroceryItem>> {

        val collection = FirebaseFirestore.getInstance()
                .collection(FB_BUCKETLIST_COLLECTION)

        val observableOnSubscribe = ObservableOnSubscribe<List<GroceryItem>> { emitter ->
            collection.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    emitter.onError(Exception("error loading data"))
                    return@addSnapshotListener
                }

//                snapshot.documentChanges.map { it ->
//                    when(it.type) {
//                        DocumentChange.Type.ADDED -> emitter.onNext(GroceryItem.mapper(it.document))
//                        DocumentChange.Type.MODIFIED -> emitter.onNext(GroceryItem.mapper(it.document))
//                        DocumentChange.Type.REMOVED -> emitter.onNext(GroceryItem.mapper(it.document))
//                    }
//                }

                val list = ArrayList<GroceryItem>()
                snapshot.documents.map { it ->
                    val item = GroceryItem.mapper(item = it)
                    list.add(item)
                }

                emitter.onNext(list)
            }
        }

        return Observable.create(observableOnSubscribe)
    }

    override fun fetchData(id: String): Observable<GroceryItem> {
        val document = FirebaseFirestore.getInstance()
                .collection(FB_BUCKETLIST_COLLECTION)
                .document(id)

        val observable = ObservableOnSubscribe<GroceryItem> { emitter ->
            document.addSnapshotListener { documentSnapshot, e ->
                if (e != null) {
                    emitter.onError(e)
                } else {
                    try {
                        emitter.onNext(GroceryItem.mapper(documentSnapshot))
                    } catch (e1: ClassCastException) {
                        emitter.onError(e1)
                    }
                }
            }
        }

        return Observable.create(observable)
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