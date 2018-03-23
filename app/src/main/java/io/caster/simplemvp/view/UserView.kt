package io.caster.simplemvp.view

interface UserView {
    val userId: Int

    val firstName: String

    val lastName: String

    fun displayFirstName(name: String)

    fun displayLastName(name: String)

    fun showUserNotFoundMessage()

    fun showUserSavedMessage()

    fun showUserNameIsRequired()
}
