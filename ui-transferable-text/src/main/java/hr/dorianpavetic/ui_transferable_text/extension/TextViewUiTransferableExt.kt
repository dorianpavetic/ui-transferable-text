package hr.dorianpavetic.ui_transferable_text.extension

import android.view.View
import android.widget.TextView
import hr.dorianpavetic.ui_transferable_text.text.UiTransferableText

/**
 * Sets text - if [uiTransferableText] is null, text is cleared.
 *
 * @param uiTransferableText [UiTransferableText] container to resolve text from.
 */
fun TextView.setText(uiTransferableText: UiTransferableText?) {
    text = uiTransferableText?.getText(context)
}

/**
 * Sets text or hides TextView and other [relatedViews] if text is null.
 *
 * @param text Text to set.
 * @param relatedViews Views that shall be hidden in case [text] is null.
 */
fun TextView.setTextOrHide(text: CharSequence?, vararg relatedViews: View) {
    if (text != null) {
        this.text = text
        setBulkVisibility(View.VISIBLE, this, relatedViews)
    } else
        setBulkVisibility(View.GONE, this, relatedViews)
}


/**
 * Same as [setTextOrHide] but for [UiTransferableText].
 *
 * @param uiTransferableText [UiTransferableText] container to resolve text from.
 * @param relatedViews Views that shall be hidden in case [uiTransferableText] is null.
 */
fun TextView.setTextOrHide(uiTransferableText: UiTransferableText?, vararg relatedViews: View) =
    setTextOrHide(uiTransferableText?.getText(context), *relatedViews)

/**
 * Changes the visibility of [view] and [relatedViews].
 *
 * @param visibility Visibility constant value to be set.
 * @param view View to set visibility to.
 * @param relatedViews Other related views to [view] for which to update visibility.
 */
private fun setBulkVisibility(visibility: Int, view: View, relatedViews: Array<out View>) {
    view.visibility = visibility
    relatedViews.forEach { relatedView ->
        relatedView.visibility = visibility
    }
}