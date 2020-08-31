package au.net.tech.app.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import au.net.tech.app.AppSharedPrefs
import au.net.tech.app.R
import au.net.tech.app.baseclasses.BaseActivity
import au.net.tech.app.networking.mesibo.MesiboNetworking
import au.net.tech.app.ui.mesibo.MessagingUiFragment
import com.mesibo.api.Mesibo
import com.mesibo.messaging.MesiboMessagingFragment
import com.mesibo.messaging.MesiboUI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_chat_activty.*


class ChatActivity : BaseActivity(), MesiboMessagingFragment.FragmentListener , Mesibo.MessageListener {

    private var subscribers = CompositeDisposable()


    val TAG = "ChatActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_activty)
        var gId = (intent.getStringExtra("gid"))!!
        val tittle = (intent.getStringExtra("tittle"))!!

        gId = "101887"

        icBack.setOnClickListener {
            onBackPressed()
        }

        tbTittleUserName.text = tittle

        Log.d(TAG, "onCreate: $gId")

        Mesibo.addListener(this)

        Handler().postDelayed({
            pbChat.visibility = View.GONE
            setProfiles(gId)

        }, 50)

    }

    private fun  setProfiles(gId: String) {
        val profile =
            Mesibo.createUserProfile("", gId.toLong(), AppSharedPrefs.getUser()?.mesibo_id!!)

        Mesibo.setUserProfile(profile,true)


        showMessages(gId)
    }

    private fun addMemberToGroup(gId: String) {
       subscribers.add(MesiboNetworking.create().addMemberToGroup(
        gid =  gId)
              .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->


                    showMessages(gId)

                },
                { error ->


                }
            )
       )
    }


    private fun showMessages(gId: String) {
//        val mFragment = MesiboMessagingFragment()
        val mFragment = MessagingUiFragment()

        val bl = Bundle()
        bl.putLong(MesiboUI.GROUP_ID, gId.toLong())
        bl.putBoolean(MesiboMessagingFragment.SHOWMISSEDCALLS, false)
        bl.putBoolean(MesiboMessagingFragment.READONLY, false)

        mFragment.arguments = bl

        Handler().postDelayed({
            supportFragmentManager.beginTransaction()
                .add(R.id.flMain, mFragment).commit()
        }, 300)

    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        super.onDestroy()

    }

    override fun Mesibo_onShowInContextUserInterface() {

    }

    override fun Mesibo_onUpdateUserOnlineStatus(p0: Mesibo.UserProfile?, p1: String?) {
        Log.d(TAG, "Mesibo_onUpdateUserOnlineStatus: $p1")


        if(p1 == null )
        {
            tvUserStatus.visibility = View.VISIBLE
            tvUserStatus.text  = "Online"
        }else
        {
            tvUserStatus.text  = p1
        }



    }

    override fun Mesibo_onError(p0: Int, p1: String?, p2: String?) {
        Log.d(TAG, "Mesibo_onError: $p1 $p2")

    }

    override fun Mesibo_onContextUserInterfaceCount(p0: Int) {

    }

    override fun Mesibo_onHideInContextUserInterface() {

    }

    override fun Mesibo_onUpdateUserPicture(p0: Mesibo.UserProfile?, p1: Bitmap?, p2: String?) {

    }

    override fun Mesibo_onMessage(p0: Mesibo.MessageParams?, p1: ByteArray?): Boolean {
        Log.d(TAG, "Mesibo_onMessage: ${p0?.peer} $p1")
        return true
    }

    override fun Mesibo_onMessageStatus(p0: Mesibo.MessageParams?) {
        Log.d(TAG, "Mesibo_onMessageStatus: ${p0?.status}  ${p0?.mid}")
    }

    override fun Mesibo_onActivity(p0: Mesibo.MessageParams?, p1: Int) {

    }

    override fun Mesibo_onLocation(p0: Mesibo.MessageParams?, p1: Mesibo.Location?) {

    }

    override fun Mesibo_onFile(p0: Mesibo.MessageParams?, p1: Mesibo.FileInfo?) {

    }


}

