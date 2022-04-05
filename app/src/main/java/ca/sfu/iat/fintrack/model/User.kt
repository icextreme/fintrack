package ca.sfu.iat.fintrack.model

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
) {
    val budgetList = ArrayList<Budget>()
}
