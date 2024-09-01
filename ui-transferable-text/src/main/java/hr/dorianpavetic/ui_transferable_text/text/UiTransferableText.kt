package hr.dorianpavetic.ui_transferable_text.text

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import java.io.Serializable

interface UiTransferableText : Serializable {

    /**
     * Resolve/translate given text.
     *
     * @param context Required context to resolve texts from resources.
     * @return translated and localized text.
     */
    fun getText(context: Context): CharSequence

    companion object {
        fun combined(vararg texts: UiTransferableText) = combined(texts.toMutableList())
        fun combined(separator: CharSequence, vararg texts: UiTransferableText) =
            combined(texts.toMutableList(), separator)
        fun combined(texts: List<UiTransferableText>, separator: CharSequence = ", ") =
            CombinedText(separator, texts)

        fun pluralsResId(@PluralsRes value: Int, quantity: Int, vararg args: Any) =
            pluralsResId(value, null, quantity, *args)
        fun pluralsResId(@PluralsRes value: Int, caseTransformType: CaseTransformType?, quantity: Int, vararg args: Any) =
            PluralsResId(value, caseTransformType, quantity, args.toList())

        fun stringResId(@StringRes value: Int, vararg args: Any) =
            stringResId(value, null, *args)
        fun stringResId(@StringRes value: Int, caseTransformType: CaseTransformType?, vararg args: Any) =
            StringResId(value, caseTransformType, args.toList())

        fun stringResIdList(@StringRes values: Collection<Int>) = StringResIdList(values)

        fun text(value: CharSequence) = Text(value)
    }


    /**
     * Generic container for combining multiple [UiTransferableText]s.
     * First element in the list is separator used to join values
     * @property value List of [UiTransferableText] to join in a single text.
     */
    @JvmInline
    value class CombinedText(private val value: List<UiTransferableText>) : UiTransferableText {

        private val separator: CharSequence
            get() = (value.first() as Text).text

        constructor(
            separator: CharSequence = ", ",
            texts: List<UiTransferableText>
        ) : this(prependSeparator(separator, texts))

        companion object {
            private fun prependSeparator(
                separator: CharSequence = ", ",
                texts: List<UiTransferableText>
            ) = texts.toMutableList().also {
                it.add(0, text(separator))
            }
        }

        override fun getText(context: Context): String =
            value.drop(1).joinToString(separator) { it.getText(context) }

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
    value class StringResIdList(
        @JvmField
        @StringRes
        val stringResIdList: Collection<Int>,
    ) : UiTransferableText {
        override fun getText(context: Context) =
            stringResIdList.joinToString { context.getString(it) }
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

}
