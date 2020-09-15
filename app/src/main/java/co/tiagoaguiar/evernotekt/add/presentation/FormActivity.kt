package co.tiagoaguiar.evernotekt.add.presentation

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import co.tiagoaguiar.evernotekt.R
import co.tiagoaguiar.evernotekt.add.add
import co.tiagoaguiar.evernotekt.model.Note
import co.tiagoaguiar.evernotekt.model.RemoteDataSource
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.content_form.*

/**
 *
 * Setembro, 24 2019
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
class FormActivity : AppCompatActivity(), TextWatcher, add.View {

    private lateinit var addPresenter: AddPresenter
    private var toSave: Boolean = false
    private var noteId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        noteId = intent.extras?.getInt("noteId")

        setupPresenter()
        setupViews()
    }

    private fun setupPresenter() {
        val dataSource = RemoteDataSource()
        addPresenter = AddPresenter(this, dataSource)
    }

    override fun onStart() {
        super.onStart()
        noteId?.let {
            addPresenter.getNote(it)
        }
    }

    override fun onStop() {
        super.onStop()
        addPresenter.stop()
    }

    private fun setupViews() {
        setSupportActionBar(toolbar)
        toggleToolbar(R.drawable.ic_arrow_back_black_24dp)

        note_title.addTextChangedListener(this)
        note_editor.addTextChangedListener(this)
    }

    private fun toggleToolbar(@DrawableRes icon: Int) {
        supportActionBar?.let {
            it.title = null
            val upArrow = ContextCompat.getDrawable(this, icon)
            val colorFilter =
                PorterDuffColorFilter(
                    ContextCompat.getColor(this, R.color.colorAccent),
                    PorterDuff.Mode.SRC_ATOP
                )
            upArrow?.colorFilter = colorFilter
            it.setHomeAsUpIndicator(upArrow)
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

//
//    private val callback: Callback<Note>
//        get() = object : Callback<Note> {
//
//            override fun onFailure(call: retrofit2.Call<Note>, t: Throwable) {
//                t.printStackTrace()
//                displayError("Erro ao carregar nota")
//            }
//
//            override fun onResponse(
//                call: retrofit2.Call<Note>,
//                response: Response<Note>
//            ) {
//                if (response.isSuccessful) {
//                    val note = response.body()
//                    displayNote(note.title, note.body)
//                }
//            }
//
//        }

    override fun displayError(message: String) {
        showToast(message)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun displayNote(title: String, body: String) {
        note_title.setText(title)
        note_editor.setText(body)
    }

    override fun returnToHome() {
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            return if (toSave && noteId == null) {
                val note = Note()
                val title = note_title.text.toString()
                val body = note_editor.text.toString()

                addPresenter.createNote(title, body)

                true
            } else {
                returnToHome()
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
    }

    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        toSave =
            if (note_editor.text.toString().isEmpty() && note_title.text.toString().isEmpty()) {
                toggleToolbar(R.drawable.ic_arrow_back_black_24dp)
                false
            } else {
                toggleToolbar(R.drawable.ic_done_black_24dp)
                true
            }
    }

    override fun afterTextChanged(editable: Editable) {
    }

}