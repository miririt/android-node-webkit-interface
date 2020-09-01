(function() {

    function _NJSPath() { }

    _NJSPath.basename = function(path, ext) {
        const baseName = path.substring(path.lastIndexOf('/'));
        if(ext && typeof ext === 'string') {
            if(baseName.endsWith(ext)) {
                return baseName.substring(0, baseName.length - ext.length);
            }
        }
        return baseName;
    };

    _NJSPath.delimiter = ':';

    _NJSPath.dirname = function(path) {
        return path.substring(path.lastIndexOf('/'), path.endsWith('/') ? path.length - 1 : path.length);
    };

    _NJSPath.extname = function(path) {
        const trimmedPath = path.replace(/^\.[^.]+/, '');
        return trimmedPath.substring(path.lastIndexOf('.'));
    };

    _NJSPath.format = function(pathObject) {
        let pathBuilder = [];
        if(pathObject.dir) {
            pathBuilder.push(pathObject.dir);
        } else if(pathObject.root) {
            pathBuilder.push(pathObject.root);
        }

        if(pathObject.base) {
            pathBuilder.push(pathObject.base);
        } else if(pathObject.name) {
            pathBuilder.push(pathObject.name + (pathObject.ext || ''));
        }

        return _NJSPath.join(...pathBuilder);
    };

    _NJSPath.isAbsolute = function(path) {
        return path.startsWith('/');
    };

    _NJSPath.join = function(...args) {
        return _NJSPath.normalize(args.join('/'));
    };

    _NJSPath.normalize = function(path) {
        if(path == '/') return '/';
        let normalized = path.replace(/\/+/, '/')
        .replace(/\/[^\/]+\/\.\./, '') // parent dir merge
        .replace(/\/\.\.($|\/)/, '/') // current dir omit
        .replace(/\/+/, '/');

        return normalized;
    };

    _NJSPath.resolve = function(...paths) {
        let resolvedPath = '';
        for(let i = 0; i < paths.length; i++) {
            const currentPath = i >= 0 ? args[i] : _NJSProcess.cwd();

            resolvedPath = `${currentPath}/${resolvedPath}`;

            if(isAbsolute(resolvedPath)) break;
        }

        return _NJSPath.normalize(resolvedPath);
    };

    _NJSPath.sep = '/';

    function _NJSFileSystem() { }

    _NJSFileSystem.constants = {
        'F_OK': 1, 'R_OK': 2, 'W_OK': 4, 'X_OK': 8,

        'COPYFILE_EXECL': 1, 'COPYFILE_FICLONE': 2, 'COPYFILE_FICLONE_FORCE': 4,

        'O_RDONLY': 1, 'O_WRONLY': 2, 'O_RDWR': 4, 'O_CREAT': 8, 'O_EXCL': 16,
        'O_NOCTTY': 32, 'O_TRUNC': 64, 'O_APPEND': 128, 'O_DIRECTORY': 256, 'O_NOATIME': 512,
        'O_NOFOLLOW': 1024, 'O_SYNC': 2048, 'O_DSYNC': 4096, 'O_SYMLINK': 8192, 'O_DIRECT': 16384,
        'O_NONBLOCK': 32768, 'UV_FS_O_FILEMAP': 65536

    };

    _NJSFileSystem.accessSync = function(path, mode = _NJSFileSystem.constants.F_OK) {
        return _NJSFileSystemInterface.accessSync(path, mode);
    };

    _NJSFileSystem.existsSync = function(path) {
        return _NJSFileSystemInterface.existsSync(path);
    };

    _NJSFileSystem.readFileSync = function(path, encoding = 'utf8') {
        if(typeof encoding === 'string')
            return _NJSFileSystemInterface.readFileSync(path, encoding);
        else
            return _NJSFileSystemInterface.readFileSync(path, encoding.encoding);
    };

    _NJSFileSystem.readFile = function(...args) {
        let path, options = {}, callback;
        if(args.length == 2) {
            [path, callback] = args;
        } else {
            [path, options, callback] = args;
        }

        new Promise(
            function(resolve, reject) {
                if(!options.encoding) options = { encoding: 'utf8' };
                const data = _NJSFileSystem.readFileSync(path, options);
                resolve(data);
            }
        ).then(data => callback(null, data));
    };

    _NJSFileSystem.writeFileSync = function(path, data, encoding = 'utf8') {
        if(typeof encoding === 'string')
            return _NJSFileSystemInterface.writeFileSync(path, data, encoding);
        else
            return _NJSFileSystemInterface.writeFileSync(path, data, encoding.encoding);
    };

    _NJSFileSystem.writeFile = function(...args) {
        let path, data, options = {}, callback;
        if(args.length == 3) {
            [path, data, callback] = args;
        } else {
            [path, data, options, callback] = args;
        }

        new Promise(
            function(resolve, reject) {
                if(!options.encoding) options = { encoding: 'utf8' };
                const err = _NJSFileSystem.writeFileSync(path, options.encoding) ? true : null;
                resolve(err);
            }
        ).then(err => callback(err));
    };

    _NJSFileSystem.appendFileSync = function(path, data, encoding = 'utf8') {
        return _NJSFileSystemInterface.appendFileSync(path, data, encoding);
    };

    _NJSFileSystem.appendFile = function(...args) {
        let path, data, options = {}, callback;
        if(args.length == 3) {
            [path, data, callback] = args;
        } else {
            [path, data, options, callback] = args;
        }

        new Promise(
            function(resolve, reject) {
                if(!options.encoding) options = { encoding: 'utf8' };
                const err = _NJSFileSystem.appendFileSync(path, options.encoding) ? true : null;
                resolve(err);
            }
        ).then(err => callback(err));
    };

    _NJSFileSystem.copyFileSync = function(src, dest, mode = 0) {
        return _NJSFileSystemInterface.copyFileSync(src, dest, mode);
    };

    _NJSFileSystem.copyFile = function(...args) {
        let src, dest, mode = 0, callback;
        if(args.length == 3) {
            [src, dest, callback] = args;
        } else {
            [src, dest, mode, callback] = args;
        }

        new Promise(
            function(resolve, reject) {
                const err = _NJSFileSystem.copyFileSync(src, dest, mode) ? true : null;
                resolve(err);
            }
        ).then(err => callback(err));
    };

    _NJSFileSystem.mkdirSync = function(path, recursive = false) {
        return _NJSFileSystemInterface.mkdirSync(path, recursive);
    };

    _NJSFileSystem.mkdir = function(...args) {
        let path, options = {}, callback;
        if(args.length == 2) {
            [path, callback] = args;
        } else {
            [path, options, callback] = args;
        }

        new Promise(
            function(resolve, reject) {
                if(!options.recursive) options = { recursive: true };
                const err = _NJSFileSystem.mkdirSync(path, options.recursive) ? true : null;
                resolve(err);
            }
        ).then(err => callback(err));
    };

    function _NJSProcess() { }

    _NJSProcess.argv = ['/', '/index.html'];
    _NJSProcess.chdir = () => {};
    _NJSProcess.config = { };
    _NJSProcess.cwd = () => '/';
    _NJSProcess.emitWarning = console.warn;
    _NJSProcess.env = {
        'USER': 'user',
        'PATH': '/',
        'PWD': '/',
        'HOME': '/'
    };
    _NJSProcess.execArgv = [];
    _NJSProcess.exit = () => { };
    _NJSProcess.mainModule = { 'filename': '/' };
    _NJSProcess.versions = {
        'node': '14.8.0',
        'chromium': "85.0.4183.83"
    };

    window.process = _NJSProcess
    function nw() { }

    nw.Clipboard = function() { };
    nw.Clipboard.ClipboardInstance = function() { };
    nw.Clipboard.get = function(type) { return this.ClipboardInstance; };
    nw.Clipboard.ClipboardInstance.get = function(type = 'text', raw) {
        return "";
    };
    nw.Clipboard.ClipboardInstance.set = function(data, type = 'text', raw = false) { };
    nw.Clipboard.ClipboardInstance.readAvailableTypes = () => [
        'text'
    ];
    nw.Clipboard.ClipboardInstance.clear = function() { };
    nw.App = {'argv': [], 'dataPath': '/'};

    nw.Window = function() { };
    nw.Window.get = function() { return this; };
    nw.Window.window = () => window;
    nw.Window.x = 0;
    nw.Window.y = 0;
    nw.Window.width = 2560;
    nw.Window.height = 1440;
    nw.Window.isAlwaysOnTop = () => false;
    nw.Window.isFullscreen = () => true;
    nw.Window.isTransparent = () => false;
    nw.Window.isKioskMode = () => false;
    nw.Window.zoomLevel = () => 0;
    nw.Window.moveTo = (x, y) => {};
    nw.Window.moveBy = (x, y) => {};
    nw.Window.resizeTo = (width, height) => {};
    nw.Window.setInnerWidth = (width) => {};
    nw.Window.setInnerHeight = (height) => {};
    nw.Window.resizeBy = (width, height) => {};
    nw.Window.focus = () => {};
    nw.Window.blur = () => {};
    nw.Window.show = (is_show = true) => {};
    nw.Window.hide = () => {};
    nw.Window.close = (force = false) => {};
    nw.Window.reload = () => { location.reload(); };
    nw.Window.maximize = () => { };
    nw.Window.minimize = () => { };
    nw.Window.restore = () => { };
    nw.Window.enterFullscreen = () => { };
    nw.Window.leaveFullscreen = () => { };
    nw.Window.setShadow = (shadow) => { };
    nw.Window.showDevTools = (iframe, callback) => { };
    nw.Window.closeDevTools = () => { };
    nw.Window.capturePage = (callback, config) => { };
    nw.Window.captureScreenshot = (options, callback) => { };
    nw.Window.eval = (frame, script) => { eval(script); };
    nw.Window.on = (type, listener) => { window.addEventListener(type, listener); };
    nw.Window.open = (url, options, callback) => { callback(window.open(url)); };

    let __njsinterface_list = {
        'path': _NJSPath,
        'fs': _NJSFileSystem,
        'process': _NJSProcess
    };
    let __nwinterface_list = {
        'nw.gui': nw
    };

    window.require = function(res) {
        if(__nwinterface_list[res]) {
            return __nwinterface_list[res];
        } else if(__njsinterface_list[res]) {
            return __njsinterface_list[res];
        } else {
            let xhr = new XMLHttpRequest();
            let exportValue = {};
            xhr.open('GET', res, false);
            xhr.send();

            try {
                if(xhr.status == 200) {
                    const evalCode = `(function(){ let module = { 'exports': {} }; { ${xhr.responseText}; } return module.exports; })();`;
                    exportValue = eval(evalCode);
                }
            } finally {
                return exportValue;
            }
        }
    }
})();