package com.nsemeniuk.tiles.utils;

import android.content.Context;
import android.os.Build;
import android.util.TypedValue;

/**
 * Created by nandressemeniuk on 7/24/17.
 */

public class VisualUtils {

  public static int getThemeAccentColor(Context context) {
    int colorAttr;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      colorAttr = android.R.attr.colorAccent;
    } else {
      //Get colorAccent defined for AppCompat
      colorAttr = context.getResources().getIdentifier("colorAccent", "attr", context.getPackageName());
    }
    TypedValue outValue = new TypedValue();
    context.getTheme().resolveAttribute(colorAttr, outValue, true);
    return outValue.data;
  }
}
