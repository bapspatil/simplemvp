package io.caster.simplemvp

import io.caster.simplemvp.model.User
import io.caster.simplemvp.presentation.UserPresenter
import io.caster.simplemvp.presentation.UserPresenterImpl
import io.caster.simplemvp.repository.UserRepository
import io.caster.simplemvp.view.UserView
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*


class PresenterTests {

    lateinit var mockUserRepository: UserRepository
    lateinit var mockView: UserView
    lateinit var presenter: UserPresenter
    lateinit var user: User

    @Before
    fun setup() {
        mockUserRepository = mock(UserRepository::class.java)

        user = User()
        user.id = 1
        user.firstName = "Mighty"
        user.lastName = "Mouse"
        `when`(mockUserRepository.getUser(ArgumentMatchers.anyInt())).thenReturn(user)

        mockView = mock(UserView::class.java)

        presenter = UserPresenterImpl(mockUserRepository)
    }

    @Test
    fun noInteractionsWithViewShouldTakePlaceIfUserIsNull() {
        presenter.saveUser()

        // user object is not initialized, lets verify no interactions take place
        verifyZeroInteractions(mockView)
    }

    @Test
    fun shouldBeABleToLoadTheUserFromTheRepositoryWhenValidUserIsPresent() {
        `when`(mockView.userId).thenReturn(1)

        presenter.setView(mockView)

        // Verify repository interactions
        verify(mockUserRepository, times(1)).getUser(ArgumentMatchers.anyInt())

        // Verify view interactions
        verify(mockView, times(1)).userId
        verify(mockView, times(1)).displayFirstName("Mighty")
        verify(mockView, times(1)).displayLastName("Mouse")
        verify(mockView, never()).showUserNotFoundMessage()
    }

    @Test
    fun shouldShowErrorMessageOnViewWhenUserIsNotPresent() {
        `when`(mockView.userId).thenReturn(1)

        // Return null when we ask the repo for a user.
        `when`(mockUserRepository.getUser(ArgumentMatchers.anyInt())).thenReturn(null)

        presenter.setView(mockView)

        // Verify repository interactions
        verify(mockUserRepository, times(1)).getUser(ArgumentMatchers.anyInt())

        // verify view interactions
        verify(mockView, times(1)).userId
        verify(mockView, times(1)).showUserNotFoundMessage()
        verify(mockView, never()).displayFirstName(ArgumentMatchers.anyString())
        verify(mockView, never()).displayLastName(ArgumentMatchers.anyString())
    }

    @Test
    fun shouldShouldErrorMessageDuringSaveIfFirstOrLastNameIsMissing() {
        `when`(mockView.userId).thenReturn(1)

        // Load the user
        presenter.setView(mockView)

        verify(mockView, times(1)).userId

        // Set up the view mock
        `when`(mockView.firstName).thenReturn("") // empty string

        presenter.saveUser()

        verify(mockView, times(1)).firstName
        verify(mockView, never()).lastName
        verify(mockView, times(1)).showUserNameIsRequired()

        // Now tell mockView to return a value for first name and an empty last name
        `when`(mockView.firstName).thenReturn("Foo")
        `when`(mockView.lastName).thenReturn("")

        presenter.saveUser()

        verify(mockView, times(2)).firstName // Called two times now, once before, and once now
        verify(mockView, times(1)).lastName  // Only called once
        verify(mockView, times(2)).showUserNameIsRequired() // Called two times now, once before and once now
    }

    @Test
    fun shouldBeAbleToSaveAValidUser() {
        `when`(mockView.userId).thenReturn(1)

        // Load the user
        presenter.setView(mockView)

        verify(mockView, times(1)).userId

        `when`(mockView.firstName).thenReturn("Foo")
        `when`(mockView.lastName).thenReturn("Bar")

        presenter.saveUser()

        // Called two more times in the saveUser call.
        verify(mockView, times(2)).firstName
        verify(mockView, times(2)).lastName

        assertThat<String>(user.firstName, `is`("Foo"))
        assertThat<String>(user.lastName, `is`("Bar"))

        // Make sure the repository saved the user
        verify(mockUserRepository, times(1)).save(user)

        // Make sure that the view showed the user saved message
        verify(mockView, times(1)).showUserSavedMessage()
    }

    @Test
    fun shouldLoadUserDetailsWhenTheViewIsSet() {
        presenter.setView(mockView)
        verify(mockUserRepository, times(1)).getUser(ArgumentMatchers.anyInt())
        verify(mockView, times(1)).displayFirstName(ArgumentMatchers.anyString())
        verify(mockView, times(1)).displayLastName(ArgumentMatchers.anyString())
    }

    @Test(expected = NullPointerException::class)
    fun shouldThrowNullPointerExceptionWhenViewIsNull() {
        // Null out the view
        presenter.setView(null!!)

        // Try to load the screen which will force interactions with the view
        presenter.loadUserDetails()
    }
}
