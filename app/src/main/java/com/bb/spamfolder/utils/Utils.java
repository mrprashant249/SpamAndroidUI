package com.bb.spamfolder.utils;

import android.content.Context;
import android.util.Log;

import com.bb.spamfolder.R;
import com.bb.spamfolder.models.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {

    public static void getErrors(final Exception e) {
        final String stackTrace = Log.getStackTraceString(e);
        Utils.sout(stackTrace);
    }

    public static void sout(final String msg) {
        if (Constant.IS_TRIAL) {
            System.out.println("Prashant :: " + msg);
        }
    }

    public static List<Order> getDeliveryData(Context context) {
        List<Order> deliveries = new ArrayList<>();

        // Sample data matching screenshot
        deliveries.add(new Order(
                context.getString(R.string.address_1725),
                context.getString(R.string._6529_22),
                context.getString(R.string._123456890),
                context.getString(R.string.in_progress),
                context.getString(R.string.eta_2_hours_43_minutes),
                null
        ));

        deliveries.add(new Order(
                context.getString(R.string.address_1725),
                context.getString(R.string._6529_22),
                context.getString(R.string._123456890),
                context.getString(R.string.pending),
                context.getString(R.string.eta_2_hours_43_minutes),
                null
        ));

        deliveries.add(new Order(
                context.getString(R.string.address_1725),
                context.getString(R.string._6529_22),
                context.getString(R.string._123456890),
                context.getString(R.string.cancelled),
                null,
                context.getString(R.string.cancelled_by_driver)
        ));

        deliveries.add(new Order(
                context.getString(R.string.address_1725),
                context.getString(R.string._6529_22),
                context.getString(R.string._123456890),
                context.getString(R.string.delivered),
                null,
                context.getString(R.string.delivered_9_jan)
        ));

        deliveries.add(new Order(
                context.getString(R.string.address_1725),
                context.getString(R.string._6529_22),
                context.getString(R.string._123456890),
                context.getString(R.string.delivered),
                null,
                context.getString(R.string.delivered_9_jan)
        ));

        deliveries.add(new Order(
                context.getString(R.string.address_1725),
                context.getString(R.string._6529_22),
                context.getString(R.string._123456890),
                context.getString(R.string.delivered),
                null,
                context.getString(R.string.delivered_9_jan)
        ));
        return deliveries;
    }

    public static List<Order> getOrderDetailsData(Context context) {
        List<Order> deliveries = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            deliveries.add(new Order(
                    context.getString(R.string.address_1725),
                    context.getString(R.string._6529_22),
                    context.getString(R.string._123456890),
                    context.getString(R.string.pending),
                    context.getString(R.string.eta_2_hours_43_minutes),
                    null
            ));
        }
        return deliveries;
    }

    public static int getRandomAvatar(boolean isMale) {
        int[] maleAvatars = {
                R.drawable.profile_male_1,
                R.drawable.profile_male_2,
                R.drawable.profile_male_3,
                R.drawable.profile_male_4,
                R.drawable.profile_male_5
        };

        int[] femaleAvatars = {
                R.drawable.profile_female_1,
                R.drawable.profile_female_2,
                R.drawable.profile_female_3,
                R.drawable.profile_female_4,
                R.drawable.profile_female_5
        };

        Random random = new Random();
        if (isMale) {
            return maleAvatars[random.nextInt(maleAvatars.length)];
        } else {
            return femaleAvatars[random.nextInt(femaleAvatars.length)];
        }
    }
}