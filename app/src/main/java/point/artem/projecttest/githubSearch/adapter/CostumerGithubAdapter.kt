package point.artem.projectpoint.githubSearch.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import point.artem.projectpoint.githubSearch.model.GithubUser
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.item_github_search.view.*
import point.artem.projecttest.R


class CostumerGithubAdapter(val models:List<GithubUser>, val context:Context) : BaseAdapter() {
    override fun getItem(position: Int): Any = models.get(position)

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = models.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v = LayoutInflater.from(context)
                .inflate(R.layout.item_github_search, null)

        v.login.setText(models.get(position).login)
        v.username.setText(models.get(position).username)
        v.fullname.setText(models.get(position).fullname)
        Log.e("model", "${models.get(position).toString()}")
        v.public_pepo_count.setText(models.get(position).publicRepository.toString())

        return v;
    }
}