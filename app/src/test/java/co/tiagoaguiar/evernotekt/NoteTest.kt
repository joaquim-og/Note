package co.tiagoaguiar.evernotekt

import co.tiagoaguiar.evernotekt.model.Note
import org.junit.Assert
import org.junit.Test

class NoteTest {

    @Test
    fun test_sould_format_date_pattern_to_Month_and_year() {
        val note = Note(title = "NotaA", body = "NotaA conteúdo", date = "20/02/2019")

        Assert.assertEquals("Fev 2019", note.createdDate)
    }

    @Test
    fun test_sould_format_date_pattern_case_empty() {
        val note = Note(title = "NotaA", body = "NotaA conteúdo", date = "")

        Assert.assertEquals("", note.createdDate)
    }

    @Test
    fun test_sould_format_date_pattern_case_null() {
        val note = Note(title = "NotaA", body = "NotaA conteúdo")

        Assert.assertEquals("", note.createdDate)
    }

}