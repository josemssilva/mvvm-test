package pt.josemssilva.bucketlist.data.models

data class User(val email: String, val name: String) {
    companion object {
        val NULL = User("", "")
    }

    fun isNull() : Boolean {
        return email.isEmpty() && name.isEmpty()
    }
}