package io.github.scalrx;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import io.github.scalrx.utilities.Permissions;

public class AndroidLauncher extends AndroidApplication implements Permissions
{
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new KnyttStories(this), config);
	}

	/* Implementation to determine if the permissions are enabled */
	@Override
	public boolean isReadPermissionEnabled(){
		int currSDKVersion = Build.VERSION.SDK_INT;
		// Lollipop-specific check
		if(currSDKVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
			// Get the return code for if we have the permission
			int permissionResult = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
			if(permissionResult != PackageManager.PERMISSION_GRANTED) {
				requestReadPermission();
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isWritePermissionEnabled() {
		int currSDKVersion = Build.VERSION.SDK_INT;
		// Lollipop-specific check
		if(currSDKVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
			// Get the return code for if we have the permission
			int permissionResult = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
			if(permissionResult != PackageManager.PERMISSION_GRANTED) {
				requestWritePermission();
			} else {
				return true;
			}
		}
		return false;
	}

	/* Helper-method Request functions */
	private void requestReadPermission() {
		int currSDKVersion = Build.VERSION.SDK_INT;
		// Lollipop-specific check
		if(currSDKVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
			requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
		}
	}

	private void requestWritePermission() {
		int currSDKVersion = Build.VERSION.SDK_INT;
		// Lollipop-specific check
		if(currSDKVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
			requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
		}
	}
}
