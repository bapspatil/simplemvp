package io.caster.simplemvp.repository

import io.caster.simplemvp.model.User

class InMemoryUserRepositoryImpl : UserRepository {
    private var u: User? = null

    /**
     * Gets the user from memory.
     *
     * @param id The id of the user.
     * @return a [User]
     */
    override fun getUser(id: Int): User {
        // Normally this would go to a DB/etc and fetch the user with an ID.
        // Here though, we're just doing something in memory, so we're kind of just
        // faking it out.
        if (u == null) {
            u = User()
            u!!.id = id
            u!!.firstName = "Captain"
            u!!.lastName = "Crunch"
        }
        return u!!
    }

    /**
     * Save's the in-memory user.
     *
     * @param u The user.
     */
    override fun save(u: User) {
        if (this.u == null) {
            this.u = getUser(0) // create the in memory user.
        }
        this.u!!.id = u.id
        this.u!!.firstName = u.firstName
        this.u!!.lastName = u.lastName
    }
}
