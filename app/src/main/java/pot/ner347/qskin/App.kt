package pot.ner347.qskin

import android.app.Application
import top.ner347.qskin.library.SkinManager

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SkinManager.init(this)
    }
}