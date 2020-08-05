package pot.ner347.qskin.skin

import java.io.File

/**
 * md5 文件md5
 * name 皮肤包的名字
 * url 下载地址
 */
class SkinBean(md5: String, val name: String, url: String) {
    // 下载完成后缓存地址
    var path: String? = null
    private var file: File? = null

    fun getSkinFile(parentFile: File): File {
        file = file ?: File(parentFile, name)
        path = file!!.absolutePath
        return file!!
    }
}