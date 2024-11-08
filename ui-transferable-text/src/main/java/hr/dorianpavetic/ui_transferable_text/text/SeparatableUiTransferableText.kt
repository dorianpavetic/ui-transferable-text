package hr.dorianpavetic.ui_transferable_text.text

import android.content.Context

/**
 * Special [UiTransferableText] type that containerizes multiple
 * items and can use separator.
 *
 * - Separator is always first value in the list
 * - By default, when resolving [getText], [textList] is joined
 * by resolved [separator].
 */
interface SeparatableUiTransferableText<T>: UiTransferableText {

    val value: Collection<T>

    val textList: List<T>
        get() = value.drop(1)

    val separator: T
        get() = value.first()

    fun resolveSingleItemText(context: Context, item: T, isSeparator: Boolean): CharSequence

    override fun getText(context: Context): CharSequence {
        val separatorText = this.resolveSingleItemText(context, separator, true)
        return textList.joinToString(separatorText) { this.resolveSingleItemText(context, it, false) }
    }

    companion object {
        const val DEFAULT_SEPARATOR = ", "

        fun <T> prependSeparator(
            separator: T,
            textList: List<T>
        ) = textList.toMutableList().also {
            it.add(0, separator)
        }
    }

}