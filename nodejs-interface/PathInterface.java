import androidx.documentfile.provider.DocumentFile;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathInterface {

    DocumentFile workDir;

    public PathInterface(DocumentFile workDir) {
        this.workDir = workDir;
    }

    @JavascriptInterface
    public boolean isAbsolute(String path) {
        return Paths.get(path).isAbsolute();
    }

    @JavascriptInterface
    public String normalize(String path) {
        return Paths.get(path).normalize().toString();
    }

    @JavascriptInterface
    public String relative(String from, String to) {
        return Paths.get(from).relativize(Paths.get(to)).toString();
    }
}