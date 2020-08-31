package au.net.tech.app.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import au.net.tech.app.*
import au.net.tech.app.baseclasses.BaseFragment
import au.net.tech.app.models.Company
import au.net.tech.app.models.ContactDto
import au.net.tech.app.models.DepartmentDto
import au.net.tech.app.models.PrioritiesDto
import au.net.tech.app.networking.Networking
import au.net.tech.app.ui.ChatActivity
import com.bigkoo.pickerview.MyOptionsPickerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.fragment_add_ticket.view.*


class AddTicketFragment : BaseFragment() {
    lateinit var mView: View
    private var departmentPicker: MyOptionsPickerView<String>? = null
    private var prioritiesPickerView: MyOptionsPickerView<String>? = null
    private var contactIdPickerView: MyOptionsPickerView<String>? = null

    val TAG = AddTicketFragment::class.simpleName

    var selectedPriorityId = ""
    var selectedDepartmentId = ""
    var selectedContactId : String? = null

    private var subscribers = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_add_ticket, container, false)

        applyListeners()

        getDataFromServer()

        return mView
    }

    private fun getDataFromServer() {
        getPriorities()
        getDepartments()
        getCompanies()
    }


    private fun applyListeners() {
        mView.etEnterSubject.setCustomAnimation(mView.dvdSubject)
        mView.etMessage.setCustomAnimation(mView.dvdMessage)

        mView.tvRetryDep.setOnClickListener {
            getDepartments()
        }

        mView.tvRetryPriority.setOnClickListener {
            getPriorities()
        }

        mView.tvRetryContactId.setOnClickListener {
            getCompanies()
        }

        mView.etPriority.setOnClickListener {
            prioritiesPickerView?.show()
        }

        mView.etDepartment.setOnClickListener {
            departmentPicker?.show()
        }

        mView.etCotactId.setOnClickListener {
            contactIdPickerView?.show()
        }

        mView.btnOpenTicket.setOnClickListener {
            if (isValidForm())
                openTicket()
        }
    }

    private fun isValidForm(): Boolean {

        if (mView.etEnterSubject.getTrimmedText() == "") {
            mView.etEnterSubject.error = "Please enter subject"
            return false
        }
        if (mView.etDepartment.getTrimmedText() == "") {
            mView.etDepartment.error = "Please select department"
            return false
        }
        if (mView.etPriority.getTrimmedText() == "") {
            mView.etPriority.error = "Please select priority"
            return false
        }
        if (Utils.isStaff()) {
            if (mView.etCotactId.getTrimmedText() == "") {
                mView.etCotactId.error = "Please select contactId"
                return false
            }
        }

        if (mView.etMessage.getTrimmedText() == "") {
            mView.etMessage.error = "Please enter some message"
            return false
        }
        return true
    }


    private fun setDepartmentsPickerView(departments: ArrayList<DepartmentDto>) {

        departmentPicker = MyOptionsPickerView(context)
        val items = ArrayList<String>()
        departments.forEach {
            items.add(it.name ?: "")
        }

        departmentPicker?.setPicker(items)
        departmentPicker?.setTitle("")
        departmentPicker?.setCyclic(false)
        departmentPicker?.setSelectOptions(0)
        departmentPicker?.setOnoptionsSelectListener { options1, option2, options3 ->
            Log.d(TAG, "setDepartmentsPickerView: ${departments[options1].id}")
            selectedDepartmentId = departments[options1].id ?: ""
            mView.etDepartment.text = departments[options1].name
        }
    }

    private fun setPrioritiesPickerView(priorities: ArrayList<PrioritiesDto>) {

        prioritiesPickerView = MyOptionsPickerView(context)
        val items = ArrayList<String>()
        priorities.forEach {
            items.add(it.name ?: "")
        }

        prioritiesPickerView?.setPicker(items)
        prioritiesPickerView?.setTitle("")
        prioritiesPickerView?.setCyclic(false)
        prioritiesPickerView?.setSelectOptions(0)
        prioritiesPickerView?.setOnoptionsSelectListener { options1, option2, options3 ->
            selectedPriorityId = priorities[options1].id ?: ""
            mView.etPriority.text = priorities[options1].name
        }
    }

    private fun setContactIds(contactIds: ArrayList<ContactDto>) {

        contactIdPickerView = MyOptionsPickerView(context)
        val items = ArrayList<String>()
        contactIds.forEach {
            items.add("${it.name}   ${it.subtext}")
        }

        contactIdPickerView?.setPicker(items)
        contactIdPickerView?.setTitle("")
        contactIdPickerView?.setCyclic(false)
        contactIdPickerView?.setSelectOptions(0)
        contactIdPickerView?.setOnoptionsSelectListener { options1, option2, options3 ->
            selectedContactId = contactIds[options1].id ?: ""
            mView.etCotactId.text = "${contactIds[options1].name}   ${contactIds[options1].subtext}"
        }
    }

    private fun resetFields() {
        mView.etEnterSubject.requestFocus()
        mView.etEnterSubject.setText("")
        mView.etDepartment.text = ""
        mView.etPriority.text = ""
        mView.etCotactId.text = ""
        mView.etMessage.setText("")
    }


    private fun openTicket() {
        showWaitingDialog()

        Log.d(TAG, "openTicket: $selectedDepartmentId  $selectedPriorityId  $selectedContactId")
        subscribers.add(Networking.create().openTicket(
            department = selectedDepartmentId,
            priority = selectedPriorityId,
            message = mView.etMessage.getTrimmedText(),
            subject = mView.etEnterSubject.getTrimmedText(),
            contactid = selectedContactId
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    dismissWaitingDialog()
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    resetFields()

                    if (result.ticket_group_id != null) {
                        openChatFragment(result.ticket_group_id!!, result.ticket_id!!)
                    }
                },
                { error ->
                    Log.d(TAG, "openTicket: ")
                    dismissWaitingDialog()
                    Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            )
        )
    }

    private fun openChatFragment(ticketId: String, gId: String) {

        if (AppSharedPrefs.getUser()?.mesibo_id == null)
            return

        startActivity(Intent(context, ChatActivity::class.java).apply {
            putExtra("gid", gId)
            putExtra("tittle", ticketId)
        })

    }

    private fun getDepartments() {
        mView.tvRetryDep.visibility = View.GONE
        mView.pbDepartment.visibility = View.VISIBLE

        subscribers.add(Networking.create().getDepartments()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    if (result != null) {
                        mView.tvRetryDep.visibility = View.GONE
                        mView.pbDepartment.visibility = View.GONE
                        setDepartmentsPickerView(result)
                    }
                },
                { error ->
                    mView.tvRetryDep.visibility = View.VISIBLE
                    mView.pbDepartment.visibility = View.GONE

                }
            )
        )
    }

    private fun getPriorities() {
        mView.tvRetryPriority.visibility = View.GONE
        mView.pbPriorities.visibility = View.VISIBLE

        subscribers.add(
            Networking.create().getPriorities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        mView.pbPriorities.visibility = View.GONE
                        mView.tvRetryPriority.visibility = View.GONE
                        setPrioritiesPickerView(result)
                    },
                    { error ->
                        mView.tvRetryPriority.visibility = View.VISIBLE
                        mView.pbPriorities.visibility = View.GONE
                    }
                )
        )
    }

    private fun getCompanies() {
        if (Utils.isStaff()) {
            mView.clContactId.visibility = View.VISIBLE
            mView.pbContactId.visibility = View.VISIBLE
            mView.tvRetryContactId.visibility = View.GONE

            subscribers.add(Networking.create().searchContact()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        mView.pbContactId.visibility = View.GONE
                        mView.tvRetryContactId.visibility = View.GONE
                        setContactIds(result)
                    },
                    { error ->
                        mView.tvRetryContactId.visibility = View.VISIBLE
                        mView.pbContactId.visibility = View.GONE
                    }
                ))

        } else {
            mView.clContactId.visibility = View.GONE
            mView.dvdContactId.visibility = View.GONE
        }

    }

}