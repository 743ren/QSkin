package pot.ner347.qskin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import top.ner347.qskin.library.SkinManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.changeSkin).setOnClickListener {
            SkinManager.getInstance(application).changeSkin(
               "/storage/emulated/0/Download/skin_red.skin"
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
}