package au.net.tech.app.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import au.net.tech.app.AppSharedPrefs
import au.net.tech.app.R
import au.net.tech.app.adapters.TicketsAdapter
import au.net.tech.app.baseclasses.BaseFragment
import au.net.tech.app.models.Ticket
import au.net.tech.app.models.TicketStatus
import au.net.tech.app.networking.Networking
import au.net.tech.app.ui.ChatActivity
import au.net.tech.app.ui.MainActivity
import com.bigkoo.pickerview.MyOptionsPickerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.mesibo.api.Mesibo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : BaseFragment() {
    val TAG = "HomeFragment"

    var selectedStatus = ""
    var selectedStatusColor = ""
    var tickets = ArrayList<Ticket>()

    private var ticketTypesViewPicker: MyOptionsPickerView<String>? = null
    var mAdpater: TicketsAdapter? = null

    private var subscribers = CompositeDisposable()
    lateinit var mView: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mView = inflater.inflate(R.layout.fragment_home, container, false)
        Mesibo.addListener(this)

        buildRecyclerView()
        getDataFromServer()
        applyListeners()
        loginInMesibo()
        return mView
    }


    private fun applyListeners() {
        (requireActivity() as MainActivity).ivFilters.setOnClickListener {
            ticketTypesViewPicker?.show()
        }

        mView.llError.btnRetry.setOnClickListener {
            mView.llError.visibility = View.GONE
            getDataFromServer()
        }
    }

    private fun getDataFromServer() {
        if (!AppSharedPrefs.getStatus().isNullOrEmpty()) {
            setTicketsPickerView(AppSharedPrefs.getStatus()!!)
            getTickets()
            getTicketStatuses(true)

        } else {
            getTicketStatuses(false)
        }
    }

    private fun setTicketsPickerView(tickets: ArrayList<TicketStatus>) {

        ticketTypesViewPicker = MyOptionsPickerView(context)
        val items = ArrayList<String>()
        tickets.forEach {
            items.add(it.name ?: "")
        }

        ticketTypesViewPicker?.setPicker(items)
        ticketTypesViewPicker?.setTitle("")
        ticketTypesViewPicker?.setCyclic(false)
        ticketTypesViewPicker?.setSelectOptions(0)
        ticketTypesViewPicker?.setOnoptionsSelectListener { options1, option2, options3 ->
            selectedStatus = tickets[options1].name!!
            selectedStatusColor = tickets[options1].statuscolor ?: "#FAFAFA"
            filterTickets()
        }

        if (tickets.size > 0) {
            selectedStatus = tickets[0].name ?: ""
            selectedStatusColor = tickets[0].statuscolor ?: ""
        }

    }

    private fun getTicketStatuses(isCaching: Boolean) {
        showWaitingDialog()
        subscribers.add(Networking.create().getTicketStatuses()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    setTicketsPickerView(result)
                    AppSharedPrefs.saveStatus(result)
                    if (!isCaching)
                        getTickets()
                },
                { error ->
                    onError(error.localizedMessage ?: "")
                    dismissWaitingDialog()
                }
            )
        )
    }

    private fun getTickets() {
        showWaitingDialog()
        subscribers.add(
            Networking.create().getTickets()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        dismissWaitingDialog()
                        tickets = result
                        filterTickets()
                    },
                    { error ->
                        onError(error.localizedMessage ?: "")
                        dismissWaitingDialog()
                    }
                )
        )
    }


    private fun loginInMesibo() {
        if (AppSharedPrefs.getUser()?.mesibo_token != null && AppSharedPrefs.getUser()?.mesibo_token != null) {
            setTokenToMesibo(AppSharedPrefs.getUser()?.mesibo_token!!)
            Log.d(TAG, "loginInMesibo: ${AppSharedPrefs.getUser()?.mesibo_id}")
        }
    }

    private fun setTokenToMesibo(token: String) {

        if (AppSharedPrefs.getFcmToken() == "") {
            getFcmToken(token)
        } else {

            Log.d(TAG, "setTokenToMesibo: ")
            Mesibo.setAccessToken(token)

            Mesibo.setDatabase("mesibo.db", 0)
            if (Mesibo.start() == 0) {
                Log.d(TAG, "setTokenToMesibo: started")
            }
            Log.d(TAG, "setTokenToMesibo: ${AppSharedPrefs.getFcmToken()}")
            Mesibo.setPushToken(AppSharedPrefs.getFcmToken())

        }


    }

    private fun getFcmToken(token: String) {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                Log.d(TAG, "getFcmToken: ${task.result!!.token}")
                AppSharedPrefs.setFcmToken(task.result!!.token)
                setTokenToMesibo(token)

            })
    }

    private fun filterTickets() {
        Log.d(TAG, "filterTickets: $selectedStatus")
        val list = ArrayList(tickets.filter { it.statusName == selectedStatus })

        if (isListNotEmpty(list)) {
            mAdpater?.updateStatusColor(selectedStatusColor)
            mAdpater?.updateList(list)
        }

    }

    private fun isListNotEmpty(list: ArrayList<Ticket>): Boolean {
        return if (list.isEmpty()) {
            mView.tvNoTickets.visibility = View.VISIBLE
            mView.rvTickets.visibility = View.GONE
            mView.tvNoTickets.text = "No tickets available for ($selectedStatus)"
            false
        } else {
            mView.tvNoTickets.visibility = View.GONE
            mView.rvTickets.visibility = View.VISIBLE
            true
        }
    }

    private fun buildRecyclerView() {
        mAdpater?.clear()
        mAdpater = TicketsAdapter()
        mView.rvTickets.layoutManager = LinearLayoutManager(context)
        mView.rvTickets.adapter = mAdpater
        mAdpater?.setClickListener {
            if (it.mesibo_group_id != null) {
                openChatFragment(it.ticketid ?: "", it.mesibo_group_id!!)
            }
        }
    }

    private fun openChatFragment(ticketId: String, gId: String) {

        if (AppSharedPrefs.getUser()?.mesibo_id == null)
            return

        startActivity(Intent(context, ChatActivity::class.java).apply {
            putExtra("gid", gId)
            putExtra("tittle", ticketId)
        })

    }

    private fun onError(msg: String) {

        mView.llError.visibility = View.VISIBLE
        logoutUser()
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }


}