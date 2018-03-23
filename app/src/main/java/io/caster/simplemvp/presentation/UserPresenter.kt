package io.caster.simplemvp.presentation

import io.caster.simplemvp.view.UserView

interface UserPresenter {
    fun loadUserDetails()

    fun setView(view: UserView)

    fun saveUser()
}
