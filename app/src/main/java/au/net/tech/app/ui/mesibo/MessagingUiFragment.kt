package au.net.tech.app.ui.mesibo


import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import au.net.tech.app.R
import au.net.tech.app.Utils
import com.mesibo.api.Mesibo
import com.mesibo.api.Mesibo.MesiboMessage
import com.mesibo.api.Mesibo.MessageParams
import com.mesibo.messaging.MesiboMessagingFragment
import com.mesibo.messaging.MesiboRecycleViewHolder


class MessagingUiFragment : MesiboMessagingFragment(), MesiboRecycleViewHolder.Listener,
    Mesibo.MessageListener {

    val TAG = MessagingUiFragment::class.java.simpleName

    override fun Mesibo_onGetItemViewType(messageParams: MessageParams, message: String): Int {

        Log.d(
            TAG,
            "Mesibo_onGetItemViewType: ${messageParams.status} ${Mesibo.MSGSTATUS_RECEIVEDNEW} ${messageParams.groupid}  $message "
        )


        if (messageParams.isIncoming) {

            return MesiboRecycleViewHolder.TYPE_INCOMING
        }

        return MesiboRecycleViewHolder.TYPE_OUTGOING


    }

    override fun Mesibo_onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): MesiboRecycleViewHolder {
        Log.d(TAG, "Mesibo_onCreateViewHolder: ")
        when {
            MesiboRecycleViewHolder.TYPE_INCOMING == viewType -> {
                val v: View = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.incoming_chat_layout, viewGroup, false)

                return IncomingMessgaeViewHolder(v)
            }
            MesiboRecycleViewHolder.TYPE_OUTGOING == viewType -> {

                val v: View = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.out_going_chat_layout, viewGroup, false)

                return OutGoingMessgaeViewHolder(v)
            }
            else -> {
                val v: View = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.list_item_type_datetime, viewGroup, false)
                return DateTimeViewHolder(v)
            }
        }

    }

    override fun Mesibo_onBindViewHolder(
        mesiboRecycleViewHolder: MesiboRecycleViewHolder,
        viewType: Int,
        selected: Boolean,
        messageParams: MessageParams?,
        mesiboMessage: MesiboMessage?
    ) {


        when (viewType) {
            MesiboRecycleViewHolder.TYPE_INCOMING -> {
                val incomingView = mesiboRecycleViewHolder as IncomingMessgaeViewHolder
                incomingView.mViewIncomingMessage.text = Html.fromHtml(mesiboMessage?.message ?: "")
                incomingView.tvSentBy.text = Html.fromHtml(messageParams?.peer ?: "")
                incomingView.tvTimeStamp.text = Utils.dateFunction(messageParams?.ts ?: 0, true)

            }
            MesiboRecycleViewHolder.TYPE_OUTGOING -> {

                val outGoing = mesiboRecycleViewHolder as OutGoingMessgaeViewHolder

                val status = mesiboMessage?.status!!

                Log.d(TAG, "Mesibo_onBindViewHolder: $status  ${mesiboMessage.mid}")

                if (status == Mesibo.STATUS_UNKNOWN) {
                    outGoing.ivStatus.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            outGoing.ivStatus.context.resources,
                            R.drawable.ic_time,
                            null
                        )
                    )

                } else if (status == Mesibo.MSGSTATUS_RECEIVEDNEW || status == Mesibo.MSGSTATUS_RECEIVEDREAD) {
                    outGoing.ivStatus.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            outGoing.ivStatus.context.resources,
                            R.drawable.ic_tick_read,
                            null
                        )
                    )
                } else if (status == Mesibo.MSGSTATUS_SENT) {
                    outGoing.ivStatus.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            outGoing.ivStatus.context.resources,
                            R.drawable.ic_tick_sent,
                            null
                        )
                    )
                } else if (status == Mesibo.MSGSTATUS_DELIVERED) {
                    outGoing.ivStatus.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            outGoing.ivStatus.context.resources,
                            R.drawable.ic_tick_delivered,
                            null
                        )
                    )
                } else {
                    outGoing.ivStatus.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            outGoing.ivStatus.context.resources,
                            R.drawable.ic_caution,
                            null
                        )
                    )
                }

                outGoing.bg.visibility = View.VISIBLE
                outGoing.mViewIncomingMessage.text = Html.fromHtml(mesiboMessage.message ?: "")
                outGoing.tvTimeStamp.text = Utils.dateFunction(mesiboMessage.ts ?: 0, true)


            }

            MesiboRecycleViewHolder.TYPE_DATETIME -> {
                val dateViewHolder = mesiboRecycleViewHolder as DateTimeViewHolder
                val h: MesiboRecycleViewHolder


            }

            else -> {

                val dateViewHolder = mesiboRecycleViewHolder as DateTimeViewHolder
                val h: MesiboRecycleViewHolder

            }
        }
    }

    override fun Mesibo_oUpdateViewHolder(p0: MesiboRecycleViewHolder?, p1: MesiboMessage?) {

    }


    override fun Mesibo_onViewRecycled(mesiboRecycleViewHolder: MesiboRecycleViewHolder) {}

    class IncomingMessgaeViewHolder(p0: View) : MesiboRecycleViewHolder(p0) {
        var mViewIncomingMessage: TextView = p0.findViewById(R.id.tvInComingMsg)
        var tvSentBy: TextView = p0.findViewById(R.id.tvSentBy)
        var tvTimeStamp: TextView = p0.findViewById(R.id.tvTimeStamp)

    }

    class OutGoingMessgaeViewHolder(p0: View) : MesiboRecycleViewHolder(p0) {
        var mViewIncomingMessage: TextView = p0.findViewById(R.id.tvOutGoingMsg)
        var tvTimeStamp: TextView = p0.findViewById(R.id.tvTimeStamp)
        var ivStatus: ImageView = p0.findViewById(R.id.ivStatus)
        var bg: ConstraintLayout = p0.findViewById(R.id.bg)

    }

    class DateTimeViewHolder(p0: View) : MesiboRecycleViewHolder(p0) {
        var tvDate: TextView = p0.findViewById(R.id.tvDate)
    }


}

