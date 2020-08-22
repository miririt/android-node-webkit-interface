(function() {
    window.require = function(res) {
        const __njsinterface_list = NJSInterface();
        const __nwinterface_list = NWInterface();

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
                    console.log(xhr.responseText);
                    const evalCode = `(function(){ let module = { 'exports': {} }; {${xhr.responseText}} return module.exports; })();`;
                    exportValue = eval(evalCode);
                }
            } finally {
                return exportValue;
            }
        }
    }
})();