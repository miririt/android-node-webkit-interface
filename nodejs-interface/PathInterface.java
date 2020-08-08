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
    public void __njsinterfaceinit() {
        return "{\"delimiter\": \":\"}";
    }

    @JavascriptInterface
    public void basename(String path) {
        try {
            int sep = path.lastIndexOf('/');
            return path.substring(sep + 1);
        } catch(Exception ignore) {
            return "";
        }
    }
    @JavascriptInterface
    public void basename(String path, String ext) {
        try {
            String baseName = basename(path);

            if(path.endsWith(ext)) return replaceLast(baseName, ext, "");
            else return baseName;
        } catch(Exception ignore) {
            return "";
        }
    }

    @JavascriptInterface
    public void dirname(String path) {
        try {
            int sep = path.lastIndexOf('/');
            return path.substring(0, sep);
        } catch(Exception ignore) {
            return "";
        }
    }

    @JavascriptInterface
    public void extname(String path) {
        try {
            int sep = path.lastIndexOf('.');
            return path.substring(sep);
        } catch(Exception ignore) {
            return "";
        }
    }
}