package cordova.plugin.usbpermission;

import org.apache.cordova.CordovaPlugin;

import javax.naming.Context;

import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class UsbPermission extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("requestpermission")) {
            this.coolMethod(args, callbackContext);
            return true;
        }
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private void requestpermission(JSONArray args, CallbackContext callback) {
        String message = "Working Plugin";
        Boolean status = printUsb();

        callbackContext.success(message);
    }

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbManager usbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
                    UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (usbManager != null && usbDevice != null) {
                            // YOUR PRINT CODE HERE
                            Log.e("Tesing_Module", "Working");
                        }
                    }
                }
            }
        }
    };

    public boolean printUsb() {
        UsbConnection usbConnection = UsbPrintersConnections.selectFirstConnected(getContext());
        UsbManager usbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);

        if (usbConnection != null && usbManager != null) {
            PendingIntent permissionIntent = PendingIntent.getBroadcast(
                    getContext(),
                    0,
                    new Intent(ACTION_USB_PERMISSION),
                    android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE
                            : 0);
            IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
            getActivity().registerReceiver(usbReceiver, filter);

            // Request permission
            usbManager.requestPermission(((UsbConnection) usbConnection).getDevice(), permissionIntent);
            return true;
        }

        // Return a default value or indicate that the permission result is pending
        return false;
    }
}
