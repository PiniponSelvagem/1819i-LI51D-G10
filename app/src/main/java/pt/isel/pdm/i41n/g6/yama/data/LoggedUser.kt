package pt.isel.pdm.i41n.g6.yama.data

import java.io.Serializable

data class LoggedUser(
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
        val bio:        String
) : Serializable