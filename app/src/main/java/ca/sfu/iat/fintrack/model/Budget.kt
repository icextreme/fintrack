package ca.sfu.iat.fintrack.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Budget(val name: String, val startingAmount: Double, val period: String) {
    val balance: Double = 0.0
    val expense: Double = 0.0
    val income: Double = 0.0
    val records = ArrayList<Record>()

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "startingAmount" to startingAmount,
            "period" to period,
            "balance" to balance,
            "expense" to expense,
            "income" to income,
            "records" to records
        )
    }
}
