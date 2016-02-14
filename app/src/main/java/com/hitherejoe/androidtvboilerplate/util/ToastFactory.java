package com.hitherejoe.androidtvboilerplate.util;


import android.content.Context;
import android.widget.Toast;

import com.hitherejoe.androidtvboilerplate.R;

public class ToastFactory {

    public static Toast createWifiErrorToast(Context context) {
        return Toast.makeText(
                context,
                context.getString(R.string.error_message_network_needed),
                Toast.LENGTH_SHORT);
    }

}