package au.net.tech.app.baseclasses

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import au.net.tech.app.AppSharedPrefs
import au.net.tech.app.ui.LoginActivity
import com.kaopiz.kprogresshud.KProgressHUD
import com.mesibo.api.Mesibo
import java.lang.Exception


open class BaseFragment : Fragment() {

    lateinit var pb: KProgressHUD


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pb = KProgressHUD.create(requireContext())
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(false)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun showWaitingDialog() {
        if (pb.isShowing)
            pb.dismiss()

        try {
            pb.show()
        } catch (ex: Exception) {

        }
    }

    fun dismissWaitingDialog() {
        pb.dismiss()
    }

    fun logoutUser() {
        AppSharedPrefs.saveUser(null)
        AppSharedPrefs.setRememberMe(false)
        try {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()

        }catch (ex : Exception){

        }

    }

    override fun onDestroy() {
        pb.dismiss()
        super.onDestroy()
    }
}