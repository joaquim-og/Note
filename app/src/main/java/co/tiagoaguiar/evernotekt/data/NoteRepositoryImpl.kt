package co.tiagoaguiar.evernotekt.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.tiagoaguiar.evernotekt.data.model.Note
import co.tiagoaguiar.evernotekt.data.model.RemoteDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class NoteRepositoryImpl : NoteRepository {

    private val remoteDataSource = RemoteDataSource()
    private val compositeDisposable = CompositeDisposable()

    override fun getAllNotes(): LiveData<List<Note>?> {
        val data = MutableLiveData<List<Note>?>()

        val disposable = remoteDataSource.listNotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<List<Note>>() {
                override fun onNext(notes: List<Note>) {
                    data.postValue(notes)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    data.postValue(null)
                }

                override fun onComplete() {
                    println("complete")
                }
            })

        compositeDisposable.add(disposable)

        return data
    }

    override fun getNote(noteId: Int): LiveData<Note>? {
        TODO("Not yet implemented")
    }

    override fun createNote(note: Note): LiveData<Note> {
        TODO("Not yet implemented")
    }

}