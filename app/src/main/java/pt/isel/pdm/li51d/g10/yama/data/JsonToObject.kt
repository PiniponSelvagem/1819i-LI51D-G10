package pt.isel.pdm.li51d.g10.yama.data

import org.json.JSONObject
import pt.isel.pdm.li51d.g10.yama.data.database.user.User

fun convertJsonToUser(jObj: JSONObject) : User {
    return User(
            jObj.getInt("id"),
            jObj.getString("login"),
            jObj.getString("name"),
            jObj.getString("avatar_url"),
            jObj.getInt("followers"),
            jObj.getInt("following"),
            jObj.getString("email"),
            jObj.getString("location"),
            jObj.getString("blog"),
            jObj.getString("bio")
    )
}