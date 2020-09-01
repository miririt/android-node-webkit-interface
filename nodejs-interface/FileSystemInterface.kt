import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.JavascriptInterface
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel

class FileSystemInterface(var context: Context, workDir: DocumentFile) {

    private var resolver: ContentResolver = context.contentResolver
    private var workUri: Uri = workDir.uri

    private fun appendUri(uri: Uri, path: String): Uri {
        return Uri.parse(uri.toString() + Uri.encode("/$path"))!!
    }

    private fun getFilename(path: String?): String {
        if(path == null) return ""
        return path.substring(path.lastIndexOf('/') + 1)
    }

    private fun getParentFilename(path: String?): String {
        if(path == null) return ""
        val parentPath = path.substring(0, path.lastIndexOf('/'))
        return parentPath.substring(parentPath.lastIndexOf('/') + 1)
    }

    private fun getParentPath(path: String?): String {
        if(path == null) return ""
        return path.substring(0, path.lastIndexOf('/'))
    }

    private fun touchFile(path: String?): Boolean {
        if(path == null) return false

        try {
            val existingTarget = DocumentFile.fromSingleUri(context, appendUri(workUri, path))
            if (existingTarget != null && existingTarget.exists()) return true
            val parent = DocumentFile.fromTreeUri(context, appendUri(workUri, getParentPath(path)))
            val target = parent!!.createFile("application/octet-stream", getFilename(path))
            return if (target!!.renameTo(getFilename(path))) {
                true
            } else {
                target.delete()
                false
            }
        } catch(e: Exception) {
            return false
        }
    }

    @JavascriptInterface
    fun existsSync(path: String?): Boolean {
        if(path == null) return false

        return try {
            val target = DocumentFile.fromSingleUri(context, appendUri(workUri, path))
            target!!.exists()
        } catch (ignore: Exception) {
            false
        }
    }

    @JavascriptInterface
    fun readFileSync(path: String?, encoding: String?): String {
        if(path == null) return ""

        return try {
            val fis =
                resolver.openInputStream(appendUri(workUri, path)) as FileInputStream?
            val baos = ByteArrayOutputStream()
            val buffer = ByteArray(1024 * 256)
            var length: Int
            while (fis!!.read(buffer).also { length = it } > 0) {
                baos.write(buffer, 0, length)
            }
            baos.toString(encoding ?: "utf8")
        } catch (ignore: Exception) {
            ignore.localizedMessage
        }
    }

    @JavascriptInterface
    fun writeFileSync(
        path: String?,
        data: String?,
        encoding: String?
    ): Boolean {
        if(path == null) return false

        return try {
            touchFile(path)
            val fos =
                resolver.openOutputStream(appendUri(workUri, path)) as FileOutputStream?
            fos!!.write(data?.toByteArray(charset(encoding!!)))
            fos.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    @JavascriptInterface
    fun appendFileSync(
        path: String?,
        data: String?,
        encoding: String?
    ): Boolean {
        if(path == null) return false

        return try {
            touchFile(path)
            val fos =
                resolver.openOutputStream(appendUri(workUri, path), "a") as FileOutputStream?
            fos!!.write(data?.toByteArray(charset(encoding!!)))
            fos.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    @JavascriptInterface
    fun copyFileSync(src: String?, dst: String?, mode: Int): Boolean {

        if(src == null || dst == null) return false

        val sourceChannel: FileChannel?
        val destChannel: FileChannel?
        val fis: FileInputStream?
        val fos: FileOutputStream?
        try {
            fis = resolver.openInputStream(appendUri(workUri, src)) as FileInputStream?
            touchFile(dst)
            fos = resolver.openOutputStream(appendUri(workUri, dst)) as FileOutputStream?
        } catch (ignore: Exception) {
            return false
        }
        try {
            sourceChannel = fis!!.channel
            destChannel = fos!!.channel
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size())
        } catch (e: Exception) {
            return false
        }
        return true
    }

    @JavascriptInterface
    fun mkdirSync(path: String?, recursive: Boolean): Boolean {
        val parentPath = getParentFilename(path)
        return try {
            if (!recursive) {
                val parentFile = DocumentFile.fromTreeUri(context, appendUri(workUri, parentPath))
                if (!parentFile!!.exists()) false
                else parentFile.createDirectory(getFilename(path)) != null
            } else {
                val parentFile = DocumentFile.fromTreeUri(context, appendUri(workUri, parentPath))
                if (!parentFile!!.exists()) mkdirSync(parentPath, true)
                parentFile.createDirectory(getFilename(path)) != null
            }
        } catch(e: Exception) {
            false
        }
    }

}