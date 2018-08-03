package point.artem.projecttest.authorization.present

import android.app.Activity
import point.artem.projecttest.authorization.model.UserModel


interface IAuthorizationPresent {
    fun login(mode: String)
    fun logout(mode: String)
    fun getActivity(): Activity// получить активити
    fun complete()//Следующее активити
    fun error(error:String)//Оповестить об ошибке
    fun updateUI(mode:String, boolean: Boolean)//Изменение состояния кнопки
    fun addUser(model: UserModel)
}