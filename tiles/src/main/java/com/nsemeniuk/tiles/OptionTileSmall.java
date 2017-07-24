package com.nsemeniuk.tiles;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by nandressemeniuk on 5/22/17.
 */

public class OptionTileSmall extends FrameLayout {

  private TextView mTitleTextView;
  private ImageView mRightArrowImageView;

  public OptionTileSmall(@NonNull Context context) {
    super(context);
    init(null);
  }

  public OptionTileSmall(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public OptionTileSmall(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  @RequiresApi(21)
  public OptionTileSmall(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(attrs);
  }

  private void init(@Nullable AttributeSet attrs) {
    inflate(getContext(), R.layout.option_tile_small, this);

    mTitleTextView = (TextView) findViewById(R.id.tile_text_view);
    mRightArrowImageView = (ImageView) findViewById(R.id.right_arrow_image_view);

    TypedArray a = getContext().getTheme().obtainStyledAttributes(
        attrs,
        R.styleable.OptionTileSmall,
        0, 0);

    try {

      mTitleTextView.setText(a.getString(R.styleable.OptionTileSmall_optionSmallTitleText));

    } finally {
      a.recycle();
    }
  }

  public void setTitle(@StringRes int title){
    mTitleTextView.setText(title);
  }

  public void setTitle(@NonNull String title){
    mTitleTextView.setText(title);
  }

  public void setArrowDown(){
    mRightArrowImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_chevron_right_black));
  }

  public void setArrowRight(){
    mRightArrowImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_chevron_right_black));
  }

}
