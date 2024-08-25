package hr.dorianpavetic.ui_transferable_text.text;

/**
 * Type of the transformation to apply to given text.
 */
enum class CaseTransformType {
    /**
     * Makes entire text uppercased.
     */
    UPPERCASE,

    /**
     * Uppercases text except the first **letter**.
     *  - First letter might not actually be first char, in case of
     *  for example special character '\n' etc.
     */
    UPPERCASE_AFTER_FIRST_LETTER,

    /**
     * Makes entire text lowercased.
     */
    LOWERCASE,

    /**
     * Lowercases text except the first **letter**.
     *  - First letter might not actually be first char, in case of
     *  for example special character '\n' etc.
     */
    LOWERCASE_AFTER_FIRST_LETTER,

    /**
     * Capitalizes text - makes first letter uppercased.
     */
    CAPITALIZE,

    /**
     * Decapitalizes text - makes first letter lowercased.
     */
    DECAPITALIZE;

    fun transform(transformText: String) : String {
        return when(this) {
            UPPERCASE -> transformText.uppercase()
            UPPERCASE_AFTER_FIRST_LETTER -> {
                // Ignores digits and special characters on start, like '\n' and searches for letter
                val firstLetterIndex = transformText.indexOfFirst { it.isLetter() }
                transformText.substring(0..firstLetterIndex) +
                        transformText.substring(firstLetterIndex+1).uppercase()
            }
            LOWERCASE -> transformText.lowercase()
            LOWERCASE_AFTER_FIRST_LETTER -> {
                // Ignores digits and special characters on start, like '\n' and searches for letter
                val firstLetterIndex = transformText.indexOfFirst { it.isLetter() }
                transformText.substring(0..firstLetterIndex) +
                        transformText.substring(firstLetterIndex+1).lowercase()
            }
            CAPITALIZE -> transformText.replaceFirstChar { it.uppercaseChar() }
            DECAPITALIZE -> transformText.replaceFirstChar { it.lowercaseChar() }
        }
    }

}