package point.artem.projecttest.authorization.utils

import point.artem.projecttest.authorization.model.UserModel


interface IAuthorizationService {
    fun getUser()
    fun logout()
}