package point.artem.projectpoint.githubSearch.present

import point.artem.projectpoint.githubSearch.model.GithubUser
import point.artem.projectpoint.githubSearch.utils.ApiSearchManager
import point.artem.projectpoint.githubSearch.view.ISearchView
import java.util.concurrent.TimeUnit


class SearchPresenter(
        val searchView: ISearchView
) :ISearchPresent{
    var page : Int = 0
    private lateinit var listAll: List<GithubUser>
    private var api:ApiSearchManager.ApiManager = ApiSearchManager.ApiManager(this)


    override fun prevPage() {
        if (!::listAll.isInitialized)return
        if (page!=0){
            --page
            searchView.loadList(parsList(page), page)
        }else{
            searchView.showToastError("Вернуть не получается")
        }
    }

    override fun nextPage() {
        if (!::listAll.isInitialized)return
        if (page*30<listAll.size){
            ++page
            searchView.loadList(parsList(page), page)
        }else{
            searchView.showToastError("Кол-во страниц закончилось")
        }
    }


    override fun searchWord(word: String) {
        page = 0
        api.searchWord(word)
    }

    override fun errorNetwork(error: String) {
        searchView.showToastError(error)
    }

    override fun showList(list: List<GithubUser>) {
        if (list==null) searchView.showToastError("Ошибка сети")

        this.listAll=list
        searchView.refreshList(parsList(page))
    }

    override fun close() {
        //Закрыть всё что надо
    }

    private fun parsList(page:Int): ArrayList<GithubUser> {
        var list: ArrayList<GithubUser> = ArrayList<GithubUser>();
        for (n in page*30..(page+1)*30){
            if (listAll.size<=n)return list
            list.add(listAll.get(n))
        }
        return list
    }
}