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
 * Created by nandressemeniuk on 3/29/17.
 */

public class OptionTile extends FrameLayout {

  private TextView mTileTitleTextView;
  private TextView mContentTextView;
  private ImageView mRightArrowImageView;

  public OptionTile(@NonNull Context context) {
    super(context);
    init(null);
  }

  public OptionTile(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public OptionTile(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  @RequiresApi(21)
  public OptionTile(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(attrs);
  }

  private void init(@Nullable AttributeSet attrs) {
    inflate(getContext(), R.layout.option_tile, this);

    mTileTitleTextView = findViewById(R.id.tile_title_text_view);
    mContentTextView = findViewById(R.id.content_text_view);
    mRightArrowImageView = findViewById(R.id.right_arrow_image_view);

    TypedArray a = getContext().getTheme().obtainStyledAttributes(
        attrs,
        R.styleable.OptionTile,
        0, 0);

    try {

      mTileTitleTextView.setText(a.getString(R.styleable.OptionTile_optionTitleText));
      mContentTextView.setText(a.getString(R.styleable.OptionTile_optionContentText));

    } finally {
      a.recycle();
    }
  }

  public void setTitle(@StringRes int title){
    mTileTitleTextView.setText(title);
  }

  public void setTitle(@NonNull String title){
    mTileTitleTextView.setText(title);
  }

  public void setContent(@StringRes int content){
    mContentTextView.setText(content);
  }

  public void setContent(@NonNull String content){
    mContentTextView.setText(content);
  }

  public void setArrowDown(){
    mRightArrowImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_chevron_right_black));
  }

  public void setArrowRight(){
    mRightArrowImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_chevron_right_black));
  }
}
