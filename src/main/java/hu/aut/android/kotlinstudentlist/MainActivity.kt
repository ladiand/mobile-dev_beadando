package hu.aut.android.kotlinstudentlist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import hu.aut.android.kotlinstudentlist.adapter.StudentAdapter
import kotlinx.android.synthetic.main.activity_main.*
import android.preference.PreferenceManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import hu.aut.android.kotlinpersonlist.R
import hu.aut.android.kotlinstudentlist.data.AppDatabase
import hu.aut.android.kotlinstudentlist.data.StudentItem
import hu.aut.android.kotlinstudentlist.touch.StudentTouchHelperCallback
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt




/*
Ez a fő, main.
Itt nem szükséges módosítani a Student Item-hez ha új adattagot adunk.
 */
class MainActivity : AppCompatActivity(), StudentItemDialog.PersonItemHandler {
    companion object {
        val KEY_FIRST = "KEY_FIRST"
        val KEY_ITEM_TO_EDIT = "KEY_ITEM_TO_EDIT"
    }

    private lateinit var adapter: StudentAdapter
    /*
    Alkalmazás create-kor hívódik meg
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Activity-main.xml-t hozza be
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        //Új elem hozzáadásakor hívódik meg, a rózsaszín levél ikonos gomb eseménykezelője
        //A ShoppingTimeDialog-ot hívja meg (jeleníti meg)
        fab.setOnClickListener { view ->
            StudentItemDialog().show(supportFragmentManager, "TAG_ITEM")
        }

        initRecyclerView()
            /*Új elem felvitelének vizsgálata, akkor hívódik meg, a dialógus címét állítja*/
        if (isFirstRun()) {
            MaterialTapTargetPrompt.Builder(this@MainActivity)
                    .setTarget(findViewById<View>(R.id.fab))
                    .setPrimaryText("New Person Item")
                    .setSecondaryText("Tap here to create new person item")
                    .show()
        }


        saveThatItWasStarted()
    }

    private fun isFirstRun(): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                KEY_FIRST, true
        )
    }

    private fun saveThatItWasStarted() {
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        sp.edit()
                .putBoolean(KEY_FIRST, false)
                .apply()
    }




    private fun initRecyclerView() {
        val dbThread = Thread {
            //Lekéri az összes Shopping Item-et.
            val items = AppDatabase.getInstance(this).personItemDao().findAllItems()

            runOnUiThread{
                adapter = StudentAdapter(this, items)
                recyclerPerson.adapter = adapter

                val callback = StudentTouchHelperCallback(adapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerPerson)
            }
        }
        dbThread.start()
    }
    /*Edit dialógus megnyitása*/
    fun showEditItemDialog(itemToEdit: StudentItem) {
        val editDialog = StudentItemDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM_TO_EDIT, itemToEdit)
        editDialog.arguments = bundle

        editDialog.show(supportFragmentManager, "TAG_ITEM_EDIT")
    }

/*Új Student Item-kor beszúrjuk a DB-be a DAO segítségével*/
    override fun personItemCreated(item: StudentItem) {
        val dbThread = Thread {
            val id = AppDatabase.getInstance(this@MainActivity).personItemDao().insertItem(item)

            item.studentId = id

            runOnUiThread{
                adapter.addItem(item)
            }
        }
        dbThread.start()
    }
/*Update-or módosítjuk a Shopping Item-et a DAO segítségével*/
    override fun personItemUpdated(item: StudentItem) {
        adapter.updateItem(item)

        val dbThread = Thread {
            AppDatabase.getInstance(this@MainActivity).personItemDao().updateItem(item)

            runOnUiThread { adapter.updateItem(item) }
        }
        dbThread.start()
    }
}
