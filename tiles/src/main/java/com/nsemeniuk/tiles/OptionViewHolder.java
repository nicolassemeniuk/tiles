package com.nsemeniuk.tiles;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 *
 */

public class OptionViewHolder extends RecyclerView.ViewHolder {

  private TextView mTextView;
  private RelativeLayout mMainContainerRelativeLayout;
  private ImageView mCheckImageView;

  private HolderSelectionListener mListener;
  private Context mContext;

  private int mPosition;
  private boolean mSelected;

  public OptionViewHolder(HolderSelectionListener listener, ViewGroup parent) {
    super(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_option_expandable_tile, parent, false));

    mTextView = (TextView) itemView.findViewById(R.id.option_text_view);
    mMainContainerRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.main_container_relative_layout);
    mCheckImageView = (ImageView) itemView.findViewById(R.id.option_check_image_view);
    mListener = listener;
    mPosition = -1;
    mSelected = false;
    mContext = parent.getContext();


    mTextView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mListener.optionSelected(mPosition);
        changeStatus();
      }
    });

  }

  public TextView getTextView() {
    return mTextView;
  }

  public void setPosition(int position){
    mPosition = position;
  }

  public void setSelected(boolean selected){
    mSelected = selected;
    if(mSelected) {
      select();
    } else {
      deselect();

    }
  }

  private void changeStatus(){
    if(mSelected){
      deselect();
      mSelected = false;
    } else {
      select();
      mSelected = true;
    }
  }

  private void select(){
    mMainContainerRelativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent));
    mTextView.setTextColor(ContextCompat.getColor(mContext, android.R.color.white));
    mTextView.setTypeface(null, Typeface.BOLD);
    mCheckImageView.setVisibility(View.VISIBLE);
  }

  private void deselect(){
    mMainContainerRelativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.white));
    mTextView.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
    mTextView.setTypeface(null, Typeface.NORMAL);
    mCheckImageView.setVisibility(View.INVISIBLE);
  }

  //------------------------------------------------------------------------------------------
  // INTERFACE

  interface HolderSelectionListener {

    void optionSelected(int optionNumber);

  }
}
