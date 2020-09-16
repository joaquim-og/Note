package co.tiagoaguiar.evernotekt.home

import co.tiagoaguiar.evernotekt.data.model.Note

interface Home {

    interface Presenter {

        fun getAllNotes()

        fun stop()

    }

    interface View {

        fun displayNotes(notes: List<Note>)
        fun displayEmptyNotes()
        fun displayError(message: String)

    }

}