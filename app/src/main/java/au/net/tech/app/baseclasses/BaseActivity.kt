package au.net.tech.app.baseclasses

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kaopiz.kprogresshud.KProgressHUD

open class BaseActivity : AppCompatActivity() {

    val _TAG = "BaseActivity"
    lateinit var pb : KProgressHUD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Log.d(_TAG, "onCreate: ")

        pb = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(false)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }

   
    fun showWaitingDialog()
    {
        if(pb.isShowing)
            pb.dismiss()

        pb.show()
    }

    fun dismissWaitingDialog()
    {
        pb.dismiss()
    }
}