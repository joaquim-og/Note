package co.tiagoaguiar.evernotekt

import co.tiagoaguiar.evernotekt.home.Home
import co.tiagoaguiar.evernotekt.home.presentation.HomePresenter
import co.tiagoaguiar.evernotekt.data.model.Note
import co.tiagoaguiar.evernotekt.data.model.RemoteDataSource
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomePresenterTests {

    @Rule
    @JvmField
    var testSchedulerRule = RxSheduleRule()

    @Mock
    private lateinit var mockView: Home.View

    @Mock
    private lateinit var mockDataSource: RemoteDataSource

    lateinit var homePresenter: HomePresenter

    private val fakeAllNotes: List<Note>
        get() = arrayListOf(
            Note(1, "Nota A", "Desc nota A", "01/01/2001", "Texto nota A"),
            Note(2, "Nota B", "Desc nota B", "01/11/2001", "Texto nota B"),
            Note(3, "Nota V", "Desc nota V", "25/01/2001", "Texto nota v")
        )

    @Before
    fun setup() {
        homePresenter = HomePresenter(mockView, mockDataSource)
    }

    @Test
    fun `test must get all notes` () {
        // Given
        Mockito.doReturn(Observable.just(fakeAllNotes)).`when`(mockDataSource).listNotes()

        // When
        homePresenter.getAllNotes()

        // Then
        Mockito.verify(mockDataSource).listNotes()
        Mockito.verify(mockView).displayNotes(fakeAllNotes)
    }

    @Test
    fun `test must show empty notes` () {
        // Given
        Mockito.doReturn(Observable.just(arrayListOf<Note>())).`when`(mockDataSource).listNotes()

        // When
        homePresenter.getAllNotes()

        // Then
        Mockito.verify(mockDataSource).listNotes()
        Mockito.verify(mockView).displayEmptyNotes()
    }

}