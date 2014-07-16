package com.cloudernew.passgen;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class PassGen extends ActionBarActivity {
    private EditText etPasskey;
    private EditText etIdentifier;
    private EditText etGenerated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_gen);
        etPasskey = (EditText) findViewById(R.id.etPasskey);
        etIdentifier = (EditText) findViewById(R.id.etIdentifier);
        etGenerated = (EditText) findViewById(R.id.etGenerated);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pass_gen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        etPasskey.setText("");
        etIdentifier.setText("");
        etGenerated.setText("");
    }

    public void generatePassword(View v) throws NoSuchAlgorithmException {
        MessageDigest mdSha1 = MessageDigest.getInstance("SHA1");

        mdSha1.update((etPasskey.getText().toString() + ":" + etIdentifier.getText().toString()).getBytes());
        byte[] data = mdSha1.digest();
        String sb = Base64.encodeToString(data, 0, data.length, 0);

        etGenerated.setText(sb.substring(0, 8));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager cb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            cb.setPrimaryClip(ClipData.newPlainText("PassGen", etGenerated.getText().toString()));
        } else {
            android.text.ClipboardManager cb = (android.text.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            cb.setText(etGenerated.getText().toString());
        }
    }
}
