package io.caster.simplemvp

import dagger.Component
import io.caster.simplemvp.view.fragment.UserFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(target: UserFragment)
}
