package pt.josemssilva.bucketlist

import io.reactivex.subjects.PublishSubject

/**
 * Created by josesilva on 10/04/18.
 */
interface ViewBehaviour {

    enum class State {
        SUCCESS,
        FAILURE
    }

    val state: PublishSubject<Pair<State, String>>

}