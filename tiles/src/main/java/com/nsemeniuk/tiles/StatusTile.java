package com.nsemeniuk.tiles;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
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


import com.nsemeniuk.tiles.utils.VisualUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The status tile provides a view that has a:
 * - Step Title
 * - Title
 * - Content
 * - Icon
 *
 * It also has states with specific attributes and icons that can be configured:
 * - Enabled
 * - Disabled
 * - Completed
 * - Completed disabled
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

  //Views
  private TextView mStepTextView;
  private TextView mTitleTextView;
  private TextView mContentTextView;
  private ImageView mActionImageView;
  private FrameLayout mMainContainerDisabledFrameLayout;
  private View mDividerView;

  //Colors
  private int mStepTextColor;
  private int mTitleTextColor;
  private int mContentTextColor;
  private int mStepTextColorDisabled;
  private int mTitleTextColorDisabled;
  private int mContentTextColorDisabled;

  //States
  private boolean mShowDividerView;

  //Drawables
  private int mEnabledIcon;
  private int mDisabledIcon;
  private int mCompletedIcon;
  private int mCompletedDisabledIcon;


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

    mStepTextView = (TextView) findViewById(R.id.step_title_text_view);
    mTitleTextView = (TextView) findViewById(R.id.tile_title_text_view);
    mContentTextView = (TextView) findViewById(R.id.content_text_view);
    mActionImageView = (ImageView) findViewById(R.id.action_image_view);
    mMainContainerDisabledFrameLayout = (FrameLayout) findViewById(R.id.main_container_disabled_frame_layout);
    mDividerView = findViewById(R.id.divider_view);

    TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(
        attrs,
        R.styleable.StatusTile,
        0, 0);

    try {
      mStepTextView.setText(typedArray.getString(R.styleable.StatusTile_statusTile_stepText));
      mTitleTextView.setText(typedArray.getString(R.styleable.StatusTile_statusTile_titleText));
      mContentTextView.setText(typedArray.getString(R.styleable.StatusTile_statusTile_contentText));
      mStepTextColor = typedArray.getColor(R.styleable.StatusTile_statusTile_stepTextColor, VisualUtils.getThemeAccentColor(getContext()));
      mTitleTextColor = typedArray.getColor(R.styleable.StatusTile_statusTile_titleTextColor, ContextCompat.getColor(getContext(), R.color.textPrimary));
      mContentTextColor = typedArray.getColor(R.styleable.StatusTile_statusTile_contentTextColor, ContextCompat.getColor(getContext(), R.color.textPrimary));
      mStepTextColorDisabled = typedArray.getColor(R.styleable.StatusTile_statusTile_stepTextColorDisabled, ContextCompat.getColor(getContext(), R.color.textPrimaryDisabled));
      mTitleTextColorDisabled = typedArray.getColor(R.styleable.StatusTile_statusTile_titleTextColorDisabled, ContextCompat.getColor(getContext(), R.color.textPrimaryDisabled));
      mContentTextColorDisabled = typedArray.getColor(R.styleable.StatusTile_statusTile_contentTextColorDisabled, ContextCompat.getColor(getContext(), R.color.textPrimaryDisabled));
      mShowDividerView = typedArray.getBoolean(R.styleable.StatusTile_statusTile_showDivider, true);

      mEnabledIcon = typedArray.getResourceId(R.styleable.StatusTile_statusTile_enabledIcon, R.drawable.ic_edit_black);
      mDisabledIcon = typedArray.getResourceId(R.styleable.StatusTile_statusTile_disabledIcon, R.drawable.ic_edit_black);
      mCompletedIcon = typedArray.getResourceId(R.styleable.StatusTile_statusTile_completedIcon, R.drawable.ic_check_black);
      mCompletedDisabledIcon = typedArray.getResourceId(R.styleable.StatusTile_statusTile_completedIcon, R.drawable.ic_check_black);

      if(mShowDividerView) {
        mDividerView.setVisibility(View.VISIBLE);
      } else {
        mDividerView.setVisibility(View.GONE);
      }

      String type = typedArray.getString(R.styleable.StatusTile_statusTile_type);

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
      typedArray.recycle();
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
          mStepTextView.setTextColor(mStepTextColor);
          mTitleTextView.setTextColor(mTitleTextColor);
          mContentTextView.setTextColor(mContentTextColor);

          mMainContainerDisabledFrameLayout.setVisibility(View.GONE);

          drawable = ContextCompat.getDrawable(getContext(), mEnabledIcon);
          break;
        case STATUS_DISABLED:
          mType = STATUS_DISABLED;
          mStepTextView.setTextColor(mStepTextColorDisabled);
          mTitleTextView.setTextColor(mTitleTextColorDisabled);
          mContentTextView.setTextColor(mContentTextColorDisabled);

          mMainContainerDisabledFrameLayout.setVisibility(View.VISIBLE);

          drawable = ContextCompat.getDrawable(getContext(), mDisabledIcon);
          break;
        case STATUS_COMPLETED:
          mType = STATUS_COMPLETED;
          mStepTextView.setTextColor(mStepTextColor);
          mTitleTextView.setTextColor(mTitleTextColor);
          mContentTextView.setTextColor(mContentTextColor);

          mMainContainerDisabledFrameLayout.setVisibility(View.GONE);

          drawable = ContextCompat.getDrawable(getContext(), mCompletedIcon);
          break;
        case STATUS_COMPLETED_DISABLED:
          mType = STATUS_COMPLETED_DISABLED;

          mStepTextView.setTextColor(mStepTextColor);
          mTitleTextView.setTextColor(mTitleTextColor);
          mContentTextView.setTextColor(mContentTextColor);

          mMainContainerDisabledFrameLayout.setVisibility(View.VISIBLE);

          drawable = ContextCompat.getDrawable(getContext(), mCompletedDisabledIcon);
          break;
        default:
          //Default view
          mType = STATUS_ENABLED;
          mMainContainerDisabledFrameLayout.setVisibility(View.GONE);

          mStepTextView.setTextColor(mStepTextColor);
          mTitleTextView.setTextColor(mTitleTextColor);
          mContentTextView.setTextColor(mContentTextColor);

          drawable = ContextCompat.getDrawable(getContext(), mEnabledIcon);
          break;
      }
      //Set the drawable
      mActionImageView.setImageDrawable(drawable);
    } else {
      //Default view

      Drawable drawable;
      mMainContainerDisabledFrameLayout.setVisibility(View.GONE);

      mStepTextView.setTextColor(mStepTextColor);
      mTitleTextView.setTextColor(mTitleTextColor);
      mContentTextView.setTextColor(mContentTextColor);

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
    mStepTextView.setText(step);
  }

  /**
   * Set the step of the tile.
   *
   * @param step a string representing the step of the tile.
   */
  public void setStep(@NonNull String step) {
    mStepTextView.setText(step);
  }

  /**
   * Set the title of the tile.
   *
   * @param title int resource the text for the title of the tile.
   */
  public void setTitle(@StringRes int title) {
    mTitleTextView.setText(title);
  }

  /**
   * Set the title of the tile.
   *
   * @param title a string representing the title of the tile.
   */
  public void setTitle(@NonNull String title) {
    mTitleTextView.setText(title);
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

  /**
   * Get the TextView for the Step
   * @return the step TextView.
   */
  public TextView getStepTextView() {
    return mStepTextView;
  }

  /**
   * Get the TextView for the Title.
   * @return the title TextView.
   */
  public TextView getTitleTextView() {
    return mTitleTextView;
  }

  /**
   * Get the TextView for the Content.
   * @return the content TextView.
   */
  public TextView getContentTextView() {
    return mContentTextView;
  }

  /**
   * The image view for the action (icon).
   * @return the image view that contains the action (icon).
   */
  public ImageView getActionImageView() {
    return mActionImageView;
  }
}
