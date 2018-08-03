package point.artem.projecttest.authorization.utils.vk

import com.vk.sdk.VKSdk
import com.facebook.appevents.internal.ActivityLifecycleTracker.startTracking
import android.content.Intent
import android.widget.Toast
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKAccessTokenTracker
import point.artem.projecttest.authorization.AuthorizationActivity


class Application: android.app.Application() {
    var vkAccessTokenTracker: VKAccessTokenTracker = object : VKAccessTokenTracker() {
        override fun onVKAccessTokenChanged(oldToken: VKAccessToken?, newToken: VKAccessToken?) {
            if (newToken == null) {
                Toast.makeText(this@Application, "AccessToken invalidated", Toast.LENGTH_LONG).show()
                val intent = Intent(this@Application, AuthorizationActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        vkAccessTokenTracker.startTracking()
        VKSdk.initialize(this)
    }

}