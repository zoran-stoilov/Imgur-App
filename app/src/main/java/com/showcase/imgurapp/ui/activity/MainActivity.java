package com.showcase.imgurapp.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.showcase.imgurapp.R;
import com.showcase.imgurapp.ui.fragment.FragmentHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            FragmentHelper.setMainFragment(this, R.id.fragment_container);
        }
        showDialog();
    }

    /**
     * Display dummy ProgressDialog for 3 seconds.
     */
    private void showDialog() {
        final ProgressDialog dialog = ProgressDialog.show(this,
                getString(R.string.please_wait), getString(R.string.loading_data), true);
        dialog.setCancelable(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 3000); // dismiss dialog in 3s
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_download) {
            startActivity(new Intent(this, DownloadActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
