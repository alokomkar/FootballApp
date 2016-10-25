package app.football.com.footballapp;

import android.app.Application;
import android.util.Log;

import com.helpshift.All;
import com.helpshift.Core;
import com.helpshift.InstallConfig;
import com.helpshift.exceptions.InstallException;

/**
 * Created by cognitive on 10/24/16.
 */
public class FBApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initHelpShift();
    }

    private void initHelpShift() {
        InstallConfig installConfig = new InstallConfig.Builder()
                .setEnableInAppNotification(true)
                .build();
        Core.init(All.getInstance());
        try {
            Core.install(this,
                    getString(R.string.help_shift_app_key),
                    getString(R.string.help_shift_domain),
                    getString(R.string.help_shift_app_id),
                    installConfig);
        } catch (InstallException e) {
            Log.e("FBApplication", "invalid install credentials : ", e);
        }
    }
}
