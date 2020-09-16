package co.tiagoaguiar.evernotekt.home.presentation

import co.tiagoaguiar.evernotekt.home.Home
import  io.reactivex.Observable
import co.tiagoaguiar.evernotekt.data.model.Note
import co.tiagoaguiar.evernotekt.data.model.RemoteDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class HomePresenter(
    private val view: Home.View,
    private val dataSource: RemoteDataSource
) : Home.Presenter {

    companion object {
        private const val TAG = "HomePresenter"
    }

    private val compositeDisposable = CompositeDisposable()
    private val notesObserver: DisposableObserver<List<Note>>
        get() = object : DisposableObserver<List<Note>>() {
            override fun onNext(notes: List<Note>) {

                if (notes.isNotEmpty()) {
                    view.displayNotes(notes)
                } else {
                    view.displayEmptyNotes()
                }

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                view.displayError("Erro ao carregar notas")
            }

            override fun onComplete() {
                println("complete")
            }
        }

    private val notesObservable: Observable<List<Note>>
    get() = dataSource.listNotes()

    override fun getAllNotes() {

    }

    override fun stop() {
        compositeDisposable.clear()
    }

}