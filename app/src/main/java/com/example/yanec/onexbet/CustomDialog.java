package com.example.yanec.onexbet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class CustomDialog extends Dialog implements  android.view.View.OnClickListener{

        public Activity c;
        public Dialog d;
        public Button yes, no;

        public CustomDialog(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_custom_dialog);
            yes = (Button) findViewById(R.id.btnYes);
            no = (Button) findViewById(R.id.btnNo);
            yes.setOnClickListener(this);
            no.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnYes:
                    String packageNamefavorite = "com.betinvest.favorit_sport_com_ua";
                    Intent launchIntent = c.getPackageManager().getLaunchIntentForPackage(packageNamefavorite);
                    if (launchIntent != null) {
                        c.startActivity(launchIntent);//null pointer check in case package name was not found
                    }
                    c.finish();
                    break;
                case R.id.btnNo:
                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }

}
