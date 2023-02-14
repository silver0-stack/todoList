import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.TodoAdapter
import com.example.myapplication.dto.Todo

class MyTouchHelperCallback(
    private val itemMoveListener: OnItemMoveListener
) : ItemTouchHelper.Callback() {


    /* move 이벤트에 대한 boolean 값 */
    private var isMoved = false

    // Activity 에서 사용할 interface
    interface ItemStartDragListener {
        // Drop 이 됐을 때 Activity 에서 사용할 메서드
        fun onDropActivity(initList : ArrayList<Todo>, changeList: ArrayList<Todo>)
    }

    interface OnItemMoveListener {
        //drag 처리를 위한 메소드
        fun onItemMove(fromPosition: Int, toPosition: Int)
        //drop 처리를 위한 메서드
        fun onDropAdapter()
        //스와이프를 위한 메서드
        fun onItemSwiped(position: Int)
    }

    /**
     * 어느 방향으로 움직일지에 따라서 flag 받는것을 정의
     * 드래그는 위, 아래 액션이기 때문에 up, down 을 넘겨줌
     */
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(dragFlags, 0)
    }

    /**
     * 어느 위치에서 어느 위치로 변경하는지
     */
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        //adapter 에 전달
        itemMoveListener.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
     //   isMoved=true
        return true
    }

    /**
     * ItemTouchHelper 로 Swipe 또는 Drag and Drop 하여 ViewHolder 가 변경될 때 호출
     */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
       when (actionState) {
            // 드래그 또는 스와이프가 끝났을 때 ACTION_STATE_IDLE 가 전달 됨.
            ItemTouchHelper.ACTION_STATE_IDLE -> itemMoveListener.onDropAdapter() // Adapter 에 전달

        }
    }

    /**
     * 좌우 스와이프
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        itemMoveListener.onItemSwiped(viewHolder.adapterPosition)
    }
}