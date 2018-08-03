package point.artem.projecttest.authorization.utils.facebook

import android.util.Log
import com.facebook.*
import com.facebook.login.LoginResult
import point.artem.projecttest.authorization.model.UserModel
import point.artem.projecttest.authorization.present.IAuthorizationPresent
import point.artem.projecttest.authorization.utils.IAuthorizationService
import com.facebook.FacebookException
import com.facebook.FacebookCallback
import com.facebook.login.LoginManager
import com.facebook.ProfileTracker






class ApiFacebookService(val present:IAuthorizationPresent):IAuthorizationService{
    private var userModel:UserModel? = null
    private var profileTracker:ProfileTracker?=null

    init {
        profileTracker = object : ProfileTracker() {
            override fun onCurrentProfileChanged(
                    oldProfile: Profile?, currentProfile: Profile?) {
                if (currentProfile!=null){
                    Log.i("Profile",currentProfile!!.getProfilePictureUri(100,100).toString())
                    userModel = UserModel("Facebook",currentProfile!!.firstName, currentProfile.lastName ,currentProfile.getProfilePictureUri(100,100).toString())
                }else{
                    userModel = UserModel("Facebook",null,null,null)
                }
                getUser()
            }
        }
    }

    override fun getUser(){
        if (userModel==null) userModel = UserModel("Facebook",Profile.getCurrentProfile().firstName,Profile.getCurrentProfile().lastName,Profile.getCurrentProfile().getProfilePictureUri(100,100).toString())
        present.addUser(userModel!!)
    }


    fun authorization() : FacebookCallback<LoginResult> {
        return object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                present.complete()
            }

            override fun onCancel() {
                present.error("Не подключился к Facebook")
                Log.i("facebook", "Не подключился к Facebook")
            }

            override fun onError(exception: FacebookException) {
                exception.printStackTrace()
            }
        }
    }

    fun callback(callbackManager:CallbackManager?){
        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        Log.i("Facebook", "Зашел в facebook")
                        present.complete()
                    }

                    override fun onCancel() {
                        Log.i("Facebook", "Пользователь закрыл окно")
                    }

                    override fun onError(exception: FacebookException) {
                        exception.printStackTrace()
                        present.error("Возникла ошибка:${exception.message}")
                    }
                }
        )
    }
    override fun logout(){
        if (profileTracker!=null) profileTracker!!.stopTracking();
        LoginManager.getInstance().logOut()
    }
}