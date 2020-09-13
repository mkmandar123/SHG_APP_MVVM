package com.digitaldetox.aww.util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

import android.os.Bundle
import android.os.Parcelable

fun Any.unitify() {

}


inline fun <reified T> Gson.fromJson(json: String) =
    this.fromJson<T>(json, object : TypeToken<T>() {}.type)


fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toastLong(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun String.getValidLetter(): String {
    if (this.isEmpty()) return "K"
    val letter = this.get(0).toString()
    val regex = "[^a-zA-Z]".toRegex()
    val newLetter = letter.toUpperCase(Locale.getDefault()).replace(regex, "K")
    if (newLetter == "Q") return "O"
    if (newLetter == "W") return "V"
    return newLetter
}


//fun caloriesToEnergy(calories: Float): Float {
//    return calories / CALORIES_TO_ENERGY_MULTIPLICATOR
//}

fun String.containsNumbers(): Boolean {

    var contains = false
    this.forEach { if (it.isDigit()) contains = true }
    return contains
}

fun String.toFloatOrZero(): Float {

    return if (this.containsNumbers()) this.toFloatOrNull() ?: 0f
    else 0f
}

fun View.hideKeyboard(context: Context) {
    try {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Activity.hideKeyboard() {
    try {
        val view = this.findViewById<View>(android.R.id.content)
        if (view != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.isConnected() = run {
    val conManager =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val ni = conManager.activeNetworkInfo
    ni != null && ni.isConnected
}

fun View.setMargins(
    leftMarginDp: Int? = null,
    topMarginDp: Int? = null,
    rightMarginDp: Int? = null,
    bottomMarginDp: Int? = null
) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val params = layoutParams as ViewGroup.MarginLayoutParams
        leftMarginDp?.run { params.leftMargin = this.dpToPx(context) }
        topMarginDp?.run { params.topMargin = this.dpToPx(context) }
        rightMarginDp?.run { params.rightMargin = this.dpToPx(context) }
        bottomMarginDp?.run { params.bottomMargin = this.dpToPx(context) }
        requestLayout()
    }
}


fun Int.dpToPx(context: Context?): Int {
    if (context == null) return this
    val metrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics).toInt()
}


fun Float.round(decimals: Int): Float {
    return BigDecimal(this.toDouble()).setScale(decimals, RoundingMode.HALF_EVEN).toFloat()
}





fun bundleOfParcelable(vararg pairs: Pair<String, Parcelable?>): Bundle {
    return Bundle().apply {
        pairs.forEach {
            putParcelable(it.first, it.second)
        }
    }
}

