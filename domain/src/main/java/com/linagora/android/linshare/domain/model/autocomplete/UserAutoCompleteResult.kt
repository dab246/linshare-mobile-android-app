package com.linagora.android.linshare.domain.model.autocomplete

import java.util.UUID

data class UserAutoCompleteResult(
    override val identifier: String,
    override val display: String,
    val firstName: String,
    val lastName: String,
    val domain: UUID,
    val mail: String
) : AutoCompleteResult

fun UserAutoCompleteResult.fullName(): String {
    return "$firstName $lastName"
        .takeIf { it.isNotBlank() && it.isNotEmpty() }
        ?: display
}
