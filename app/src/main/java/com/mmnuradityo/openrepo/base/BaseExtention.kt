package com.mmnuradityo.openrepo.base

import android.content.Context
import android.text.method.LinkMovementMethod
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.mmnuradityo.openrepo.R
import com.mmnuradityo.openrepo.viewmodel.VMFactory
import java.util.*

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply(action).commit()
}

fun <T : Fragment> BaseActivity.replaceFragment(frameId: Int, fragment: T) {
    val currentFragment = supportFragmentManager.findFragmentByTag(fragment::class.simpleName)
    currentFragment?.let {
        if (it::class.simpleName == fragment::class.simpleName) return
    }

    supportFragmentManager.transact {
        replace(frameId, fragment, fragment::class.simpleName)
    }
}

fun <T : Fragment> BaseActivity.replaceFragmentWithBackStack(frameId: Int, fragment: T) {
    supportFragmentManager.transact {
        replace(frameId, fragment, fragment::class.simpleName)
        addToBackStack(fragment::class.simpleName)
    }
}

fun <T : Fragment> BaseActivity.addFragment(frameId: Int, fragment: T) {
    supportFragmentManager.transact {
        add(frameId, fragment, fragment::class.simpleName)
        addToBackStack(null)
    }
}

fun <T : ViewModel> BaseActivity.obtainVM(vmClass: Class<T>) = ViewModelProviders.of(
    this, VMFactory.getInstance(application)
).get(vmClass)

val Context.glide: RequestManager
    get() = Glide.with(this)

fun ImageView.load(path: String, request: RequestOptions) {
    context.glide.load(path).apply(request).into(this)
}

@BindingAdapter("app:imageUrl")
fun loadImage(view: ImageView, url: String?) {
    url?.let {
        view.load(
            it,
            RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .dontAnimate()
                .placeholder(R.drawable.ic_not_support)
                .error(R.drawable.ic_not_support)
        )
    }
}

@BindingAdapter("app:avatarUrl")
fun loadAvatar(view: ImageView, url: String?) {
    url?.let {
        view.load(
            it,
            RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transform(CenterCrop(), CircleCrop())
                .dontAnimate()
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
        )
    }
}

fun Context.getDrawableImage(drawable: Int) =
    ContextCompat.getDrawable(this, drawable)

fun Button.setActivatedButton(isActive: Boolean?) {
    isActive?.let {
        background = if (it)
            context.getDrawableImage(R.drawable.bg_button)
        else
            context.getDrawableImage(R.drawable.ic_circle_darkgray)

        isEnabled = it
        isClickable = it
        isActivated = it
    }
}

@BindingAdapter("app:btnActivated")
fun setBtnActivated(btn: Button, isActive: Boolean?) {
    btn.setActivatedButton(isActive)
}

@BindingAdapter("app:textLinkable")
fun setTextLinkable(tv: TextView, isActive: Boolean?) {
    isActive?.let {
        if (it) tv.movementMethod = LinkMovementMethod.getInstance()
        else tv.movementMethod = null
    }
}

@BindingAdapter("app:language")
fun imageLanguage(iv: ImageView, language: String?) {
    iv.setImageResource(
        when (language?.toLowerCase(Locale.ROOT)) {
            "kotlin" -> R.drawable.ic_kotlin
            "java" -> R.drawable.ic_java
            else -> R.drawable.ic_not_support
        }
    )
}


