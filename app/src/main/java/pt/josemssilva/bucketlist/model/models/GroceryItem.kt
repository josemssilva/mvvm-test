package pt.josemssilva.bucketlist.model.models

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.gson.Gson

/**
 * Created by josesilva on 04/04/18.
 */
data class GroceryItem(
        var id: String,
        var description: String,
        var quantity: String,
        var image: String?,
        var comments: String?
) {
    constructor() : this("", "", "", "", "")

    companion object {

        const val FIELD_ID = "id"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_QUANTITY = "quantity"
        const val FIELD_IMAGE = "image"
        const val FIELD_COMMENTS = "comments"

        fun mapper(item: Any): GroceryItem {
            when (item) {
                is QueryDocumentSnapshot -> {
                    val gson = Gson()
                    val grocery = gson.fromJson(gson.toJson(item.data), GroceryItem::class.java)
                    grocery.id = item.id
                    return grocery
                }
                is DocumentSnapshot -> {
                    val gson = Gson()
                    val grocery = gson.fromJson(gson.toJson(item.data), GroceryItem::class.java)
                    grocery.id = item.id
                    return grocery
                }
                else -> throw ClassCastException()
            }
        }
    }

    fun toMap(): Map<String, String> {
        val map = HashMap<String, String>()
        map.put(FIELD_ID, id)
        map.put(FIELD_DESCRIPTION, description)
        map.put(FIELD_QUANTITY, quantity)
        map.put(FIELD_IMAGE, image ?: "")
        map.put(FIELD_COMMENTS, comments ?: "")

        return map
    }

    fun toJson(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
}