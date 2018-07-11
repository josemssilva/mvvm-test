package pt.josemssilva.bucketlist.data.models

import android.content.Context
import android.graphics.Color
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.gson.*
import com.google.gson.annotations.SerializedName
import pt.josemssilva.bucketlist.R
import java.lang.reflect.Type

/**
 * Created by josesilva on 04/04/18.
 */
data class GroceryItem(
        var id: String,
        var description: String,
        var quantity: String,
        var image: String?,
        var comments: String?,
        var state: State
) {

    enum class State constructor(val value: Int) {
        FULL(3),
        SOME(2),
        FEW(1),
        NONE(0),
        UNKNOWN(-1);

        fun color(): Int {
            return when (this) {
                FULL    -> Color.GREEN
                SOME    -> Color.YELLOW
                FEW     -> Color.MAGENTA
                NONE    -> Color.RED
                UNKNOWN -> Color.GRAY
            }
        }

        fun readableString(context: Context): String {
            return context.getString(when (this) {
                NONE    -> R.string.groceries_item_state_none
                FEW     -> R.string.groceries_item_state_few
                SOME    -> R.string.groceries_item_state_some
                FULL    -> R.string.groceries_item_state_full
                UNKNOWN -> R.string.groceries_item_state_unknown
            })
        }
    }

    private constructor() : this("", "", "", "", "", State.UNKNOWN)

    companion object {

        const val FIELD_ID = "id"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_QUANTITY = "quantity"
        const val FIELD_IMAGE = "image"
        const val FIELD_COMMENTS = "comments"
        const val FIELD_STATE = "state"

        val EMPTY = GroceryItem()

        fun mapper(item: Any): GroceryItem {
            when (item) {
                is QueryDocumentSnapshot -> {
                    val gson = GsonBuilder()
                            .registerTypeAdapter(GroceryItem.State::class.java, GroceryItemStateDeserializer())
                            .create()
                    val grocery = gson.fromJson(gson.toJson(item.data), GroceryItem::class.java)
                    grocery.id = item.id
                    return grocery
                }
                is DocumentSnapshot -> {
                    if (item.data == null) {
                        return EMPTY
                    }

                    val gson = GsonBuilder()
                            .registerTypeAdapter(GroceryItem.State::class.java, GroceryItemStateDeserializer())
                            .create()
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
        map.put(FIELD_STATE, state.value.toString())

        return map
    }
}

class GroceryItemStateDeserializer : JsonDeserializer<GroceryItem.State> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): GroceryItem.State {
        val jsonValue = json?.asInt ?: return GroceryItem.State.UNKNOWN

        return when (jsonValue) {
            GroceryItem.State.NONE.value -> GroceryItem.State.NONE
            GroceryItem.State.FEW.value -> GroceryItem.State.FEW
            GroceryItem.State.SOME.value -> GroceryItem.State.SOME
            GroceryItem.State.FULL.value -> GroceryItem.State.FULL
            else -> GroceryItem.State.UNKNOWN
        }
    }
}