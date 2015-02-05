package dmostek.cz.library;

import android.app.Application;
import android.content.res.Configuration;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(formKey = "", // will not be used
        applicationLogFileLines = 1000,
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)
public class LibraryApplication extends Application {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);
        ACRA.getErrorReporter().setReportSender(new EmailAcraSender(this.getApplicationContext()));
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
