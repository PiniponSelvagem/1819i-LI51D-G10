package pt.isel.pdm.li51d.g10.yama.data.dto

import android.graphics.Bitmap
import java.io.Serializable
import kotlin.ReplaceWith
import kotlin.DeprecationLevel


@Deprecated(
        level = DeprecationLevel.ERROR,
        message = "Deprecated, moving to room. Use 'pt.isel.pdm.li51d.g10.yama.data.database.user.User' instead.",
        replaceWith = ReplaceWith(expression = "User(...)",
        imports = arrayOf("pt.isel.pdm.li51d.g10.yama.data.database.user.User"))
)
data class User(
        val token:      String,
        val orgID:      String,

        val nickname:   String,
        val name:       String,
        val id:         Int,
        val avatarUrl:  String,
        val followers:  Int,
        val following:  Int,
        val email:      String,
        val location:   String,
        val blog:       String,
        val bio:        String,

        var avatar:     Bitmap?=null
) : Serializable