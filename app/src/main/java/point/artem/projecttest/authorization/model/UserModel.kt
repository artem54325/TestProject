package point.artem.projecttest.authorization.model

data class UserModel(
        val service:String,
        val first_name: String?,
        val last_name: String?,
        val urlImage: String?
)