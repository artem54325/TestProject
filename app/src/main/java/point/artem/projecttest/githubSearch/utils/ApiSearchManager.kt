package point.artem.projectpoint.githubSearch.utils


import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import point.artem.projectpoint.githubSearch.present.ISearchPresent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiSearchManager {
    private const val SERVER : String = "https://api.github.com"

    fun create():GithubSearchService{
        val reretroft = retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SERVER)
                .build()
        return reretroft.create(GithubSearchService::class.java);
    }
    class ApiManager(var searchPresenter: ISearchPresent){
        fun searchWord(wordSearch:String){
            ApiSearchManager.create().getSearch(wordSearch).subscribeOn(Schedulers.io())
                    .debounce(600, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        searchPresenter.showList(it.list)
                    },{
                        it.printStackTrace()
                        searchPresenter.errorNetwork(it.toString())
                    })
        }
    }

}