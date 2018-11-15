package pt.isel.pdm.i41n.g6.yama.data

import android.graphics.Bitmap
import java.io.Serializable

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