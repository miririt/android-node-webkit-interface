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
        } else {
            return "";
        }
    }

    @JavascriptInterface
    public boolean writeFileSync(String path, String data, String encoding) {
        try {
            FileOutputStream fos = this.resolver.openOutputStream(buildUri(path));

            fos.write(data.getBytes(encoding));

            return true;
        } else {
            return false;
        }
    }
}