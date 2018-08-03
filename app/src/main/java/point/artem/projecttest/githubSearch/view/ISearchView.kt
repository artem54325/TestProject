package point.artem.projectpoint.githubSearch.view

import android.content.Context
import point.artem.projectpoint.githubSearch.model.GithubUser


interface ISearchView {
    fun getContext():Context//Получить контекст)
    fun loadList(list:List<GithubUser>, page:Int)// Если листать вниз по списку догружает пользователей
    fun refreshList(list:List<GithubUser>)//Показать лист с новыми данными, Если пользователь обновил данные
    fun showToastError(error:String?)//Показать ошибку. Можно конечно сделать throwable
}