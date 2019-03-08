package com.appbell.iraisefund4u.common.service;

import android.util.Log;

import com.appbell.iraisefund4u.common.util.AndroidAppUtil;
import com.appbell.iraisefund4u.resto.app.service.sync.RestoAppSyncProcessor;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class InstaFndrFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = InstaFndrFirebaseMessagingService.class.getSimpleName() + ": ";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if(!AndroidAppUtil.isUserLoggedIn(getApplicationContext())){
            return;
        }
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            new RestoAppSyncProcessor(getApplicationContext()).processRequest(remoteMessage.getData());
        }

    }



    @Override
    public void onNewToken(String token) {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //To displaying token on logcat
        Log.d("TOKEN: ", refreshedToken);
    }
}
