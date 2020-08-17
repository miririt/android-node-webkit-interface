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

require = function(res) {
    const __njsinterface_list = {
        'path': _NJSPath || {}
    };

    if(__njsinterface_list[res]) {
        return __njsinterface_list[res];
    } else {
        let xhr = new XMLHttpRequest();
        xhr.open('GET', `${res}`, false);
        if(xhr.status == 200) {
            const evalCode = `(function(){ let module = { 'exports': {} }; {${xhr.responseText}} return module.exports; })();`;
            return eval(evalCode);
        }
        else return {};
    }
}