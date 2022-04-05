package ca.sfu.iat.fintrack.model

import java.time.LocalTime

data class User(val firstName: String,
                val lastName: String,
                val email: String,
) {
    val budgetList = ArrayList<Budget>()
}
