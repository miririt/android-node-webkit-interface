function _NJSPath() {
    throw new Error('This is a static class');
}

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
    return _NJSPathInterface.normalize(path);
};

_NJSPath.relative = function(from, to) {
    return _NJSPathInterface.relative(from, to);
};

_NJSPath.resolve = function(...paths) {
    return _NJSPath.join(_NJSProcess.cwd(), ...paths);
};

_NJSPath.sep = '/';

function _NJSFileSystem() {
    throw new Error('This is a static class');
}

_NJSFileSystem.constants = {
    'F_OK': 1, 'R_OK': 2, 'W_OK': 4, 'X_OK': 8,

    'COPYFILE_EXECL': 1, 'COPYFILE_FICLONE': 2, 'COPYFILE_FICLONE_FORCE': 4,
    
    'O_RDONLY': 1, 'O_WRONLY': 2, 'O_RDWR': 4, 'O_CREAT': 8, 'O_EXCL': 16,
    'O_NOCTTY': 32, 'O_TRUNC': 64, 'O_APPEND': 128, 'O_DIRECTORY': 256, 'O_NOATIME': 512,
    'O_NOFOLLOW': 1024, 'O_SYNC': 2048, 'O_DSYNC': 4096, 'O_SYMLINK': 8192, 'O_DIRECT': 16384,
    'O_NONBLOCK': 32768, 'UV_FS_O_FILEMAP': 65536

}

_NJSFileSystem.accessSync = function(path, mode = _NJSFileSystem.constants.F_OK) {
    return _NJSFileSystemInterface.accessSync(path, mode);
}

_NJSFileSystem.readFileSync = function(path, encoding = 'utf8') {
    return _NJSFileSystemInterface.readFileSync(path, encoding);
}

_NJSFileSystem.readFile = function(...args) {
    let path, options, callback;
    if(args.length == 2) {
        [path, callback] = args;
    } else {
        [path, options, callback] = args;
    }

    new Promise(
        function(resolve, reject) {
            const data = _NJSFileSystem.readFileSync(path, options);
            resolve(data);
        }
    ).then(data => callback(null, data));
}

_NJSFileSystem.writeFileSync = function(path, data, encoding = 'utf8') {
    return _NJSFileSystemInterface.writeFileSync(path, data, encoding);
}

_NJSFileSystem.writeFile = function(...args) {
    let path, data, options, callback;
    if(args.length == 3) {
        [path, data, callback] = args;
    } else {
        [path, data, options, callback] = args;
    }

    new Promise(
        function(resolve, reject) {
            const err = _NJSFileSystem.writeFileSync(path, options) ? true : null;
            resolve(err);
        }
    ).then(err => callback(err));
}

_NJSFileSystem.appendFileSync = function(path, data, encoding = 'utf8') {
    return _NJSFileSystemInterface.appendFileSync(path, data, encoding);
}

_NJSFileSystem.appendFile = function(...args) {
    let path, data, options, callback;
    if(args.length == 3) {
        [path, data, callback] = args;
    } else {
        [path, data, options, callback] = args;
    }

    new Promise(
        function(resolve, reject) {
            const err = _NJSFileSystem.appendFileSync(path, options) ? true : null;
            resolve(err);
        }
    ).then(err => callback(err));
}

_NJSFileSystem.copyFileSync = function(src, dest, mode = 0) {
    return _NJSFileSystemInterface.copyFileSync(src, dest, mode);
}

_NJSFileSystem.copyFile = function(...args) {
    let src, dest, mode, callback;
    if(args.length == 3) {
        [src, dest, mode] = args;
    } else {
        [src, dest, mode, callback] = args;
    }

    new Promise(
        function(resolve, reject) {
            const err = _NJSFileSystem.copyFileSync(src, dest, mode) ? true : null;
            resolve(err);
        }
    ).then(err => callback(err));
}

require = function(res) {
    const __njsinterface_list = {
        'path': _NJSPath || {},
        'fs': _NJSFileSystem || {}
    };

    if(__njsinterface_list[res]) {
        return __njsinterface_list[res];
    } else {
        let xhr = new XMLHttpRequest();
        let exportValue = {};
        xhr.open('GET', res, false);
        xhr.send();

        try {
            if(xhr.status == 200) {
                console.log(xhr.responseText);
                const evalCode = `(function(){ let module = { 'exports': {} }; {${xhr.responseText}} return module.exports; })();`;
                exportValue = eval(evalCode);
            }
        } finally {
            return exportValue;
        }
    }
}