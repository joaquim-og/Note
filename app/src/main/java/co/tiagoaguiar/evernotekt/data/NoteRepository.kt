package co.tiagoaguiar.evernotekt.data

import androidx.lifecycle.LiveData
import co.tiagoaguiar.evernotekt.data.model.Note
import co.tiagoaguiar.evernotekt.data.network.RetrofitClient
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface NoteRepository {

    fun getAllNotes(): LiveData<List<Note>?>

    fun getNote(noteId: Int): LiveData<Note>?

    fun createNote(note: Note): LiveData<Note>

}