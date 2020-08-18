import androidx.documentfile.provider.DocumentFile;
import android.net.Uri;

public class FileSystemInterface {

    Context context;
    DocumentFile workDir;
    Uri workUri;

    public FileSystemInterface(Context context, DocumentFile workDir) {
        this.context = context;
        this.workDir = workDir;
        this.workUri = workDir.getUri();
    }

    protected static Uri buildUri(String path) {
        return Uri.withAppendedPath(workUri, Uri.encode(path));
    }

    @JavascriptInterface
    public String accessSync(String path, int mode) {
        boolean flag = true;
        //F_OK: 1, R_OK: 2, W_OK: 4, X_OK: 8
        if(mode & 1 != 0)
            flag = flag && Files.exists(Paths.get(path));
        if(mode & 2 != 0)
            flag = flag && Files.isReadable(Paths.get(path));
        if(mode & 4 != 0)
            flag = flag && Files.isWritable(Paths.get(path));
        if(mode & 8 != 0)
            flag = flag && Files.isExecutable(Paths.get(path));
    }

    @JavascriptInterface
    public boolean appendFileSync(String path, String data) {
        FileOutputStream appendStream = null;
        try {
            appendStream = context.getContentResolver().openOutputStream(buildUri(path), "a");
            appendStream.write(data.getBytes());
            appendStream.flush();
            appendStream.close();

            return true;
        } catch(Exception e) {
            if(appendStream != null) appendStream.close();
            return false;
        }
    }

    @JavascriptInterface
    public boolean copyFileSync(String src, String dst, int mode) {
        //COPYFILE_EXECL: 1, COPYFILE_FILECLONE: 2, COPYFILE_FILECLONE_FORCE: 4

        if(mode & COPYFILE_EXECL != 0)
            Files.copy(Paths.get(src), Paths.get(dst), REPLACE_EXISTING);
        else
            Files.copy(Paths.get(src), Paths.get(dst));
    }

    @JavascriptInterface
    public String existsSync(String path) {
        return Files.exists(Paths.get(path));
    }

    @JavascriptInterface
    public String mkdirSync(String path, boolean recursive) {
        File targetDir = new File(path);
        if(recursive) {
            targetDir.mkdirs();
        } else {
            targetDir.mkdir();
        }
    }

}