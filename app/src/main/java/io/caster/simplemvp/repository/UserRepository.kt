package io.caster.simplemvp.repository

import io.caster.simplemvp.model.User

interface UserRepository {
    fun getUser(id: Int): User

    fun save(u: User)
}
