package pt.isel.pdm.li51d.g10.yama.data.dto

import java.io.Serializable

data class Message(
        val text:           String,
        val fromLoggedUser: Boolean
) : Serializable