package pt.isel.pdm.li51d.g10.yama.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog

fun hideKeyboard(context: Context, view: View?) {
    if (view != null) {
        val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun showDialogYesNo(context: Context,
                    title: Int, msg: Int,
                    yes: (Unit) -> Unit, no: (Unit) -> Unit) {

    val builder = AlertDialog.Builder(context)
    builder.setTitle(context.resources.getString(title))
    builder.setMessage(context.resources.getString(msg))
    builder.setPositiveButton("Yes"){ _, _ -> yes.invoke(Unit) }
    builder.setNegativeButton("No") { _, _ ->  no.invoke(Unit) }

    val dialog: AlertDialog = builder.create()
    dialog.setCancelable(false)
    dialog.show()
}