package co.tiagoaguiar.evernotekt.add.presentation

import co.tiagoaguiar.evernotekt.add.add
import co.tiagoaguiar.evernotekt.data.model.Note
import co.tiagoaguiar.evernotekt.data.model.RemoteDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class AddPresenter(
    private val view: add.View,
    private val dataSource: RemoteDataSource
): add.Presenter {

    private val compositeDisposable = CompositeDisposable()

    private val createNoteObserver: DisposableObserver<Note>
        get() = object : DisposableObserver<Note>() {
            override fun onNext(notes: Note) {
                view.returnToHome()
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                view.displayError("Erro ao carregar notas")
            }

            override fun onComplete() {
                println("complete")
            }
        }

    private val getNotesObserver: DisposableObserver<Note>
        get() = object : DisposableObserver<Note>() {
            override fun onNext(note: Note) {
               view.displayNote(note.title ?: "", note.body ?: "")
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                view.displayError("Erro ao carregar notas")
            }

            override fun onComplete() {
                println("complete")
            }
        }

    override fun createNote(title: String, body: String) {
        if (title.isEmpty() || body.isEmpty()) {
            view.displayError("título e corpo não informado")
            return
        }

        val note = Note(title = title, body = body)
        val disposable = dataSource.createNote(note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(createNoteObserver)

        compositeDisposable.add(disposable)
    }

    override fun getNote(id: Int) {

        val disposable = dataSource.getNote(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(getNotesObserver)

        compositeDisposable.add(disposable)
    }

    override fun stop() {
        compositeDisposable.clear()
    }
}