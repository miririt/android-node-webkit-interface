package net.miririt.maldives.compat

import android.content.Context
import android.net.Uri
import android.webkit.JavascriptInterface
import androidx.documentfile.provider.DocumentFile
import net.miririt.maldives.appendUri
import org.json.JSONArray
import java.io.*

class FileSystemInterface(var context: Context, private val workDir: DocumentFile) {

    private val workUri: Uri = workDir.uri
    private val resolver = context.contentResolver

    private fun getFilename(path: String): String {
        return path.substring(path.lastIndexOf('/') + 1)
    }

    private fun getParentFilename(path: String): String {
        val parentPath = path.substring(0, path.lastIndexOf('/'))
        return parentPath.substring(parentPath.lastIndexOf('/') + 1)
    }

    private fun getParentPath(path: String): String {
        return path.substring(0, path.lastIndexOf('/'))
    }

    private fun resolvePath(startDir : DocumentFile, path: String) : DocumentFile {
        var currentPath = startDir
        val segments = path.split('/')
        for(dirName in segments) {
            if(dirName != "" && dirName != ".")
                currentPath = currentPath.findFile(dirName)!!
        }
        return currentPath
    }

    private fun touchFile(path : String) : Boolean {
        return try {
            val targetFile = DocumentFile.fromSingleUri(context, appendUri(workUri, path))

            if(targetFile != null && targetFile.exists()) {
                return true
            }

            val parentFile = try {
                resolvePath(workDir, getParentPath(path))
            } catch(e : Exception) {
                mkdirSync(getParentPath(path), true)
                resolvePath(workDir, getParentPath(path))
            }

            val createdFile = parentFile.createFile("", getFilename(path))

            if(createdFile != null && createdFile.exists() && createdFile.name == getFilename(path)) {
                true
            } else if(createdFile == null || !createdFile.exists()) {
                false
            } else {
                createdFile.renameTo(getFilename(path))
            }
        } catch(e : Exception) {
            false
        }
    }

    @JavascriptInterface
    fun existsSync(path: String?): Boolean {
        if(path == null) return false

        return try {
            resolver.openFileDescriptor(appendUri(workUri, path), "r")!!.close()
            true
        } catch (ignore: Exception) {
            false
        }
    }

    @JavascriptInterface
    fun readFileSync(path: String?, encoding: String?): String {
        if(path == null) return ""

        return try {
            val fis =
                resolver.openInputStream(appendUri(workUri, path))
            val baos = ByteArrayOutputStream()
            val buffer = ByteArray(1024 * 256)
            var length: Int
            while (fis!!.read(buffer).also { length = it } > 0) {
                baos.write(buffer, 0, length)
            }

            fis.close()
            baos.toString(encoding ?: "utf8")
        } catch (ignore: Exception) {
            ""
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
                resolver.openOutputStream(appendUri(workUri, path))
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
                resolver.openOutputStream(appendUri(workUri, path), "wa")
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

        val fis: InputStream?
        val fos: OutputStream?
        try {
            fis = resolver.openInputStream(appendUri(workUri, src))
            touchFile(dst)
            fos = resolver.openOutputStream(appendUri(workUri, dst))
        } catch (ignore: Exception) {
            return false
        }
        try {
            val buffer = ByteArray(1024 * 256)
            var length: Int
            while (fis!!.read(buffer).also { length = it } > 0) {
                fos?.write(buffer, 0, length)
            }

            fis.close()
            fos?.close()
        } catch (e: Exception) {
            return false
        }
        return true
    }

    @JavascriptInterface
    fun unlinkSync(path : String?) : Boolean {
        if(path == null) return false
        return try {
            val targetFile = DocumentFile.fromSingleUri(context, appendUri(workUri, path))
            targetFile!!.delete()
        } catch(e : Exception) {
            false
        }
    }

    @JavascriptInterface
    fun mkdirSync(path: String?, recursive: Boolean): Boolean {
        if(path == null) return false
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

    @JavascriptInterface
    fun readdirSync(path: String?): String {
        if(path == null) return "[]"
        return try {
            val jsonFileList = JSONArray()
            val dir = DocumentFile.fromTreeUri(context, appendUri(workUri, path))
            val fileList = dir!!.listFiles()
            for(file in fileList) {
                jsonFileList.put(file.name)
            }
            jsonFileList.toString()
        } catch(e : java.lang.Exception) {
            "[]"
        }
    }

    @JavascriptInterface
    fun isFile(path: String?): Boolean {
        if(path == null) return false
        return try {
            val targetFile = DocumentFile.fromTreeUri(context, appendUri(workUri, path))
            targetFile!!.isFile
        } catch(e: Exception) { false }
    }

    @JavascriptInterface
    fun isDirectory(path: String?): Boolean {
        if(path == null) return false
        return try {
            val targetFile = DocumentFile.fromTreeUri(context, appendUri(workUri, path))
            targetFile!!.isDirectory
        } catch(e: Exception) { false }
    }

    @JavascriptInterface
    fun fileSize(path: String?): Long {
        if(path == null) return 0L
        return try {
            val targetFile = DocumentFile.fromTreeUri(context, appendUri(workUri, path))
            if(!targetFile!!.isFile) 0L
            else targetFile.length()
        } catch(e: Exception) { 0L }
    }
}