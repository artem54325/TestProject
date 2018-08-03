package point.artem.projectpoint.githubSearch

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_github_search.*
import point.artem.projectpoint.githubSearch.adapter.CostumerGithubAdapter
import point.artem.projectpoint.githubSearch.model.GithubUser
import point.artem.projectpoint.githubSearch.model.ListGithubUser
import point.artem.projectpoint.githubSearch.present.ISearchPresent
import point.artem.projectpoint.githubSearch.present.SearchPresenter
import point.artem.projectpoint.githubSearch.utils.ApiSearchManager
import point.artem.projectpoint.githubSearch.utils.GithubSearchService
import point.artem.projectpoint.githubSearch.view.ISearchView
import point.artem.projecttest.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit


class GithubSearchActivity : AppCompatActivity(), ISearchView {
    var presenter:ISearchPresent?=null

    @SuppressLint("ShowToast")
    override fun loadList(list: List<GithubUser>, page:Int) {
        Toast.makeText(this, "Страница: "+(page+1), Toast.LENGTH_SHORT).show()
        list_view.adapter=CostumerGithubAdapter(list, this.applicationContext)
        prev.visibility=View.VISIBLE

        if (page==0)prev.visibility=View.INVISIBLE
        else prev.visibility=View.VISIBLE
    }

    override fun refreshList(list: List<GithubUser>) {
        list_view.adapter=CostumerGithubAdapter(list, this.applicationContext)
        prev.visibility=View.INVISIBLE
    }

    override fun getContext(): Context = this.getContext()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_search)


        presenter = SearchPresenter(this)
        prev.visibility=View.INVISIBLE

        prev.setOnClickListener({
            (presenter as SearchPresenter).prevPage()
        })
        next.setOnClickListener({
            (presenter as SearchPresenter).nextPage()
        })


        /*  Билблиотека- compile 'com.jakewharton.rxbinding:rxbinding:0.4.0'
        При добавлении библиотеки с RxTextView пишет ошибку:
        Error:Execution failed for task ':app:transformResourcesWithMergeJavaResForDebug'.
        > More than one file was found with OS independent path 'META-INF/rxjava.properties'

        А после пишет Error:Execution failed for task ':app:transformResourcesWithMergeJavaResForDebug'.
        > java.io.IOException: Could not delete path '...pointTest\app\build\intermediates\transforms\mergeJavaRes\debug\0.jar'.


        Гугл не сказал, что делать(
        */
/*        RxTextView.textChanges(edit_search)
                .debounce(600, TimeUnit.MILLISECONDS )
                .map({
                    it.toString()
                })
                .subscribe( {
                    (presenter as SearchPresenter).searchWord(it)
                });*/

        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true);

        edit_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p: Editable?) {
                (presenter as SearchPresenter).searchWord(p.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        presenter!!.close()
        super.onDestroy()
    }

    @SuppressLint("ShowToast")
    override fun showToastError(error: String?) {
        Log.i("tag", "$error")
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }
}
