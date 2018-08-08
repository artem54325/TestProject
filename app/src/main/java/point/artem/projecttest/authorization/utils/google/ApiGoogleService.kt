package point.artem.projecttest.authorization.utils.google

import android.content.Intent
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import point.artem.projecttest.authorization.AuthorizationActivity
import point.artem.projecttest.authorization.model.UserModel
import point.artem.projecttest.authorization.present.IAuthorizationPresent
import point.artem.projecttest.authorization.utils.IAuthorizationService


class ApiGoogleService(val present: IAuthorizationPresent) :IAuthorizationService, GoogleApiClient.OnConnectionFailedListener{


    private var userModel:UserModel? = null
    var googleApiClient: GoogleApiClient

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        googleApiClient = GoogleApiClient.Builder(present.getActivity())
                .enableAutoManage(present.getActivity() as FragmentActivity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    override fun getUser(){
        present.addUser(userModel!!)
    }

    override fun login() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)//googleApiClient
        present.getActivity().startActivityForResult(signInIntent, AuthorizationActivity.RC_SIGN_IN_GOOGLE)
    }


    private fun handleSignInResult(result: GoogleSignInResult){
        Log.i("Google", "handleSignInResult:${result.status} " + result.isSuccess());
        if (result.isSuccess) {
            val acct: GoogleSignInAccount = result.signInAccount!!

            userModel = UserModel("Google",acct.familyName, acct.displayName, acct.photoUrl.toString())
            present.updateUI("Google",true)
            present.complete()
            present.addUser(userModel!!)
        } else {//Не вошел
            present.updateUI("Google",false)
            Log.i("Google", "не зашел")
            error("Google не зашел")
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.i("google", p0.errorMessage)
        present.error("Ошибка сети:"+p0.errorMessage)
    }
    override fun logout() {
        Auth.GoogleSignInApi.signOut(googleApiClient)
        present.updateUI("Google", false)
    }
    override fun result(requestCode: Int, responseCode: Int, data: Intent?) {
        if (requestCode == AuthorizationActivity.RC_SIGN_IN_GOOGLE) handleSignInResult(Auth.GoogleSignInApi.getSignInResultFromIntent(data))
    }
}