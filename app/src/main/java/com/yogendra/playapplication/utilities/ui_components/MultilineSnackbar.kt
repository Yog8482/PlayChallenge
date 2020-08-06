package com.yogendra.socialmediamvvm.utils.ui_components

import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class MultilineSnackbar(var context: View, private var message: String) {
    fun show() {
        val snackbar =
            Snackbar.make(context, message, 5000)
        val snackbarView: View = snackbar.getView()
        val textView =
            snackbarView.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setMaxLines(5) // show multiple line
        snackbar.also { snack ->
            snack.setAction("OK") {
                snack.dismiss()
            }
        }.show()
    }

}

fun View.snackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()
}