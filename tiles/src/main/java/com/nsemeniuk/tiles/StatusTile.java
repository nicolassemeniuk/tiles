package com.nsemeniuk.tiles;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringDef;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by nandressemeniuk on 3/20/17.
 */

public class StatusTile extends FrameLayout {

  @Retention(RetentionPolicy.SOURCE)
  @StringDef({STATUS_ENABLED, STATUS_DISABLED, STATUS_COMPLETED, STATUS_COMPLETED_DISABLED})
  public @interface TileType {
  }

  public final static String STATUS_ENABLED = "enabled";
  public final static String STATUS_DISABLED = "disabled";
  public final static String STATUS_COMPLETED = "completed";
  public final static String STATUS_COMPLETED_DISABLED = "completedDisabled";

  private TextView mStepTitleTextView;
  private TextView mTileTitleTextView;
  private TextView mContentTextView;
  private ImageView mActionImageView;
  private FrameLayout mMainContainerDisabledFrameLayout;

  @TileType
  String mType;

  public StatusTile(@NonNull Context context) {
    super(context);
    init(null);
  }

  public StatusTile(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public StatusTile(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  @RequiresApi(21)
  public StatusTile(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(attrs);
  }

  private void init(@Nullable AttributeSet attrs) {
    inflate(getContext(), R.layout.status_tile, this);

    mStepTitleTextView = (TextView) findViewById(R.id.step_title_text_view);
    mTileTitleTextView = (TextView) findViewById(R.id.tile_title_text_view);
    mContentTextView = (TextView) findViewById(R.id.content_text_view);
    mActionImageView = (ImageView) findViewById(R.id.action_image_view);
    mMainContainerDisabledFrameLayout = (FrameLayout) findViewById(R.id.main_container_disabled_frame_layout);

    TypedArray a = getContext().getTheme().obtainStyledAttributes(
        attrs,
        R.styleable.StatusTile,
        0, 0);

    try {
      mStepTitleTextView.setText(a.getString(R.styleable.StatusTile_stepText));
      mTileTitleTextView.setText(a.getString(R.styleable.StatusTile_titleText));
      mContentTextView.setText(a.getString(R.styleable.StatusTile_contentText));
      String type = a.getString(R.styleable.StatusTile_type);

      if(type != null) {
        switch(type) {
          case STATUS_COMPLETED:
            setType(STATUS_COMPLETED);
            break;
          case STATUS_COMPLETED_DISABLED:
            setType(STATUS_COMPLETED_DISABLED);
            break;
          case STATUS_DISABLED:
            setType(STATUS_DISABLED);
            break;
          case STATUS_ENABLED:
            setType(STATUS_ENABLED);
            break;
          default:
            setType(STATUS_DISABLED);
            break;
        }
      }

    } finally {
      a.recycle();
    }

  }

  /**
   * Sets the visuals based on a statusType for this tail.
   *
   * @param statusType: 1 - enabled: gray step, pencil.
   *                    2 - disabled: grayed out the tile, gray pencil, gray step.
   *                    3 - completed: gray step, green check.
   *                    4 - completedDisabled: grayed out the tile, green check, gray title.
   */


  public void setType(@TileType String statusType) {
    if(statusType != null) {
      Drawable drawable;

      switch(statusType) {
        case STATUS_ENABLED:
          mType = STATUS_ENABLED;
          mMainContainerDisabledFrameLayout.setVisibility(View.GONE);
          mStepTitleTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
          drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_edit_black);
          break;
        case STATUS_DISABLED:
          mType = STATUS_DISABLED;
          mStepTitleTextView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));
          mMainContainerDisabledFrameLayout.setVisibility(View.VISIBLE);
          drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_edit_black);
          drawable.setColorFilter(ContextCompat.getColor(getContext(), android.R.color.darker_gray), PorterDuff.Mode.SRC_ATOP);
          break;
        case STATUS_COMPLETED:
          mType = STATUS_COMPLETED;
          mStepTitleTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
          mMainContainerDisabledFrameLayout.setVisibility(View.GONE);
          drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_check_black);
          drawable.setColorFilter(ContextCompat.getColor(getContext(), android.R.color.holo_green_dark), PorterDuff.Mode.SRC_ATOP);
          break;
        case STATUS_COMPLETED_DISABLED:
          mType = STATUS_COMPLETED_DISABLED;
          mStepTitleTextView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));
          mMainContainerDisabledFrameLayout.setVisibility(View.VISIBLE);
          drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_check_black);
          drawable.setColorFilter(ContextCompat.getColor(getContext(), android.R.color.darker_gray), PorterDuff.Mode.SRC_ATOP);
          break;
        default:
          //Default view
          mType = STATUS_ENABLED;
          mMainContainerDisabledFrameLayout.setVisibility(View.GONE);
          mStepTitleTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
          drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_edit_black);
          break;
      }
      //Set the drawable
      mActionImageView.setImageDrawable(drawable);
    } else {
      //Default view

      Drawable drawable;
      mMainContainerDisabledFrameLayout.setVisibility(View.GONE);
      mStepTitleTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
      drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_edit_black);

      //Set the drawable
      mActionImageView.setImageDrawable(drawable);
    }
  }

  @TileType
  public String getType() {
    return mType;
  }

  /**
   * Set the text for step, title and content.
   *
   * @param step    int resource representing the text for the step of the tile.
   * @param title   int resource the text for the title of the tile.
   * @param content int resource the text for the content of the tile.
   */
  public void setText(@StringRes int step, @StringRes int title, @StringRes int content) {
    setStep(step);
    setTitle(title);
    setContent(content);
  }

  /**
   * Set the step of the tile.
   *
   * @param step int resource representing the text for the step of the tile.
   */
  public void setStep(@StringRes int step) {
    mStepTitleTextView.setText(step);
  }

  /**
   * Set the step of the tile.
   *
   * @param step a string representing the step of the tile.
   */
  public void setStep(@NonNull String step) {
    mStepTitleTextView.setText(step);
  }

  /**
   * Set the title of the tile.
   *
   * @param title int resource the text for the title of the tile.
   */
  public void setTitle(@StringRes int title) {
    mTileTitleTextView.setText(title);
  }

  /**
   * Set the title of the tile.
   *
   * @param title a string representing the title of the tile.
   */
  public void setTitle(@NonNull String title) {
    mTileTitleTextView.setText(title);
  }

  /**
   * Set the content of the tile.
   *
   * @param content int resource the text for the content of the tile.
   */
  public void setContent(@StringRes int content) {
    mContentTextView.setText(content);
  }

  /**
   * Set the content of the tile.
   *
   * @param content a string representing the content of the tile.
   */
  public void setContent(@NonNull String content) {
    mContentTextView.setText(content);
  }


}
