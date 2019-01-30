package com.example.yanec.onexbet.IfAppInstalled;

import android.content.pm.PackageManager;

public class CheckIfAppInstalled {
    public boolean isPackageInstalled(String packageName, PackageManager packageManager) {

        boolean found = true;

        try {

            packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {

            found = false;
        }

        return found;
    }


}
