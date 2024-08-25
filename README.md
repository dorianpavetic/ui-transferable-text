[![](https://jitpack.io/v/dorianpavetic/ui-transferable-text.svg)](https://jitpack.io/#dorianpavetic/ui-transferable-text)

# ui-transferable-text
Library which enables passing localized text between Android context-aware and contextless components - e.g. between ViewModel and Fragment

## Getting started
To start using library, add dependency to your Android gradle file (not root one):
```
dependencies {
  implementation 'com.github.dorianpavetic:ui-transferable-text:v1.0.0'
}
```
## Implementation

### Resolving `UiTransferableText`
```kotlin
val text = UiTransferableText.stringResId(R.string.dog)

// Using existing Android API
binding.textView.text = text.getText(requireContext())

// Using extensions
binding.textView.setText(text)
```

### StringResId
```kotlin
val textNoArgs = UiTransferableText.stringResId(R.string.formatted_heated_pool)
val textWithArgs = UiTransferableText.stringResId(R.string.formatted_pool_temp, "32 F")
// Argument name 'caseTransformType' is required to prevent ambiguity
val textWithArgsAndCaseTransform = UiTransferableText.stringResId(
   R.string.formatted_pool_temperature,
   caseTransformType = UiTransferableText.CaseTransformType.CAPITALIZE,
   "32 F"
)
```

### StringResIdList
```kotlin
// Resolves as "dog, cat, pig"
val petNamesText = UiTransferableText.stringResIdList(R.string.pet_name_dog, R.string.pet_name_cat, R.string.pet_name_pig)
```

### PluralResId
```kotlin
// Resolves as "Item" for 1 quantity and "Items" for more
val textNoArgs = UiTransferableText.pluralsResId(R.plurals.plural_item, 3)
// Resolves as "1 item" for 1 quantity and "n items" for more (in this case n=3)
val textWithArgs = UiTransferableText.pluralsResId(R.plurals.formatted_plural_item, 3, 3)
// Resolves as "item count: one" for 1 quantity and "items count: n" for more (in this case n=3)
val textWithArgsAndCaseTransform = UiTransferableText.pluralsResId(
 R.plurals.formatted_plural_item_counts,
 UiTransferableText.CaseTransformType.DECAPITALIZE,
 3,
 3
)
```

### Translatable
```kotlin
val translatableMap = TranslatableMap.emptyMap()
translatableMap[LanguageCode.EN] = "Cat"
translatableMap[LanguageCode.HR] = "Mačka"
// For English resolves to "Cat" but for Croatian resolves to "Mačka"
val text = UiTransferableText.translatable(translatableMap)
```

### Text
```kotlin
// Resolves as "Garden"
val text = UiTransferableText.text("Garden")
```

### Combined
```kotlin
// Resolves to "First text. Second text"
val singleLineText = UiTransferableText.combined(
   ". ",
   UiTranferableText.text("First text"), UiTranferableText.text("Second text")
)
// Resolves to "First text, second text"
val singleLineTextComma = UiTransferableText.combined(
   ", ",
   UiTranferableText.text("First text"), UiTranferableText.text("second text")
)
/* Resolves to "
  First line.
  Second line"
*/
val multiLineText = UiTransferableText.combined(
   ".\n",
   UiTranferableText.text("First line"), UiTranferableText.text("Second line")
)
```

### Complex case #1:
```kotlin
private fun getAllowedPetsTypesWithRestrictionText(
  petPolicy: PetPolicy,
  /** "Dog"/"Cat"/"Pet"... */
  allowedTypeAppendableText: UiTransferableText.StringResId
 ): UiTransferableText {
  val maxPetQuantity = petPolicy.maxPetQuantity
  val maxTotalPetWeightKg = petPolicy.maxTotalPetWeightKg

  return if (maxPetQuantity != null && maxTotalPetWeightKg != null) {
   // a) maxPetQuantity > 1 --> Restricted to more than 1 pet and N kg
   //  - "Stay of up to X pets per room of total weight N kg is allowed"
   // b) maxPetQuantity == 1 --> Restricted to 1 pet and N kg
   //  - "Stay of one pet of total weight of N kg per room is allowed"
   UiTransferableText.pluralsResId(
    value = R.plurals.formatted_pets_allowed_quantity_and_weight,
    caseTransformType = UiTransferableText.CaseTransformType.CAPITALIZE,
    maxPetQuantity,
    allowedTypeAppendableText,
    getAllowedPetsNameQuantified(petPolicy, maxPetQuantity),
    maxTotalPetWeightKg.formattedShort()
   )
  } else if (maxPetQuantity != null) {
   // a) maxPetQuantity > 1 --> Restricted to more than 1 pet (without weight)
   //  - "Stay of up to X pets per room is allowed"
   // b) maxPetQuantity == 1 --> Restricted to 1 pet (without weight)
   //  - "Stay of one pet per room is allowed"
   UiTransferableText.pluralsResId(
    value = R.plurals.formatted_pets_allowed_quantity_only,
    caseTransformType = UiTransferableText.CaseTransformType.CAPITALIZE,
    maxPetQuantity,
    allowedTypeAppendableText,
    getAllowedPetsNameQuantified(petPolicy, maxPetQuantity)
   )
  } else if (maxTotalPetWeightKg != null) {
   // Restricted to N kg (without quantity)
   //  - "Stay of pets with total weight of N kg per room is allowed"
   UiTransferableText.stringResId(
    value = R.string.formatted_pets_allowed_weight_only,
    caseTransformType = UiTransferableText.CaseTransformType.CAPITALIZE,
    allowedTypeAppendableText,
    getAllowedPetsName(petPolicy),
    maxTotalPetWeightKg.formattedShort()
   )
  } else {
   // No quantity nor weight restrictions
   //  - "Stay of pets is allowed"
   UiTransferableText.stringResId(
    value = R.string.formatted_pets_allowed_no_restrictions,
    caseTransformType = UiTransferableText.CaseTransformType.CAPITALIZE,
    allowedTypeAppendableText,
    getAllowedPetsName(petPolicy)
   )
  }
 }
```

## Medium
This library is result of the Medium article, where you can see more detailed explainations of library code:
https://medium.com/@dorianpavetic/android-localize-text-in-viewmodel-f1521aff3583

