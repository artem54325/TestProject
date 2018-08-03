package point.artem.projectpoint.githubSearch.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*


data class GithubUser(
        @SerializedName("id") @Expose val id: String,
        @SerializedName("login") @Expose val login: String,
        @SerializedName("name")@Expose val name: String,
        @SerializedName("username")@Expose val username: String,
        @SerializedName("fullname")@Expose val fullname: String,
        @SerializedName("location")@Expose val location: String?,
        @SerializedName("language")@Expose val language: String,
        @SerializedName("type")@Expose val type: String,
        @SerializedName("public_repo_count")@Expose val publicRepository: Int,
        @SerializedName("repos")@Expose val repos: Int?,
        @SerializedName("followers")@Expose val followers: Int,
        @SerializedName("followers_count")@Expose val followersCount: Int,
        @SerializedName("createAt")@Expose val createAt: String,
        @SerializedName("create")@Expose val create: String
)

data class ListGithubUser(@SerializedName("users") @Expose val list: List<GithubUser>)