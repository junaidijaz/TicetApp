package au.net.tech.app

import android.app.Application
import android.content.Context
import com.mesibo.api.Mesibo


class App : Application(), Mesibo.FileTransferHandler {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        val api = Mesibo.getInstance()
        api.init(this)

        Mesibo.addListener(this)


    }



    companion object {

        lateinit var appContext: Context
            private set
    }

    override fun Mesibo_onStartFileTransfer(p0: Mesibo.FileInfo?): Boolean {
        return true
    }

    override fun Mesibo_onStopFileTransfer(p0: Mesibo.FileInfo?): Boolean {
        return true
    }
}