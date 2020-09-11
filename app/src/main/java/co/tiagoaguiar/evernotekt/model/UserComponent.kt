package co.tiagoaguiar.evernotekt.model

import dagger.Component

@Component
interface UserComponent {

    fun getUser(): User

}