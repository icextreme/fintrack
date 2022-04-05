package ca.sfu.iat.fintrack.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Budget(val name: String, val startingAmount: Double) {
    private val balance: Double = 0.0
    private val expense: Double = 0.0
    private val income: Double = 0.0

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "startingAmount" to startingAmount,
            "balance" to balance,
            "expense" to expense,
            "income" to income,
        )
    }
}
