package ca.sfu.iat.fintrack.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Record(
    val recordName: String = "",
    val amount: Double = 0.0,
    val category: String = "",
    val type: String = "",
    var date: String = "01/01/1970"
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "recordName" to recordName,
            "amount" to amount,
            "category" to category,
            "type" to type,
            "date" to date
        )
    }
}
