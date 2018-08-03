package point.artem.projectpoint.githubSearch.present

import point.artem.projectpoint.githubSearch.model.GithubUser


interface ISearchPresent {
    fun searchWord(word:String)
    fun showList(list:List<GithubUser>)
    fun errorNetwork(error:String)
    fun prevPage()
    fun nextPage()
    fun close()
}