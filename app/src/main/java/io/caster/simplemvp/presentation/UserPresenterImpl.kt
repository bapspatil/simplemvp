package io.caster.simplemvp.presentation

import io.caster.simplemvp.model.User
import io.caster.simplemvp.repository.UserRepository
import io.caster.simplemvp.view.UserView

class UserPresenterImpl(val userRepository: UserRepository) : UserPresenter {

    private var view: UserView? = null
    private var u: User? = null

    override fun loadUserDetails() {
        val userId = view!!.userId
        u = userRepository.getUser(userId)
        if (u == null) {
            view!!.showUserNotFoundMessage()
        } else {
            view!!.displayFirstName(u!!.firstName!!)
            view!!.displayLastName(u!!.lastName!!)
        }
    }

    override fun setView(view: UserView) {
        this.view = view
        loadUserDetails()
    }

    override fun saveUser() {
        if (u != null) {
            if (view!!.firstName.trim { it <= ' ' } == "" || view!!.lastName.trim { it <= ' ' } == "") {
                view!!.showUserNameIsRequired()
            } else {
                u!!.firstName = view!!.firstName
                u!!.lastName = view!!.lastName
                userRepository.save(u!!)
                view!!.showUserSavedMessage()
            }

        }
    }
}
