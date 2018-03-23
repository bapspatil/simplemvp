package io.caster.simplemvp

import dagger.Module
import dagger.Provides
import io.caster.simplemvp.presentation.UserPresenter
import io.caster.simplemvp.presentation.UserPresenterImpl
import io.caster.simplemvp.repository.InMemoryUserRepositoryImpl
import io.caster.simplemvp.repository.UserRepository
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository = InMemoryUserRepositoryImpl()

    @Provides
    fun provideUserPresenter(userRepository: UserRepository): UserPresenter = UserPresenterImpl(userRepository)
}
