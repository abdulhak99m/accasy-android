package com.starglare.accasy.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;

import com.starglare.accasy.R;
import com.starglare.accasy.core.Helper;
import com.starglare.accasy.core.Logger;
import com.starglare.accasy.models.ReportModel;
import com.starglare.accasy.services.SyncService;

public class NetworkBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(getClass().getSimpleName(),"Network state changed");

        if(Helper.hasActiveInternetConnection(context)) {
            intent = new Intent(Intent.ACTION_SYNC,null,context,SyncService.class);
            context.startService(intent);
            Log.i(getClass().getSimpleName(),"Service started");
        }
    }
}
