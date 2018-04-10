package pt.josemssilva.bucketlist.ui.views

import io.reactivex.subjects.PublishSubject
import pt.josemssilva.bucketlist.ViewBehaviour

/**
 * Created by josesilva on 10/04/18.
 */
interface FetchImageBehaviour : ViewBehaviour {
    val fetchImage: PublishSubject<Boolean>

    fun imageLocalPath(path: String)
}