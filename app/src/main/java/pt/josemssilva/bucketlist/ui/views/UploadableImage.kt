package pt.josemssilva.bucketlist.ui.views

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import io.reactivex.subjects.PublishSubject
import pt.josemssilva.bucketlist.ViewBehaviour

/**
 * Created by josesilva on 10/04/18.
 */
class UploadableImage(context: Context, attr: AttributeSet?) : ImageView(context, attr), FetchImageBehaviour, View.OnClickListener {

    constructor(context: Context) : this(context, null)

    override val state: PublishSubject<Pair<ViewBehaviour.State, String>>
        get() = PublishSubject.create()

    override val fetchImage: PublishSubject<Boolean>
        get() = PublishSubject.create()

    var url: String? = null
        set(value) {
            Glide.with(this)
                    .asBitmap()
                    .load(value)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                            state.onNext(Pair(ViewBehaviour.State.FAILURE, e?.message ?: ""))
                            return false
                        }

                        override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            state.onNext(Pair(ViewBehaviour.State.SUCCESS, ""))
                            return false
                        }
                    })
                    .into(this)
        }

    override fun onClick(p0: View?) {
        fetchImage.onNext(true)
    }

    override fun imageLocalPath(path: String) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}