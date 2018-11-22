package com.aosdp.launcher.icons;

import android.content.Context;
import android.content.pm.LauncherActivityInfo;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.android.launcher3.Utilities;
import com.android.launcher3.graphics.LauncherIcons;

public class IconProvider {

    private Context mContext;
    private IconPackHelper mIconPackHelper;
    private String mCurrentIconPack;

    public IconProvider(Context context) {
        mContext = context;
        mIconPackHelper = new IconPackHelper(context);
        loadIconPack();
    }

    public Drawable getIcon(LauncherActivityInfo info, int iconDpi) {
        if (!mIconPackHelper.isIconPackLoaded()) {
            return info.getIcon(iconDpi);
        }
        int resourceId = mIconPackHelper.getResourceIdForActivityIcon(info);
        if (resourceId != 0) {
            try {
                return mIconPackHelper.getIconPackResources().getDrawableForDensity(resourceId, iconDpi, mContext.getTheme());
            } catch (Exception e) {
                return getIconBitmap(info, iconDpi);
            }
        } else {
            return getIconBitmap(info, iconDpi);
        }
    }

    private Drawable getIconBitmap(LauncherActivityInfo info, int iconDpi) {
        LauncherIcons li = LauncherIcons.obtain(mContext);
        Drawable d = new BitmapDrawable(mContext.getResources(), li.createIconBitmap(info.getIcon(iconDpi), mContext, mIconPackHelper));
        li.recycle();
        return d;
    }

    public void loadIconPack() {
        if (mIconPackHelper == null) return;
        mIconPackHelper.unloadIconPack();
        mCurrentIconPack = Utilities.getPrefs(mContext).getString("icon_pack", "default");
        if (!mCurrentIconPack.equals("default") &&
                !mIconPackHelper.loadIconPack(mCurrentIconPack)) {
            Utilities.getPrefs(mContext).edit().putString("icon_pack", "").apply();
        }
    }
}
