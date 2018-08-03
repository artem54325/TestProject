package point.artem.projecttest.authorization.utils.vk

import android.util.Log
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKApi
import com.vk.sdk.api.VKError
import com.vk.sdk.api.model.VKApiUser
import point.artem.projecttest.authorization.model.UserModel
import point.artem.projecttest.authorization.present.IAuthorizationPresent
import point.artem.projecttest.authorization.utils.IAuthorizationService
import com.vk.sdk.api.model.VKList
import com.vk.sdk.api.VKResponse
import com.vk.sdk.api.VKRequest




class ApiVkService(val present: IAuthorizationPresent): IAuthorizationService {
    var userModelVk:UserModel?=null

    override fun login() {
        VKSdk.login(present.getActivity());
    }
    override fun getUser(){
        if (userModelVk!=null){
            present.addUser(userModelVk!!)
        }else loadUser()
    }
    private fun loadUser(){//Запускается загрузка профиля в другом потоке
        VKApi.users().get().executeWithListener(object : VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse?) {
                val user = (response!!.parsedModel as VKList<VKApiUser>)[0]
                userModelVk=UserModel("VK", user?.first_name, user.last_name, user.photo_100)
                Log.i("Vk_user", userModelVk.toString())
                present.addUser(userModelVk!!)
            }
        })
    }

    override fun logout() {
        VKSdk.logout()
        present.updateUI("Google", false)
    }

    fun getCallback() :VKCallback<VKAccessToken>{
        return object : VKCallback<VKAccessToken> {
            override fun onResult(res: VKAccessToken) {
                present.complete()
                present.updateUI("VK",true)
                getUser()
            }
            override fun onError(error: VKError) {
                error("Ошибка VK")
                Log.i("VK","Ошибка ВК")//error.errorMessage-Выдает ошибку
            }
        }
    }
}