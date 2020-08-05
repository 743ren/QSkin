package pot.ner347.qskin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import pot.ner347.qskin.skin.SkinBean
import top.ner347.qskin.library.SkinManager
import java.io.File

class MainActivity : AppCompatActivity() {
    var skins: List<SkinBean> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val file = getExternalFilesDir("")
        if (file!=null && !file.exists()) {
            file.mkdirs()
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1);
            }
        }

        findViewById<Button>(R.id.changeSkin).setOnClickListener {
//            selectSkin(skins[0])
            SkinManager.getInstance(application).changeSkin(
                "${getExternalFilesDir("")?.absolutePath}/skin_red.skin"
//               "/storage/emulated/0/Download/skin_red.skin"
            )
        }
        findViewById<Button>(R.id.reset).setOnClickListener {
            SkinManager.getInstance(application).changeSkin(
                ""
            )
        }
        findViewById<Button>(R.id.toSecond).setOnClickListener {
            startActivity(Intent(this@MainActivity, SecondActivity::class.java))
        }

    }

    private fun selectSkin(skin: SkinBean) {
        // 比如 apk 都存到 /Android/data/包名/files/theme 目录下
        val theme = File(filesDir, "theme")
        if (theme.exists() && theme.isFile) {
            theme.delete()
        }
        theme.mkdirs()
        val skinFile: File = skin.getSkinFile(theme)
        if (skinFile.exists()) { // 皮肤已存在，去执行换肤
            SkinManager.getInstance(application).changeSkin(skin.path)
            return
        }
        val tempSkin = File(skinFile.parentFile, "${skin.name}.temp")
        // 如果下载成功
        tempSkin.renameTo(skinFile)
        tempSkin.delete()
        // 执行换肤，可能要切换线程
        SkinManager.getInstance(application).changeSkin(skin.path)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

    }

}