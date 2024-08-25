package hr.dorianpavetic.ui_transferable_text.extension

import android.view.View
import android.widget.TextView
import hr.dorianpavetic.ui_transferable_text.text.UiTransferableText

fun TextView.setText(uiTransferableText: UiTransferableText?) {
    text = uiTransferableText?.getText(context)
}

fun TextView.setTextOrHide(text: CharSequence?, vararg relatedViews: View) {
    if (text != null) {
        this.text = text
        setBulkVisibility(View.VISIBLE, this, relatedViews)
    } else
        setBulkVisibility(View.GONE, this, relatedViews)
}

fun TextView.setTextOrHide(text: UiTransferableText?, vararg relatedViews: View) =
    setTextOrHide(text?.getText(context), *relatedViews)

private fun setBulkVisibility(visibility: Int, view: View, relatedViews: Array<out View>) {
    view.visibility = visibility
    relatedViews.forEach { relatedView ->
        relatedView.visibility = visibility
    }
}