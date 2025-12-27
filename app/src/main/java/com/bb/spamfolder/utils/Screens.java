package com.bb.spamfolder.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.bb.spamfolder.R;

public class Screens {

    public static void showClearTopScreen(final Context context, final Class<?> cls) {
        final Intent intent = new Intent(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void showCustomScreen(final Context context, final Class<?> cls) {
        try {
            final Intent intent = new Intent(context, cls);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }
}