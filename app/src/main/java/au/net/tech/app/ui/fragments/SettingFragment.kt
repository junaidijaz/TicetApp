package au.net.tech.app.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import au.net.tech.app.AppSharedPrefs
import au.net.tech.app.R
import au.net.tech.app.baseclasses.BaseFragment
import kotlinx.android.synthetic.main.fragment_settings.view.*


class SettingFragment : BaseFragment() {


    lateinit var mView : View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mView = inflater.inflate(R.layout.fragment_settings, container, false)

        initGui()

        applyListeners()

        return mView
    }

    private fun applyListeners() {
        mView.btnLogout.setOnClickListener {
            logoutUser()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initGui() {
        mView.tvUserName.text  = "${AppSharedPrefs.getUser()?.firstname} ${AppSharedPrefs.getUser()?.lastname}"
        mView.tvUserEmail.text = AppSharedPrefs.getUser()?.email
    }


}