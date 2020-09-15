package co.tiagoaguiar.evernotekt

import co.tiagoaguiar.evernotekt.add.add
import co.tiagoaguiar.evernotekt.add.presentation.AddPresenter
import co.tiagoaguiar.evernotekt.home.Home
import co.tiagoaguiar.evernotekt.home.presentation.HomePresenter
import co.tiagoaguiar.evernotekt.model.Note
import co.tiagoaguiar.evernotekt.model.RemoteDataSource
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import io.reactivex.Observable
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddPresenterTests : BaseTest() {

    @Rule
    @JvmField
    var testSchedulerRule = RxSheduleRule()

    @Mock
    private lateinit var mockView: add.View

    @Mock
    private lateinit var mockDataSource: RemoteDataSource

    lateinit var addPresenter: AddPresenter

    @Captor
    private lateinit var noteArgCaptor: ArgumentCaptor<Note>

    @Before
    fun setup() {
        addPresenter = AddPresenter(mockView, mockDataSource)
    }

    @Test
    fun `test must not add empty body note` () {

        // When
        addPresenter.createNote("","")

        // Then
        Mockito.verify(mockView).displayError("título e corpo não informado")
    }

    @Test
    fun `test must add new note` () {
        //Given
        val note = Note(title = "a", body = "texto a")
        Mockito.doReturn(Observable.just(note)).`when`(mockDataSource).createNote(captureArg(noteArgCaptor))

        // When
        addPresenter.createNote("a", "texto a")

        // Then
        Mockito.verify(mockDataSource).createNote(captureArg(noteArgCaptor))

        Assert.assertThat(noteArgCaptor.value.title, CoreMatchers.equalTo("a"))
        Assert.assertThat(noteArgCaptor.value.body, CoreMatchers.equalTo("texto a"))

        Mockito.verify(mockView).returnToHome()
    }

    @Test
    fun `test must show error when creation failure` () {
        //Given
         Mockito.doReturn(Observable.error<Throwable>(Throwable("server error"))).`when`(mockDataSource).createNote(
             anyOrNull()
         )

        // When
        addPresenter.createNote("a", "texto a")

        // Then
        Mockito.verify(mockDataSource).createNote(captureArg(noteArgCaptor))

        Assert.assertThat(noteArgCaptor.value.title, CoreMatchers.equalTo("a"))
        Assert.assertThat(noteArgCaptor.value.body, CoreMatchers.equalTo("texto a"))

        Mockito.verify(mockView).displayError("Erro ao carregar notas")
    }

}