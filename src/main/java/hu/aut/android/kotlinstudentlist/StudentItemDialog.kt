package hu.aut.android.kotlinstudentlist

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.EditText
import hu.aut.android.kotlinpersonlist.R
import hu.aut.android.kotlinstudentlist.data.StudentItem
import kotlinx.android.synthetic.main.dialog_create_item.view.*

/*
Ez a dialógus ablak szolgál az új Shipping Item felvitelére, és a meglevő Shopping Item módosítására
 */

class StudentItemDialog : DialogFragment() {

    private lateinit var personItemHandler: PersonItemHandler
    //Student item elemek text-ben, ide szükséges a bővítés a Student Item új adattagja esetén
    private lateinit var etName: EditText
    private lateinit var etAge: EditText
    private lateinit var etAddres: EditText

    interface PersonItemHandler {
        fun personItemCreated(item: StudentItem)

        fun personItemUpdated(item: StudentItem)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is PersonItemHandler) {
            personItemHandler = context
        } else {
            throw RuntimeException("The Activity does not implement the PersonItemHandler interface")
        }
    }
/*Új Shopping Item felvitelekor ez hívódik meg. A felirat a New Item lesz*/
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("New Item")

        initDialogContent(builder)

        builder.setPositiveButton("OK") { dialog, which ->
            // keep it empty
        }
        return builder.create()
    }

    private fun initDialogContent(builder: AlertDialog.Builder) {
        /*etItem = EditText(activity)
        builder.setView(etItem)*/

        //dialog_create_item.xml-el dolgozunk
        val rootView = requireActivity().layoutInflater.inflate(R.layout.dialog_create_item, null)
        //Shopping Item tagok az xml-ből (EditText elemek)
        //Itt is szükséges a bővítés új Shopping Item adattag esetén
        etName = rootView.etName
        etAge = rootView.etAge
        etAddres=rootView.etAddress
        builder.setView(rootView)

        //Megnézzük, hogy kapott-e argumentumot (a fő ablakból), ha igen, akkor az adattagokat beállítjuk erre
        // tehát az Edittext-ek kapnak értéket, és az ablak címét beállítjuk
        val arguments = this.arguments
        if (arguments != null &&
                arguments.containsKey(MainActivity.KEY_ITEM_TO_EDIT)) {
            val item = arguments.getSerializable(
                    MainActivity.KEY_ITEM_TO_EDIT) as StudentItem
            //Itt is szükséges a bővítés új Shopping Item adattag esetén
            etName.setText(item.name)
            etAge.setText(item.age.toString())
            etAddres.setText(item.address)

            builder.setTitle("Edit person")
        }
    }


    override fun onResume() {
        super.onResume()

        val dialog = dialog as AlertDialog
        val positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE)
         //OK gomb a dialógus ablakon
        //vizsgálja az eseménykezelője, hogy a dialógus ablak kapott-e paramétereket
        //Ha kapott, akkor a handleItemEdit() hívódik meg (edit)
        //Ha nem kapott, akor a handleItemCreate() hívódik meg (create)
        positiveButton.setOnClickListener {
            if (etName.text.isNotEmpty()) {
                val arguments = this.arguments
                if (arguments != null &&
                        arguments.containsKey(MainActivity.KEY_ITEM_TO_EDIT)) {
                    handleItemEdit()
                } else {
                    handleItemCreate()
                }

                dialog.dismiss()
            } else {
                etName.error = "This field can not be empty"
            }
        }
    }
    //Új elem esetén hvódik meg, egy új ShoppingItem-et hoz létre
    //az itemId azért null, mert a DB adja a kulcsot
    //Itt is szükséges a bővítés új Shopping Item adattag esetén
    private fun handleItemCreate() {
        personItemHandler.personItemCreated(StudentItem(
                null,
                etName.text.toString(),
                etAge.text.toString().toInt(),
                false,
            etAddres.text.toString()
        ))
    }
    //Edit esetén hívódik meg, az edit-et csinálja, paraméterként átadja az adatokat
    //Itt is szükséges a bővítés új Student Item adattag esetén
    private fun handleItemEdit() {
        val itemToEdit = arguments?.getSerializable(
                MainActivity.KEY_ITEM_TO_EDIT) as StudentItem
        itemToEdit.name = etName.text.toString()
        itemToEdit.age = etAge.text.toString().toInt()
        itemToEdit.address=etAddres.text.toString()

        personItemHandler.personItemUpdated(itemToEdit)
    }
}
