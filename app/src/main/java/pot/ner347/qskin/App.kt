package pot.ner347.qskin

import android.app.Application
import pot.ner347.qskin.skin.SkinUtil
import top.ner347.qskin.library.SkinManager

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SkinManager.init(this)
        SkinUtil.saveSkinFiles(this, 1);
    }
}