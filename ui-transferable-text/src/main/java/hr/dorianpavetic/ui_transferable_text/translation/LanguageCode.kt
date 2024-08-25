package hr.dorianpavetic.ui_transferable_text.translation

import androidx.annotation.StringDef
import hr.dorianpavetic.ui_transferable_text.translation.LanguageCode.Companion.EN
import hr.dorianpavetic.ui_transferable_text.translation.LanguageCode.Companion.HR

@Target(
	AnnotationTarget.PROPERTY,
	AnnotationTarget.VALUE_PARAMETER,
	AnnotationTarget.FUNCTION,
	AnnotationTarget.PROPERTY_GETTER,
	AnnotationTarget.TYPE
)
@Retention(AnnotationRetention.SOURCE)
@StringDef(EN, HR)
annotation class LanguageCode {
	companion object {
		const val EN = "en"
		const val HR = "hr"
	}
}