package point.artem.projecttest.authorization.present

import android.app.Activity
import android.content.Intent
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import point.artem.projecttest.authorization.AuthorizationActivity
import point.artem.projecttest.authorization.model.UserModel
import point.artem.projecttest.authorization.utils.facebook.ApiFacebookService
import point.artem.projecttest.authorization.utils.google.ApiGoogleService
import point.artem.projecttest.authorization.utils.vk.ApiVkService
import point.artem.projecttest.authorization.view.IAuthorizationView


class AuthorizationPresent(val view: IAuthorizationView):IAuthorizationPresent {
    var facebook:ApiFacebookService= ApiFacebookService(this)
    var google:ApiGoogleService=ApiGoogleService(this)
    var vk: ApiVkService=ApiVkService(this)

    var map:HashMap<String, UserModel> = HashMap<String, UserModel>()

    override fun login(mode: String) {
        when(mode){
            "Google" ->google.login()
            "VK" ->vk.login()
            "Facebook" ->facebook.login()
        }
    }

    override fun logout(mode: String) {
        when(mode){
            "Google" ->     google.logout()
            "VK" ->         vk.logout()
            "Facebook" ->   facebook.logout()
        }
    }

    override fun getActivity(): Activity = view.getActivity()

    override fun addUser(model: UserModel) {
       map.put(model.service, model)
       view.showMenu(model)
    }
    override fun complete() {
        view.complete()
    }
    override fun error(error: String) {
        view.error(error)
    }
    override fun updateUI(mode: String, boolean: Boolean) {
        view.updateUI(mode, boolean)
    }

    fun close(){
        vk.logout()
        facebook.logout()
        google.logout()
    }

    override fun activityResultPresent(requestCode: Int, responseCode: Int, data: Intent?) {
        vk.result(requestCode, responseCode, data)
        google.result(requestCode, responseCode, data)
        facebook.result(requestCode, responseCode, data)
    }
}