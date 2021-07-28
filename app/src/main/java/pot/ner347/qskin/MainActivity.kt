package pot.ner347.qskin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import pot.ner347.qskin.skin.SKIN_RED
import pot.ner347.qskin.skin.SkinUtil
import top.ner347.qskin.library.SkinManager
import java.io.File

class MainActivity : AppCompatActivity() {
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
            SkinManager.changeSkin(SkinUtil.getSkinMap(this)[SKIN_RED])
        }
        findViewById<Button>(R.id.reset).setOnClickListener {
            SkinManager.changeSkin(
                ""
            )
        }
        findViewById<Button>(R.id.toSecond).setOnClickListener {
            startActivity(Intent(this@MainActivity, SecondActivity::class.java))
        }

        findViewById<Button>(R.id.night).setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            recreate()
        }
        findViewById<Button>(R.id.day).setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            recreate()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val mode = newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK

        when(mode) {
            Configuration.UI_MODE_NIGHT_NO -> {}
            Configuration.UI_MODE_NIGHT_YES -> {}
        }
    }

    override fun onNightModeChanged(mode: Int) {
        super.onNightModeChanged(mode)
        when (mode) {
            AppCompatDelegate.MODE_NIGHT_NO -> {
            }
            AppCompatDelegate.MODE_NIGHT_YES -> {
            }
            AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY -> {
            }
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

    }

}