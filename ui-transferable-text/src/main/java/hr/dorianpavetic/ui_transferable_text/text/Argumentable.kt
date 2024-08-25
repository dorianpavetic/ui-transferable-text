package hr.dorianpavetic.ui_transferable_text.text

import android.content.Context

/**
 * Transferable text that accepts arguments and enables resolving them recursively
 */
interface Argumentable {
    val args: List<Any>

    fun resolveArgs(context: Context) =
        args.map {
            if (it is UiTransferableText)
                it.getText(context)
            else
                it
        }
}