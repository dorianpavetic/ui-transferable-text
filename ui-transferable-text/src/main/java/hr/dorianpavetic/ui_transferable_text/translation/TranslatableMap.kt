package hr.dorianpavetic.ui_transferable_text.translation

import java.util.Locale

open class TranslatableMap : HashMap<String, String>, MutableMap<String, String> {

	var translation: String
		private set

	constructor() : super() {
		// On empty map, no translations so no need to redundantly translate --> assign empty string
		this.translation = ""
	}

	constructor(map: Map<String, String>) : super(map) {
		this.translation = translate()
	}

	override fun putIfAbsent(@LanguageCode key: String, value: String): String? {
		val putResult = super<HashMap>.putIfAbsent(key, value)
		retranslate()
		return putResult
	}

	override fun put(@LanguageCode key: String, value: String): String? {
		val putResult = super.put(key, value)
		retranslate()
		return putResult
	}

	override fun putAll(from: Map<out String, String>) {
		super.putAll(from)
		retranslate()
	}

	/**
	 * Specific implementation of [put] without retranslation on each entry add
	 * to prevent overhead on deserialization.
	 *  - Should only be used for special serialization/deserialization cases
	 */
	fun addOnlyWithoutTranslating(@LanguageCode key: String, value: String): String? {
		return super.put(key, value)
	}

	companion object {
		@JvmStatic
		fun emptyMap(): TranslatableMap {
			return TranslatableMap()
		}

		/**
		 * Static method that enables creating instance from any other [Map].
		 *
		 * @param anyMap any [Map] from which to create this instance.
		 * @return newly created [TranslatableMap].
		 */
		@JvmStatic
		fun fromAnyMap(anyMap: Map<String, String>): TranslatableMap {
			if (anyMap is TranslatableMap)
				return anyMap
			return TranslatableMap(anyMap)
		}

		@JvmStatic
		fun defaultTranslation(
			@LanguageCode languageCode: String, value: String
		): TranslatableMap {
			val myTranslationMap = TranslatableMap()
			myTranslationMap[languageCode] = value
			return myTranslationMap
		}

		fun defaultTranslation(value: String): TranslatableMap {
			return defaultTranslation(Locale.getDefault().language, value)
		}

		/**
		 * Retranslates values to a new translation - also changes internal [translation] var value
		 */
		@JvmStatic
		fun TranslatableMap.retranslate() {
			this.translation = this.translate()
		}

		/**
		 * Translates given translatable map for given language code.
		 * - If no entry is found, empty string is returned.
		 *
		 * @implSpec Priorities during determining translation:
		 * 1.) Entry for given language code exits.
		 * 2.) Find English [LanguageCode.EN].
		 * 3.) Find any (first entry).
		 * 4.) Empty String.
		 *
		 * @param languageCode language code for which to translate.
		 * @return translation or empty string of no translation is found.
		 */
		@JvmOverloads
		@JvmStatic
		fun Map<String, String>.translate(
			@LanguageCode languageCode: String = getLanguageCode()
		): String {
			return getOrElse(languageCode) {
				getOrElse(LanguageCode.EN) {
					values.firstOrNull() ?: ""
				}
			}
		}

		@JvmStatic
		@LanguageCode
		fun getLanguageCode(): String {
			return Locale.getDefault().language
		}

		@JvmStatic
		fun Map<String, String>.translated(): String {
			if (this is TranslatableMap)
				return this.translation
			return this.translate()
		}
	}
}