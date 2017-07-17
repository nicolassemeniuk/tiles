package com.nsemeniuk.tiles;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * This class represents a view that has an Option Tile on the top, and expands a list below to
 * select Items.
 * <p>
 * It requires a title, a content and optionally you can provide a custom string to display on the
 * selection of the items of the list. Keep in mind that this string should be in the format:
 * %1$s selected
 * Where selected can be anything you want: e.g. restaurants added, food selected, etc.
 * <p>
 * You can also set listener to observe the changes for expanding an collapsing of the associated
 * recyclerView.
 */

public class OptionExpandableTile extends FrameLayout implements OptionExpandableTileAdapter.OnSelectionListener {

  private OnExpandListener mOnExpandListener;
  private OnSelectionListener mOnSelectionListener;

  private OptionTile mOptionTile;
  private RecyclerView mRecyclerView;
  private OptionExpandableTileAdapter mAdapter;
  private final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());

  private boolean mExpanded;
  private String mTitle;
  private String mContent;
  private String mSelectedContent;
  private int mMaxHeightExpanded;
  private boolean mCanExpandAndCollapse;
  private int MAX_HEIGHT_EXPANDED = 150;

  public OptionExpandableTile(@NonNull Context context) {
    super(context);
    init(null);
  }

  public OptionExpandableTile(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public OptionExpandableTile(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  @RequiresApi(21)
  public OptionExpandableTile(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(attrs);
  }

  private void init(@Nullable AttributeSet attrs) {
    inflate(getContext(), R.layout.option_expandable_tile, this);
    mOptionTile = (OptionTile) findViewById(R.id.option_tile);
    mRecyclerView = (RecyclerView) findViewById(R.id.options_recycler_view);

    //Start compressed.
    mExpanded = false;
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
    mRecyclerView.setLayoutParams(params);


    //Initialize the values.
    setOptionsValues(new ArrayList<String>(), null, false);

    //Check for the values on the XML.
    TypedArray a = getContext().getTheme().obtainStyledAttributes(
        attrs,
        R.styleable.OptionExpandableTile,
        0, 0);

    try {
      mTitle = a.getString(R.styleable.OptionExpandableTile_optionExpandableTitleText);
      mContent = a.getString(R.styleable.OptionExpandableTile_optionExpandableContentText);
      mSelectedContent = a.getString(R.styleable.OptionExpandableTile_optionExpandableContentSelectedText);
      mMaxHeightExpanded = a.getInt(R.styleable.OptionExpandableTile_optionExpandableTileHeightExpanded, MAX_HEIGHT_EXPANDED);
      mCanExpandAndCollapse = a.getBoolean(R.styleable.OptionExpandableTile_optionExpandableTileCanExpandAndCollapse, true);
      if (mTitle != null) {
        mOptionTile.setTitle(mTitle);
      }
      if (mContent != null) {
        mOptionTile.setContent(mContent);
      }

      if (mCanExpandAndCollapse) {
        //Click listener for the tile, if we have a listener for changes, notify it and animate.
        mOptionTile.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View view) {
            if (mExpanded) {
              collapse();
            } else {
              expand();
            }
          }
        });
      }

    } finally {
      a.recycle();
    }
  }


  /**
   * Set the title on the Option tile.
   *
   * @param title the res representing a string for the title.
   */
  public void setTitle(@StringRes int title) {
    mOptionTile.setTitle(title);
  }

  /**
   * Set the content on the Option tile.
   *
   * @param content the res representing a string for the content.
   */
  public void setContent(@StringRes int content) {
    mOptionTile.setContent(content);
  }

  /**
   * Set the title on the Option tile.
   *
   * @param title the string for the title.
   */
  public void setTitle(@NonNull String title) {
    mOptionTile.setTitle(title);
  }


  /**
   * Set the content on the Option tile.
   *
   * @param content the string for the content.
   */
  public void setContent(@NonNull String content) {
    mOptionTile.setContent(content);
  }

  /**
   * Set count changes the content of the OptionTile if you have set a specific selection text.
   *
   * @param count the amount of items selected.
   */
  public void setCount(int count) {
    if (mSelectedContent != null) {
      mOptionTile.setContent(String.format(mSelectedContent, count));
    }
  }

  /**
   * Set a listener for the expand/collapse behavior.
   *
   * @param listener a listener to react to collapse/expand behavior.
   */
  public void setOnExpandListener(@NonNull OnExpandListener listener) {
    mOnExpandListener = listener;
  }

  /**
   * Set the values that belong to the list for the expandable tile.
   *
   * @param values the values that will be displayed on the list.
   */
  public void setOptionsValues(@NonNull ArrayList<String> values, @Nullable ArrayList<String> selectedValues, @Nullable boolean isSingleSelection) {
    mAdapter = new OptionExpandableTileAdapter(values, this, selectedValues, isSingleSelection);
    mAdapter.setOptionSelectionListener(this);
    if (mRecyclerView != null) {
      mRecyclerView.setLayoutManager(mLinearLayoutManager);
      mRecyclerView.setAdapter(mAdapter);
    }

    //If we have specified pre-selected values and a selected content, change the content to reflect it.
    if(isSingleSelection){
      if(mSelectedContent != null && selectedValues != null && !selectedValues.isEmpty()){
        mOptionTile.setContent(String.format(mSelectedContent, selectedValues.get(0)));
      }
    } else {
      if (mSelectedContent != null && selectedValues != null && !selectedValues.isEmpty()) {
        mOptionTile.setContent(String.format(mSelectedContent, selectedValues.size()));
      }
    }

  }

  /**
   * Collapse the list below the option tile.
   */
  public void collapse() {
    if (mExpanded) {
      mOptionTile.setArrowRight();

      Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.collapse);
      animation.setDuration(200);
      animation.setRepeatCount(0);
      animation.setFillAfter(true);
      animation.setInterpolator(new AccelerateInterpolator(1.0f));
      animation.setAnimationListener(new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
          mExpanded = false;
          LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
          mRecyclerView.setLayoutParams(params);
          if (mOnExpandListener != null) {
            mOnExpandListener.collapsed();
          }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
      });
      mRecyclerView.setAnimation(animation);
      mRecyclerView.animate();
    }
  }

  /**
   * Expand the list below the option tile.
   */
  public void expand() {
    if (!mExpanded) {
      mOptionTile.setArrowDown();

      Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.expand);
      animation.setDuration(200);
      animation.setRepeatCount(0);
      animation.setFillAfter(true);
      animation.setInterpolator(new AccelerateInterpolator(1.0f));
      animation.setAnimationListener(new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
          DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
          float pxHeight = displayMetrics.density * mMaxHeightExpanded;

          //Each sell measures 50 dp. So we calculate how much height have all the possible cells.
          float cellHeight = displayMetrics.density * 50;
          float totalCellHeight = cellHeight * mAdapter.getItemCount();
          LinearLayout.LayoutParams params;

          if (pxHeight < totalCellHeight) {
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Math.round(pxHeight));
          } else {
            //The max height is bigger than the amount of cells we have, just wrap the content.
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
          }

          mRecyclerView.setLayoutParams(params);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
          mExpanded = true;
          if (mOnExpandListener != null) {
            mOnExpandListener.expanded();
          }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
      });
      mRecyclerView.setAnimation(animation);
      mRecyclerView.animate();

    }

  }

  public void setOnSelectionListener(OnSelectionListener listener) {
    mOnSelectionListener = listener;
  }

  public OptionExpandableTileAdapter getAdapter() {
    return mAdapter;
  }

  //------------------------------------------------------------------------------------------
  // INTERFACE METHODS

  @Override
  public void optionSelected(int position, int totalSelected) {
    mOnSelectionListener.optionSelected(position, totalSelected);
  }


  //------------------------------------------------------------------------------------------
  // INTERFACE

  /**
   * This interface can be implemented by any parent that want's to listen to the collapse and
   * expand changes on this view.
   */

  public interface OnExpandListener {

    /**
     * Called AFTER the view has expanded.
     */
    void expanded();

    /**
     * Called AFTER the view has collapsed.
     */
    void collapsed();


  }

  //------------------------------------------------------------------------------------------
  // INTERFACE

  public interface OnSelectionListener {

    void optionSelected(int position, int totalSelected);

  }


}
