package dmostek.cz.library;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

import java.util.prefs.Preferences;

/**
 * Created by mostek on 5.2.2015.
 */
public class EmailAcraSender implements ReportSender {

    private final Context context;

    public EmailAcraSender(Context applicationContext) {
        this.context = applicationContext;
    }

    @Override
    public void send(CrashReportData errorContent) throws ReportSenderException {
        try {
            final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.crash_email_subject));
            emailIntent.putExtra(Intent.EXTRA_TEXT, errorContent.toJSON().toString());
            String[] recipients =  new String[] {"mostekdominik@gmail.com"};
            emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
            context.startActivity(emailIntent);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO
        }
    }
}
