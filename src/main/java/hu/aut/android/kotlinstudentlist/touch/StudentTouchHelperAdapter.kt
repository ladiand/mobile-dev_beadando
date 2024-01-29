package hu.aut.android.kotlinstudentlist.touch

interface StudentTouchHelperAdapter {

    fun onItemDismissed(position: Int)

    fun onItemMoved(fromPosition: Int, toPosition: Int)
}