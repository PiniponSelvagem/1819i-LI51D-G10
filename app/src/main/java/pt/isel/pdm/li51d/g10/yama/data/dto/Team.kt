package pt.isel.pdm.li51d.g10.yama.data.dto

import java.io.Serializable

@Deprecated(
        level = DeprecationLevel.ERROR,
        message = "Deprecated, moving to room. Use 'pt.isel.pdm.li51d.g10.yama.data.database.team.Team' instead.",
        replaceWith = ReplaceWith(expression = "User(...)",
                imports = arrayOf("pt.isel.pdm.li51d.g10.yama.data.database.team.Team"))
)
data class Team(
        val name:   String,
        val id:     Int
) : Serializable