package com.digital.signature;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    private static final int SIGN_REQUEST_CODE=10;
    public static final String KEY_SIGN = "signature";
    private String imgBase64="";
    private ImageView mImageView;
    private EditText mEdtxtName;
    private EditText mEdtxtEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView=(ImageView)findViewById(R.id.imageView);
        mEdtxtName=(EditText)findViewById(R.id.edtxt_name);
        mEdtxtEmail=(EditText)findViewById(R.id.edtxt_email);
    }




    public void onSignature(View v){
        imgBase64="";
        Intent mIntent = new Intent(this, YourSignActivity.class);
        startActivityForResult(mIntent, 10);
    }


    public void onSubmit(View v){
        if(isValid()) {

            if (imgBase64.equals("")) {

                Toast.makeText(this, "NO Signature", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Do your work now ", Toast.LENGTH_LONG).show();
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imgBase64 = data.getStringExtra(KEY_SIGN);
            // Print log to check base 64 string
            //Log.i("Sign Base64","Sign Base64 : "+imgBase64);
            mImageView.setImageBitmap(getBitMap(imgBase64));
        }
    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap getBitMap(String encodedString){
        try{
            byte[] decodedString = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    private boolean isValid() {

        if (mEdtxtName.getText().toString().trim().equals("")) {
            showAlertMessage(getString(R.string.error),getString(R.string.err_name));
            mEdtxtName.requestFocus();
            return false;
        }

        String strEmail = mEdtxtEmail.getText().toString().trim().trim();
        if (strEmail.equals("")) {
            showAlertMessage(getString(R.string.error),getString(R.string.err_email));
            mEdtxtEmail.requestFocus();
            return false;
        }
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = strEmail;
        // Make the comparison case-insensitive.
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        boolean matchFound = matcher.matches();
        if (!matchFound) {
            showAlertMessage(getString(R.string.error),getString(R.string.err_email));
            mEdtxtEmail.requestFocus();
            return false;
        }

        return true;
    }

    private void showAlertMessage(String titile, String content) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        if (titile != null && !titile.equals(""))
            alert.setTitle(titile);
        alert.setMessage(content);
        alert.setPositiveButton(android.R.string.ok, null);
        alert.show();
    }
}
