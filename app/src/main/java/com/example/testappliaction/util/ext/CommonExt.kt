package com.example.testappliaction.util.ext

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testappliaction.util.BaseViewModelFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**Create Factory Class at runtime*/
inline fun <reified T : ViewModel> Fragment.getViewModel(noinline creator: (() -> T)? = null): T {
    return if (creator == null)
        ViewModelProvider(this).get(T::class.java)
    else
        ViewModelProvider(this, BaseViewModelFactory(creator)).get(T::class.java)
}

inline fun <reified T : ViewModel> FragmentActivity.getViewModel(noinline creator: (() -> T)? = null): T {
    return if (creator == null)
        ViewModelProvider(this).get(T::class.java)
    else
        ViewModelProvider(this, BaseViewModelFactory(creator)).get(T::class.java)
}

/** Log functions with Tag as caller class name */
inline fun <reified T : Any> T.logD(text: String) {
    Log.d(T::class.java.simpleName, "TAG ===> $text")
}
