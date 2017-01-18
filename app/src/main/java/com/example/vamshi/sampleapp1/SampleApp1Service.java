//package com.example.vamshi.sampleapp1;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Message;
//import android.os.Messenger;
//import android.util.Log;
//
///**
// * Created by vamshi on 12-01-2017.
// */
//
//public class SampleApp1Service extends Service {
//
//    private static final String TAG = "ComWithIoMcu";
//    /** Messenger for communicating with service. */
//    static Messenger mServiceMcu = null;
//    /** Flag indicating whether we have called bind on the service. */
//    boolean mIsBound;
//
//    /**
//     * Command to the service to register a client, receiving callbacks
//     * from the service.  The Message's replyTo field must be a Messenger of
//     * the client where callbacks should be sent.
//     */
//    static final int MSG_REGISTER_CLIENT = 1;
//
//    /**
//     * Command to the service to unregister a client, ot stop receiving callbacks
//     * from the service.  The Message's replyTo field must be a Messenger of
//     * the client as previously given with MSG_REGISTER_CLIENT.
//     */
//    static final int MSG_UNREGISTER_CLIENT = 2;
//    /**
//     * Command to forward a string command to the I/O MCU
//     */
//    public static final int MSG_SEND_STRING_TO_IOMCU = 3;
///** List of supported commands
// *
// */
//
//    /**
//     * Handler of incoming messages from service.
//     */
//    class IncomingHandler extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case MSG_UNSOL_MESSAGE:
//                    Log.d(TAG, "Received from service: " + msg.arg1);
//                    break;
//                default:
//                    super.handleMessage(msg);
//            }
//        }
//    }
//
//    /**
//     * Target we publish for clients to send messages to IncomingHandler.
//     */
//    final Messenger mMessenger = new Messenger(new IncomingHandler());
//    boolean mBound;
//
//    /**
//     * Class for interacting with the main interface of the service.
//     */
//    private ServiceConnection mConnection = new ServiceConnection() {
//        public void onServiceConnected(ComponentName className,
//                                       IBinder service) {
//            // This is called when the connection with the service has been
//            // established, giving us the service object we can use to
//            // interact with the service.  We are communicating with our
//            // service through an IDL interface, so get a client-side
//            // representation of that from the raw service object.
//            mServiceMcu = new Messenger(service);
//            Log.d(TAG, "Attached.");
//
//            // We want to monitor the service for as long as we are
//            // connected to it.
//            try {
//                Message msg = Message.obtain(null,
//                        MSG_REGISTER_CLIENT);
//                msg.replyTo = mMessenger;
//                mServiceMcu.send(msg);
//
//            } catch (RemoteException e) {
//                // In this case the service has crashed before we could even
//                // do anything with it; we can count on soon being
//                // disconnected (and then reconnected if it can be restarted)
//                // so there is no need to do anything here.
//                Log.e(TAG, "ModemWatcherService is not running");
//            }
//        }
//
//        public void onServiceDisconnected(ComponentName className) {
//            // This is called when the connection with the service has been
//            // unexpectedly disconnected -- that is, its process crashed.
//            mServiceMcu = null;
//            mBound = false;
//
//
//        }
//    };
//
//    void doBindService() {
//        // Establish a connection with the service.  We use an explicit
//        // class name because there is no reason to be able to let other
//        // applications replace our component.
//        //bindService(new Intent(this, MessengerService.class), mConnection, Context.BIND_AUTO_CREATE);
//        try {
//            Intent intentForMcuService = new Intent();
//            Log.d(TAG, "Before init intent.componentName");
//            intentForMcuService.setComponent(new ComponentName("com.admetric.modemwatcher", "ModemWatcherService"));
//            Log.d(TAG, "Before bindService");
//            if (bindService(intentForMcuService, mConnection, 0)){
//                Log.d(TAG, "Binding to Modem Watcher returned true");
//            } else {
//                Log.d(TAG, "Binding to Modem Watcher returned false");
//            }
//        } catch (SecurityException e) {
//            Log.e(TAG, "can't bind to ModemWatcherService, check permission in Manifest");
//        }
//        mIsBound = true;
//        Log.d(TAG, "Binding.");
//    }
//
//    void doUnbindService() {
//        if (mIsBound) {
//            // If we have received the service, and hence registered with
//            // it, then now is the time to unregister.
//            if (mServiceMcu != null) {
//                try {
//                    Message msg = Message.obtain(null, MSG_UNREGISTER_CLIENT);
//                    msg.replyTo = mMessenger;
//                    mServiceMcu.send(msg);
//                } catch (RemoteException e) {
//                    // There is nothing special we need to do if the service
//                    // has crashed.
//                }
//            }
//
//            // Detach our existing connection.
//            unbindService(mConnection);
//            mIsBound = false;
//            Log.d(TAG, "Unbinding.");
//        }
//
//}
