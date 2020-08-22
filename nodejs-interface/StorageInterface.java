import androidx.documentfile.provider.DocumentFile;
import android.net.Uri;
import android.content.ContentResolver;

public class StorageInterface {

    ContentResolver resolver;
    DocumentFile workDir;
    Uri workUri;

    public StorageInterface(Context context, DocumentFile gameDir) {
        this.resolver = context.getContentResolver();
        this.workDir = DocumentFile.fromTreeUri(Uri.withAppendedPath(gameDir.getUri(), "/www/save"));

        if(!workDir.exists()) {
            DocumentFile wwwDir = DocumentFile.fromTreeUri(Uri.withAppendedPath(gameDir.getUri(), "/www"));
            if(!wwwDir.exists()) {
                wwwDir = gameDir.createDirectory("www");
            }
            this.workDir = wwwDir.createDirectory("save");
        }
        
        this.workUri = this.workDir.getUri();
    }

    @JavascriptInterface
    public String getItem(String key) {
        return "";
    }

    @JavascriptInterface
    public void setItem(String key, String value) {
        return;
    }

    @JavascriptInterface
    public void removeItem(String key, String value) {
        setItem(key, null);
        return;
    }
}