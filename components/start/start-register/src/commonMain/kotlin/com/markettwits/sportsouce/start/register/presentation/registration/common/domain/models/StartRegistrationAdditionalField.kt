package com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models

import com.markettwits.sportsouce.start.cloud.model.start.fields.Option
import kotlinx.serialization.Serializable

@Serializable
data class StartRegistrationAdditionalField(
    val id : Int,
    val type : Type,
    val title : String,
    val options : List<Option>,
    val price : StartRegistrationFieldPrice,
    val isOptional : Boolean,
    val dependentFields : List<StartRegistrationAdditionalField>
){
    @Serializable
    enum class Type(val value : String){
        TEXT("Текст"),
        NUMBER("Число"),
        CHECKBOX("Чекбокс"),
        DATA("Дата"),
        TIME("Время"),
        MULTISELECT("Мультиселект"),
        SINGLE_SELECT("Синглселект")
    }
}
