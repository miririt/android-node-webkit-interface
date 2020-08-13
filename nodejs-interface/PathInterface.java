import androidx.documentfile.provider.DocumentFile;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathInterface {

    DocumentFile workDir;

    public PathInterface(DocumentFile workDir) {
        this.workDir = workDir;
    }

    public static String replaceLast(String str, String pattern, String replacement) {
        int idx = str.lastIndexOf(pattern);
        if(idx == -1){
            return str;
        }else{
            return str.substring(0, idx) + replacement + str.substring(idx + pattern.length());
        }
    }

    @JavascriptInterface
    public String __njsinterfaceinit() {
        return "{\"delimiter\": \":\", \"sep\": \"/\"}";
    }

    @JavascriptInterface
    public String basename(String path) {
        try {
            int sep = path.lastIndexOf('/');
            return path.substring(sep + 1);
        } catch(Exception ignore) {
            return "";
        }
    }
    @JavascriptInterface
    public String basename(String path, String ext) {
        try {
            String baseName = basename(path);

            if(path.endsWith(ext)) return replaceLast(baseName, ext, "");
            else return baseName;
        } catch(Exception ignore) {
            return "";
        }
    }

    @JavascriptInterface
    public String dirname(String path) {
        try {
            int sep = path.lastIndexOf('/');
            return path.substring(0, sep);
        } catch(Exception ignore) {
            return "";
        }
    }

    @JavascriptInterface
    public String extname(String path) {
        try {
            int sep = path.lastIndexOf('.');
            return path.substring(sep);
        } catch(Exception ignore) {
            return "";
        }
    }
    /*
    @JavascriptInterface
    public void format(JSON) { }
    */
    @JavascriptInterface
    public boolean isAbsolute(String path) {
        return Paths.get(path).isAbsolute();
    }

    @JavascriptInterface
    public String join(String first, String... more) {
        return Paths.get(first, more).toString();
    }

    @JavascriptInterface
    public String normalize(String path) {
        return Paths.get(path).normalize().toString();
    }
    /*
    @JavascriptInterface
    public JSON parse(String path) { }
    */
    @JavascriptInterface
    public String relative(String from, String to) {
        return Paths.get(from).relativize(Paths.get(to)).toString();
    }
    @JavascriptInterface
    public String join(String first, String... more) {
        Path basePath = Paths.get(first);
        for(int i = 0; i < more.length; i++) {
            basePath = basePath.resolve(more[i]);
        }
        return basePath.toString();
    }
}