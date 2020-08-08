public class FileSystemInterface {

    DocumentFile workDir;

    public FileSystemInterface(DocumentFile workDir) {
        this.workDir = workDir;
    }

    @JavascriptInterface
    public void __njsinterfaceinit() {
        return "{\"constants\": {\"UV_FS_SYMLINK_DIR\":1,\"UV_FS_SYMLINK_JUNCTION\":2,\"O_RDONLY\":0,\"O_WRONLY\":1,\"O_RDWR\":2,\"UV_DIRENT_UNKNOWN\":0,\"UV_DIRENT_FILE\":1,\"UV_DIRENT_DIR\":2,\"UV_DIRENT_LINK\":3,\"UV_DIRENT_FIFO\":4,\"UV_DIRENT_SOCKET\":5,\"UV_DIRENT_CHAR\":6,\"UV_DIRENT_BLOCK\":7,\"S_IFMT\":61440,\"S_IFREG\":32768,\"S_IFDIR\":16384,\"S_IFCHR\":8192,\"S_IFLNK\":40960,\"O_CREAT\":256,\"O_EXCL\":1024,\"UV_FS_O_FILEMAP\":536870912,\"O_TRUNC\":512,\"O_APPEND\":8,\"F_OK\":0,\"R_OK\":4,\"W_OK\":2,\"X_OK\":1,\"UV_FS_COPYFILE_EXCL\":1,\"COPYFILE_EXCL\":1,\"UV_FS_COPYFILE_FICLONE\":2,\"COPYFILE_FICLONE\":2,\"UV_FS_COPYFILE_FICLONE_FORCE\":4,\"COPYFILE_FICLONE_FORCE\":4}}";
    }

    @JavascriptInterface
    public void accessSync(String path) {

    }

    @JavascriptInterface
    public void readFileSync(String path) {
        
    }
}