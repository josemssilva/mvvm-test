package pt.josemssilva.bucketlist.data.models

data class Session(val token: String?, val user: User) {

    companion object {
        val NULL = Session("", User.NULL)
    }

    val isNull = token.isNullOrEmpty() && user.isNull()
}