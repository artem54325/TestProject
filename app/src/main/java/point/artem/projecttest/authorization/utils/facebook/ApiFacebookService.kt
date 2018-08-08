package point.artem.projecttest.authorization.utils.facebook

import android.content.Intent
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
    private var callbackManager: CallbackManager? = null
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

    override fun login() {
        callbackManager=CallbackManager.Factory.create()
        setCallback(callbackManager)
    }

    override fun getUser(){
        if (userModel==null) userModel = UserModel("Facebook",Profile.getCurrentProfile().firstName,Profile.getCurrentProfile().lastName,Profile.getCurrentProfile().getProfilePictureUri(100,100).toString())
        present.addUser(userModel!!)
    }


    fun setCallback(callbackManager:CallbackManager?){
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
    override fun result(requestCode: Int, responseCode: Int, data: Intent?) {
        callbackManager!!.onActivityResult(requestCode, responseCode, data)//FACEBOOK
    }
}