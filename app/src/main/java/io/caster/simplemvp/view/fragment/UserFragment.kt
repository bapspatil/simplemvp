package io.caster.simplemvp.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.caster.simplemvp.MvpApplication
import io.caster.simplemvp.R
import io.caster.simplemvp.presentation.UserPresenter
import io.caster.simplemvp.view.UserView
import kotlinx.android.synthetic.main.fragment_main.*
import org.jetbrains.anko.support.v4.toast
import javax.inject.Inject

/**
 * The V in MVP (Model View Presenter)
 */
class UserFragment : Fragment(), UserView {
    @Inject lateinit var userPresenter: UserPresenter

    override val userId: Int
        get() = if (arguments == null) 0 else arguments!!.getInt(USER_ID, 0)

    override val firstName: String
        get() = userFirstName.text.toString()

    override val lastName: String
        get() = userLastName.text.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MvpApplication).component!!.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_main, container, false)
        userSave.setOnClickListener { userPresenter.saveUser() }
        return v
    }

    override fun onResume() {
        super.onResume()
        userPresenter.setView(this)
    }

    override fun displayFirstName(name: String) {
        userFirstName.setText(name)
    }

    override fun displayLastName(name: String) {
        userLastName.setText(name)
    }

    override fun showUserNotFoundMessage() {
        toast(R.string.user_not_found)
    }

    override fun showUserSavedMessage() {
        toast(R.string.user_saved)
    }

    override fun showUserNameIsRequired() {
        toast(R.string.user_name_required_message)
    }

    companion object {
        const val USER_ID = "user_id"
    }
}
