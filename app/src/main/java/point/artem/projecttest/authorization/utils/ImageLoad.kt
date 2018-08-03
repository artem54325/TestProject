package point.artem.projecttest.authorization.utils

import android.text.TextUtils
import android.widget.ImageView
import com.squareup.picasso.Picasso


fun ImageView.load(imageUrl:String){
    if(!TextUtils.isEmpty(imageUrl)) Picasso.with(context).load(imageUrl).into(this)
    else setImageDrawable(null)
}