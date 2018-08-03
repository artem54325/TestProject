package point.artem.projectpoint.githubSearch.utils

import io.reactivex.Observable
import point.artem.projectpoint.githubSearch.model.ListGithubUser
import retrofit2.http.GET
import retrofit2.http.Path


interface GithubSearchService {
    @GET("/legacy/user/search/{word_search}")fun getSearch(@Path("word_search")wordSearch:String): Observable<ListGithubUser>
}