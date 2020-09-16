package co.tiagoaguiar.evernotekt.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import co.tiagoaguiar.evernotekt.data.NoteRepository
import co.tiagoaguiar.evernotekt.data.NoteRepositoryImpl
import co.tiagoaguiar.evernotekt.data.model.Note

class HomeViewModel(private val repository: NoteRepository = NoteRepositoryImpl()): ViewModel() {

    fun getAllNotes() : LiveData<List<Note>?> {
        return repository.getAllNotes()
    }

}