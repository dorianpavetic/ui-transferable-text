package hr.dorianpavetic.ui_transferable_text.text

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import hr.dorianpavetic.ui_transferable_text.text.SeparatableUiTransferableText.Companion.DEFAULT_SEPARATOR
import hr.dorianpavetic.ui_transferable_text.text.SeparatableUiTransferableText.Companion.prependSeparator
import java.io.Serializable

interface UiTransferableText : Serializable {

    /**
     * Resolve/translate given text.
     *
     * @param context Required context to resolve texts from resources.
     * @return translated and localized text.
     */
    fun getText(context: Context): CharSequence

    @Suppress("unused", "MemberVisibilityCanBePrivate")
    companion object {
        fun combined(vararg texts: UiTransferableText) = combined(texts.toMutableList())
        fun combined(separator: CharSequence, vararg texts: UiTransferableText) =
            combined(texts.toMutableList(), separator)
        fun combined(texts: List<UiTransferableText>, separator: CharSequence = DEFAULT_SEPARATOR) =
            CombinedText(separator, texts)

        fun pluralsResId(@PluralsRes value: Int, quantity: Int, vararg args: Any) =
            pluralsResId(value, null, quantity, *args)
        fun pluralsResId(@PluralsRes value: Int, caseTransformType: CaseTransformType?, quantity: Int, vararg args: Any) =
            PluralsResId(value, caseTransformType, quantity, args.toList())

        fun stringResId(@StringRes value: Int, vararg args: Any) =
            stringResId(value, null, *args)
        fun stringResId(@StringRes value: Int, caseTransformType: CaseTransformType?, vararg args: Any) =
            StringResId(value, caseTransformType, args.toList())

        fun stringResIdList(@StringRes stringResId: List<Int>) = StringResIdList(-1, stringResId)
        fun stringResIdList(@StringRes separator: Int, @StringRes stringResId: List<Int>) =
            StringResIdList(separator, stringResId.toMutableList())

        fun text(value: CharSequence) = Text(value)

        fun lazyResolvable(resolveFunction: (Context) -> CharSequence) = LazyResolvable(resolveFunction)
    }


    /**
     * Generic container for combining multiple [UiTransferableText]s.
     * First element in the list is separator used to join values
     * @property value List of [UiTransferableText] to join in a single text.
     */
    @JvmInline
    value class CombinedText private constructor(
        override val value: List<UiTransferableText>
    ) : SeparatableUiTransferableText<UiTransferableText> {

        internal constructor(
            separator: CharSequence = DEFAULT_SEPARATOR,
            texts: List<UiTransferableText>
        ) : this(prependSeparator(text(separator), texts))

        override fun resolveSingleItemText(
            context: Context, item: UiTransferableText, isSeparator: Boolean
        ) = item.getText(context)

    }

    /**
     * Container for [StringRes] with optional arguments and case transformation.
     *
     * @property stringResId ID of the String resource.
     * @property caseTransformType Optional case transformation type of
     * the translated text.
     * @property args Arguments to supply to given [stringResId].
     */
    data class StringResId(
        @JvmField
        @StringRes
        val stringResId: Int,
        val caseTransformType: CaseTransformType?,
        override val args: List<Any>
    ) : UiTransferableText, Argumentable {
        override fun getText(context: Context) : String {
            // Resolves arguments texts if argument is UiTransferableText
            val resolvedArgs = resolveArgs(context)

            val text = if (args.isEmpty())
                context.getString(this.stringResId)
            else
                context.getString(this.stringResId, *resolvedArgs.toTypedArray())
            return caseTransformType?.transform(text) ?: text
        }
    }

    /**
     * Container for [PluralsRes] with optional arguments and case transformation.
     *
     * @property pluralsResId ID of the Plural resource.
     * @property caseTransformType Optional case transformation type of
     * the translated text.
     * @property quantity Quantity to use for [pluralsResId].
     * @property args Arguments to supply to given [pluralsResId].
     */
    data class PluralsResId(
        @JvmField
        @PluralsRes
        val pluralsResId: Int,
        val caseTransformType: CaseTransformType?,
        val quantity: Int,
        override val args: List<Any>
    ) : UiTransferableText, Argumentable {
        override fun getText(context: Context) : String {
            // Resolves arguments texts if argument is UiTransferableText
            val resolvedArgs = resolveArgs(context)

            val text = if (args.isEmpty())
                context.resources.getQuantityString(this.pluralsResId, quantity)
            else
                context.resources.getQuantityString(this.pluralsResId, quantity, *resolvedArgs.toTypedArray())
            return caseTransformType?.transform(text) ?: text
        }
    }

    /**
     * Container for combined list of [StringRes] joined by ', '.
     *
     * - Used for lists of string resources: e.g. "VISA, AMEX, MASTERCARD" etc,
     * where each item is [StringRes].
     *
     * @property stringResIdList List of String resource IDs.
     */
    @JvmInline
    value class StringResIdList private constructor(
        @StringRes
        override val value: List<Int>,
    ) : SeparatableUiTransferableText<Int> {

        internal constructor(
            separator: Int,
            texts: List<Int>
        ) : this(prependSeparator(separator, texts))

        override fun resolveSingleItemText(
            context: Context,
            item: Int,
            isSeparator: Boolean
        ): CharSequence {
            // Uses default separator if no valid separator is provided
            if (isSeparator && item == -1)
                return DEFAULT_SEPARATOR
            return context.getString(item)
        }

    }

    /**
     * Container for plain text as is - [CharSequence] used to support
     * passing spannable texts or other [CharSequence] subtypes.
     *
     * - Used for general purpose and non-translatable texts.
     *
     * @property text Text to resolve.
     */
    @JvmInline
    value class Text(
        @JvmField
        val text: CharSequence
    ) : UiTransferableText {
        override fun getText(context: Context) = text
    }

    /**
     * Container for data type that allows passing function that should
     * be resolved at runtime when `getText(Context)` is called, for example,
     * when `Context` is actually known and passed
     *  - Use case: when resolving text that requires `Context`
     */
    @JvmInline
    value class LazyResolvable(
        private val resolveFunction: (Context) -> CharSequence
    ) : UiTransferableText {
        override fun getText(context: Context) = resolveFunction.invoke(context)
    }

}
