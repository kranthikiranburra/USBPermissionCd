var exec = require('cordova/exec');

module.exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'UsbPermission', 'coolMethod', [arg0]);
};

module.exports.requestpermission = function(arg0, success, error){
    exec(success,error, 'UsbPermission', 'requestpermission', [arg0]);
}