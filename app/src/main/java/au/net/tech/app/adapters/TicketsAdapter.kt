package au.net.tech.app.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import au.net.tech.app.R
import au.net.tech.app.models.Ticket
import kotlinx.android.synthetic.main.list_item_tickets.view.*

import java.util.*
import kotlin.collections.ArrayList

class TicketsAdapter(

) : RecyclerView.Adapter<TicketsAdapter.MyViewHolder>() {
    var mListener: ((Ticket) -> Unit)? = null

    private var tickets = ArrayList<Ticket>()
    private var statusColor = ""


    inner class MyViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        private var tvTicketMsg: TextView = view.tvTicketMsg
        private var tvTicketId: TextView = view.tvTicketId
        private var tvPriority: TextView = view.tvPriority
        private var btnTicketStatus: Button = view.btnTicketStatus
        private var bg: ConstraintLayout = view.bg

        fun bind(item: Ticket) {
            tvTicketMsg.text = item.subject ?: ""
            tvTicketId.text = item.ticketid ?: ""
            tvPriority.text = item.priorityName ?: ""
            btnTicketStatus.text = item.statusName ?: ""

            Log.d("TAG", "bind: ")
            btnTicketStatus.setBackgroundColor(Color.parseColor(statusColor))

            bg.setOnClickListener {
                mListener?.invoke(item)
            }

        }

    }

    fun clear() {
        tickets.clear()
        notifyDataSetChanged()
    }

    fun updateList(list : ArrayList<Ticket>)
    {
        tickets.clear()
        tickets.addAll(list)
        notifyDataSetChanged()
    }

    fun setClickListener(onItemClicked: ((Ticket) -> Unit)) {
        mListener = onItemClicked
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_tickets, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = tickets[position]

        holder.bind(currentItem)

    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return tickets.size
    }

    fun updateStatusColor(selectedStatusColor: String) {

        statusColor = selectedStatusColor
    }

}