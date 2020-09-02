package au.net.tech.app.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import au.net.tech.app.AppSharedPrefs
import au.net.tech.app.R
import au.net.tech.app.baseclasses.BaseActivity
import au.net.tech.app.getTrimmedText
import au.net.tech.app.models.User
import au.net.tech.app.networking.Networking
import au.net.tech.app.setCustomAnimation
import com.mesibo.api.Mesibo
import com.mesibo.mediapicker.MediaPicker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity() {

    val TAG = "LoginActivity"
    private var subscribers = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.d(TAG, "onCreate: ")

        checkIsUserLoggedIn()
        applyListeners()

    }



    private fun checkIsUserLoggedIn() {
        if(AppSharedPrefs.isRememberMe() && AppSharedPrefs.isUserLoggedIn())
        { openMainActivity() }
    }

    private fun openMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }

    private fun applyListeners() {
        btnLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        etEmail.setCustomAnimation(dvdUserName)
        etPass.setCustomAnimation(dvdPass)

        btnLogin.setOnClickListener {



            if (isFormValid()) {
                loginUser()
            }
        }

    }

    private fun loginUser() {
        showWaitingDialog()
        subscribers.add(
            Networking.create()
                .loginUser(email = etEmail.getTrimmedText(), password = etPass.getTrimmedText())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        dismissWaitingDialog()
                        renderResult(result)
                    },
                    { error ->
                        onError(error.localizedMessage ?: "")
                        dismissWaitingDialog()
                    }
                )
        )
    }

    private fun renderResult(result: User?) {
        if (result != null) {

            AppSharedPrefs.saveUser(result)
            AppSharedPrefs.setRememberMe(cbRemember.isChecked)
            openMainActivity()
        } else {
            onError("Some thing went wrong please contact admin")
        }
    }

    private fun onError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun isFormValid(): Boolean {
        if (etEmail.getTrimmedText() == "") {
            etEmail.error = "Please Enter Email"
            return false
        }

        if (etPass.getTrimmedText() == "") {
            etPass.error = "Please Enter Password"
            return false
        }

        if (!cbTerms.isChecked) {
            Toast.makeText(this, "Please accept terms and conditions", Toast.LENGTH_SHORT).show()
            return false
        }


        return true

    }
}