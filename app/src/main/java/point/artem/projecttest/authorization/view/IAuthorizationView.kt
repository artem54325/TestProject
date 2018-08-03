package point.artem.projecttest.authorization.view

import android.app.Activity
import point.artem.projecttest.authorization.model.UserModel


interface IAuthorizationView {
    fun error(error:String)//Оповестит пользователя об ошибке
    fun complete()//Перейдет на активити GithubSearch
    fun getActivity():Activity//Получить активити
    fun updateUI(mode:String,isSignedIn: Boolean)//Изменение состояния кнопки
    fun showMenu(model: UserModel)//Показать menu
}