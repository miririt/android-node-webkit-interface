(function() {
    StorageManager.isLocalMode = () => true;
    Utils.isMobileDevice = () => true;
    Utils.isAndroidChrome = () => true;
    Utils.isNwjs = () => true;

    Storage.prototype.getItem = function(key){
        return _StorageInterface.getItem(key) || null;
    };

    Storage.prototype.setItem = function(key, value) {
        _StorageInterface.setItem(key,value);
    };

    Storage.prototype.removeItem = function(key){
    	_StorageInterface.removeItem(key);
    };

})();