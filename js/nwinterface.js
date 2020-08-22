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

function NWInterface() {
    function _NWProcess() { }

    _NWProcess.mainModule = {
        'filename': 'index.html'
    };

    _NWProcess.versions = {
        'node': '14.8.0',
        'chromium': '84.0.4147.135'
    };

    return {
        'process': _NWProcess,
        'nw.gui': nw
    };
}