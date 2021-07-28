package pot.ner347.qskin.skin

import android.content.Context
import android.text.TextUtils
import android.util.Log
import top.ner347.qskin.library.SkinPreference
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import kotlin.concurrent.thread

const val SKIN_RED = "skin_red.skin"

class SkinUtil {
    companion object {
        /**
         * 从 Assets 中将皮肤包存到 sd 卡
         */
        @JvmStatic
        fun saveSkinFiles(context: Context, version: Int) {
            val sp = context.getSharedPreferences("skinname", Context.MODE_PRIVATE)
            if (version <= sp.getInt("version", 0)) {
                return
            }

            thread {
                var outputStream: OutputStream? = null
                var inputStream: InputStream? = null
                try {
                    context.assets.list("skins")?.forEach {
                        val file = File(context.filesDir, "skins/$it").apply {
                            if (exists()) delete()
                            if (!parentFile.exists()) parentFile.mkdirs()
                        }

                        try {
                            outputStream = file.outputStream()
                            inputStream = context.assets.open("skins/$it")
                            val buffer = ByteArray(1024)
                            var len = 0
                            len = inputStream!!.read(buffer)
                            while (len != -1) {
                                outputStream!!.write(buffer, 0, len)
                                len = inputStream!!.read(buffer)
                            }
                            outputStream!!.flush()
                            sp.edit().putInt("version", version).apply()
                        } catch (e: Exception) {
                            Log.e("SKin", "皮肤包加载失败 ${e.toString()}")
                        } finally {
                            inputStream?.close()
                            outputStream?.close()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        @JvmStatic
        fun getCurrentSkinFileName() : String? {
            val path = SkinPreference.getSkin()
            if (TextUtils.isEmpty(path) || !File(path).exists()) {
                return ""
            } else {
                return path!!.substringAfterLast(File.separator)
            }
        }

        /**
         * Key 文件名
         * Value 文件路径，如 data/data/${packageName}/files/skin/${skinname}
         */
        fun getSkinMap(context: Context): MutableMap<String, String>{
            val map = mutableMapOf<String, String>()
            File(context.filesDir, "skins").takeIf { it.exists() }?.listFiles()?.forEach {
                map[it.name] = it.absolutePath
            }
            return map
        }
    }
}

