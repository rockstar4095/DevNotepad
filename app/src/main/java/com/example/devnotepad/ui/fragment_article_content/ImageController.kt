package com.example.devnotepad.ui.fragment_article_content

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.devnotepad.ArticleImage
import com.example.devnotepad.data.data_handlers.HandlerForContentData
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

class ImageController(
    private val item: ArticleImage,
    private val imageView: ImageView,
    loadingPlaceholder: View,
    noInternetConnectionPlaceholder: View,
    heightPlaceholder: View
) : DynamicHeightViewsController(
    item,
    loadingPlaceholder,
    noInternetConnectionPlaceholder,
    heightPlaceholder
) {

    private val viewsContext = imageView.context
    private val imagesIdHeightHashMap: HashMap<String, Int> = HashMap()

    init {
        loadImage()
        setPlaceholdersHeight()
        showLoadingPlaceholder()
        observeWereElementsLoaded()
        imageView.setOnClickListener { v -> transition(v) }
    }

    private fun loadImage() {
        Picasso.get().load(item.url)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .into(imageView, object : Callback {
                override fun onSuccess() {
                    hideLoadingPlaceholder()
                    showView(imageView)
                }

                override fun onError(e: Exception?) {
                    // handle error
                }
            })
    }

    companion object {
        var imagesHeightIdHashMap: MutableLiveData<HashMap<String, Int>> = MutableLiveData()
        const val INTENT_KEY_IMAGE_URL = "intent key image url"
    }

    override fun observeWereElementsLoaded() {
        /**TODO: remove from parent class. Implement in CodeSnippetsController only.*/
    }

    override fun removeObserverWereElementsLoaded() {
        CoroutineScope(Dispatchers.Main).launch {
            HandlerForContentData.wereImagesLoadedLiveData.removeObservers(viewsContext as LifecycleOwner)
        }
    }

    override fun saveViewHeight(view: View) {
        Timer().schedule(1500) {
            val viewMeasuredHeight = view.measuredHeight
            println("debug: webViewMeasuredHeight = $viewMeasuredHeight from controller")

            imagesIdHeightHashMap[ArticleContentFragment.KEY_ID_FOR_DYNAMIC_VIEWS] =
                item.idFromServer
            imagesIdHeightHashMap[ArticleContentFragment.KEY_HEIGHT_FOR_DYNAMIC_VIEWS] =
                viewMeasuredHeight

            imagesHeightIdHashMap.postValue(imagesIdHeightHashMap)
        }
    }

    private fun transition(view: View?) {
        val intent = Intent(viewsContext, ImageViewActivity::class.java)
        intent.putExtra(INTENT_KEY_IMAGE_URL, item.url)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(viewsContext as Activity, view!!, "test")
        viewsContext.startActivity(intent, options.toBundle())
    }
}