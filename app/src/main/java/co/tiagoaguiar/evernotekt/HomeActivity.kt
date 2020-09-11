package co.tiagoaguiar.evernotekt

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import co.tiagoaguiar.evernotekt.model.Note
import co.tiagoaguiar.evernotekt.model.RemoteDataSource
import com.google.android.material.navigation.NavigationView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.content_home.*
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val dataSource = RemoteDataSource()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupViews()
    }

    private fun setupViews() {
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        home_recycler_view.addItemDecoration(divider)
        home_recycler_view.layoutManager = LinearLayoutManager(this)

        fab.setOnClickListener {
            val intent = Intent(baseContext, FormActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        dataSource.listNotes(callback)

        val subscriber = createSubscribe()

        Observable.merge(createChannel(), createSpotify())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber)

    }

    fun createChannel(): Observable<String> {
        return Observable.create{emitter ->
            println(Thread.currentThread().name)
            emitter.onNext("Bem vindo ao canal")
            emitter.onComplete()
        }
    }

    fun createSpotify(): Observable<String> {
        return Observable.create{emitter ->
            println(Thread.currentThread().name)
            emitter.onNext("Tocando música XXX")
            emitter.onComplete()
        }
    }

    fun createSubscribe(): Observer<String> {
        return object: Observer<String> {
            override fun onSubscribe(d: Disposable) {
                println("inscrição completada")
            }

            override fun onNext(t: String) {
                println("novo valor é $t")
            }

            override fun onError(e: Throwable) {
                println("novo valor erro ${e.message}")
            }

            override fun onComplete() {
                println("novo valor emitido")
                println(Thread.currentThread().name)
            }


        }
    }

    private val callback: Callback<List<Note>>
        get() = object : Callback<List<Note>> {

            override fun onFailure(call: retrofit2.Call<List<Note>>, t: Throwable) {
                t.printStackTrace()
                displayError("Erro ao carregar notas")
            }

            override fun onResponse(
                call: retrofit2.Call<List<Note>>,
                response: Response<List<Note>>
            ) {
                if (response.isSuccessful) {
                    val notes = response.body()
                    notes?.let {
                        displayNotes(it)
                    }
                }
            }

        }

    fun displayError(message: String) {
        showToast(message)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun displayNotes(notes: List<Note>) {
        // progress
        if (notes.isNotEmpty()) {
            home_recycler_view.adapter = NoteAdapter(notes) { note ->
                val intent = Intent(baseContext, FormActivity::class.java)
                intent.putExtra("noteId", note.id)
                startActivity(intent)
            }
        } else {
            // no data
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.nav_all_notes) {
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

}