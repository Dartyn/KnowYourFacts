package sg.edu.rp.webservices.knowyourfacts;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    int requestCode = 123;
    int notificationID = 888;
    ArrayList<Fragment> al;
    MyFragmentPagerAdapter adapter;
    ViewPager vPager;
    Button btnreadlate;
    AlarmManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vPager = (ViewPager) findViewById(R.id.viewpager1);
        btnreadlate = (Button)findViewById(R.id.button);

        FragmentManager fm = getSupportFragmentManager();

        al = new ArrayList<Fragment>();
        al.add(new Frag1());
        al.add(new Frag2());
        al.add(new Frag3());


        adapter = new MyFragmentPagerAdapter(fm,al);
        vPager.setAdapter(adapter);
        btnreadlate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, 5);
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(MainActivity.this,requestCode,intent ,PendingIntent.FLAG_CANCEL_CURRENT);
                am = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pi);
                NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    NotificationChannel channel = new NotificationChannel("default","Default Channel",NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setDescription("This is for default notification");
                    notificationManager.createNotificationChannel(channel);
                }

                PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "default");
                builder.setContentTitle("Reminder");
                builder.setContentText("Don't forget to view!");
                builder.setSmallIcon(android.R.drawable.btn_star_big_off);
                builder.setContentIntent(pIntent);
                builder.setAutoCancel(true);

                Uri uri= RingtoneManager.getDefaultUri
                        (RingtoneManager.TYPE_NOTIFICATION);
                builder.setSound(uri);

                builder.setPriority(Notification.PRIORITY_HIGH);



                Notification n = builder.build();

                notificationManager.notify(notificationID, n);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_next) {
            int max = vPager.getChildCount();
            if (vPager.getCurrentItem() < max-1){
                int nextPage = vPager.getCurrentItem() + 1;
                vPager.setCurrentItem(nextPage, true);
            }
            return true;
        }else if(id == R.id.action_previous){
            if (vPager.getCurrentItem() > 0){
                int previousPage = vPager.getCurrentItem() - 1;
                vPager.setCurrentItem(previousPage, true);
            }
            return  true;
        }else {
            Random r = new Random();
            int randomPage = r.nextInt(2);
            vPager.setCurrentItem(randomPage, true);
        }

        return super.onOptionsItemSelected(item);
    }


}
