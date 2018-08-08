package point.artem.projecttest.authorization

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import point.artem.projecttest.R
import point.artem.projecttest.authorization.present.AuthorizationPresent
import point.artem.projecttest.authorization.view.IAuthorizationView
import android.content.Intent
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import com.facebook.*
import com.vk.sdk.VKSdk
import android.view.View
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.ConnectionResult
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.item_servis.view.*
import kotlinx.android.synthetic.main.nav_header_main.*
import point.artem.projectpoint.githubSearch.GithubSearchActivity
import point.artem.projecttest.authorization.model.UserModel
import point.artem.projecttest.authorization.utils.load


class AuthorizationActivity :AppCompatActivity(), IAuthorizationView, GoogleApiClient.OnConnectionFailedListener {
    lateinit var present: AuthorizationPresent
    companion object {
        val RC_SIGN_IN_GOOGLE = 911
    }

    /*
    * Я очень надеюсь, что код оправдает ваши желаение.
    *
    * Под левым меню, я надеюсь правильно понял что имеете ввиду NavigationDrawer, а не просто menu только слева.
    *
    * Приложение разложил на 2 активити с GithubSearch и Authorization
    *
    * Оба активити сделаны по паттерну MVP, чтобы уменьшить связь между элементами для удобного тестирования
    * */

    override fun error(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    override fun complete() {
        val intent:Intent = Intent(this ,GithubSearchActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        present = AuthorizationPresent(this)

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        login_button_facebook.setReadPermissions("public_profile");//facebook
        login_button_facebook.setOnClickListener{
            present.login("Facebook")
        }

        //GOOGLE
        sign_in_button.setOnClickListener {
            present.login("Google")
        }
        btn_sign_out.setOnClickListener{//Выйти из аккаунта
            present.logout("Google")
        }

        //VK
        vk_in_button.setOnClickListener{
            present.login("VK")
        }
        vk_on_button.setOnClickListener{
            present.logout("VK")
        }
    }

    override fun showMenu(model: UserModel) {
            var view :View? = null
            when(model.service){
                "Google" -> view = google_include
                "Facebook" -> view = facebook_include
                "VK" -> view = vk_include
            }
        if (model.first_name!=null) view!!.first_name_text.setText(model.first_name)else view!!.first_name_text.setText("")
        if (model.last_name!=null) view!!.last_name_text.setText(model.last_name) else view!!.last_name_text.setText("")
        if (model.urlImage!=null) view!!.circleView.load(model.urlImage) else view!!.circleView.load("")
    }

    override fun getActivity(): Activity =this

    override fun onActivityResult(requestCode: Int, responseCode: Int,
                                  data: Intent?) {
        Log.i("requestCode", "${requestCode}")

        present.activityResultPresent(requestCode, responseCode, data)

        super.onActivityResult(requestCode, responseCode, data)
    }

    override fun updateUI(mode:String,isSignedIn: Boolean) {
        when(mode){
            "Google"->  updateUIGoogle(isSignedIn)
            "VK"    ->  updateUIVK(isSignedIn)
                    //"facebook"-не нуждается в этом у него самого получается следить за авторизацией
        }
    }

    private fun updateUIGoogle(isSignedIn: Boolean) {
        if (isSignedIn) {
            sign_in_button.setVisibility(View.GONE)
            btn_sign_out.setVisibility(View.VISIBLE)
        } else {
            showMenu(UserModel("Google", null,null,null))
            sign_in_button.setVisibility(View.VISIBLE)
            btn_sign_out.setVisibility(View.GONE)
        }
    }

    private fun updateUIVK(isSignedIn: Boolean) {
        if (isSignedIn) {
            vk_in_button.setVisibility(View.GONE)
            vk_on_button.setVisibility(View.VISIBLE)
        } else {
            showMenu(UserModel("VK", null,null,null))
            vk_in_button.setVisibility(View.VISIBLE)
            vk_on_button.setVisibility(View.GONE)
        }
    }

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.i("Google", "onConnectionFailed:" + p0);
    }

    override fun onDestroy() {
        Log.i("menu","onDestroy")
        present.close()
        super.onDestroy()
    }
}
