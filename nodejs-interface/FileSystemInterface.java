import androidx.documentfile.provider.DocumentFile;
import android.net.Uri;
import android.content.ContentResolver;

public class FileSystemInterface {

    ContentResolver resolver;
    DocumentFile workDir;
    Uri workUri;

    public FileSystemInterface(Context context, DocumentFile workDir) {
        this.resolver = context.getContentResolver();
        this.workDir = workDir;
        this.workUri = workDir.getUri();
    }

    protected static Uri buildUri(String path) {
        return Uri.withAppendedPath(workUri, Uri.encode(path));
    }

    protected static boolean createFile(String path) {
        if(DocumentFile.fromSingleUri(buildUri(path)).exists()) return true;

        DocumentFile parent = DocumentFile.fromTreeUri(buildUri(path)).getParentFile();
        DocumentFile target = parent.createFile("application/octet-stream", "__DUMMY_FILE_NAME__");
        if(target.renameTo(Paths.get(path).getFileName())) {
            return true;
        } else {
            target.delete();
            return false;
        }
    }

    @JavascriptInterface
    public boolean existsSync(String path) {
        try {
            DocumentFile target = DocumentFile.fromSingleUri(buildUri(path));
            if(target.exists()) return true;
            else return false;
        } catch(Exception ignore) {
            return false;
        }
    }

    @JavascriptInterface
    public String readFileSync(String path, String encoding) {
        try {
            FileInputStream fis = this.resolver.openInputStream(buildUri(path));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }

            return result.toString(encoding);
        } catch(Exception ignore) {
            return "";
        }
    }

    @JavascriptInterface
    public boolean writeFileSync(String path, String data, String encoding) {
        try {
            createFile(path);
            FileOutputStream fos = this.resolver.openOutputStream(buildUri(path));

            fos.write(data.getBytes(encoding));
            fos.close();

            return true;
        } catch(Exception e) {
            return false;
        }
    }

    @JavascriptInterface
    public boolean appendFileSync(String path, String data, String encoding) {
        try {
            createFile(path);
            FileOutputStream fos = this.resolver.openOutputStream(buildUri(path), "a");

            fos.write(data.getBytes(encoding));
            fos.close();

            return true;
        } catch(Exception e) {
            return false;
        }
    }

    @JavascriptInterface
    public boolean copyFileSync(String src, String dst, int mode) {

        FileChannel sourceChannel = null;
        FileChannel destChannel = null;

        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = this.resolver.openOutputStream(buildUri(src));

            createFile(dst);
            fos = this.resolver.openOutputStream(buildUri(dst));
        } catch(Exception ignore) {
            return false;
        }

        try {
            sourceChannel = fis.getChannel();
            destChannel = fos.getChannel();

            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } finally {
            sourceChannel.close();
            destChannel.close();
            fos.close();
        }
        
        return true;
    }
}