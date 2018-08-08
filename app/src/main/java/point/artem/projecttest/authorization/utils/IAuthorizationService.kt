package point.artem.projecttest.authorization.utils

import android.content.Intent
import point.artem.projecttest.authorization.model.UserModel


interface IAuthorizationService {
    fun login()
    fun getUser()
    fun logout()
    fun result(requestCode: Int, responseCode: Int, data: Intent?)
}