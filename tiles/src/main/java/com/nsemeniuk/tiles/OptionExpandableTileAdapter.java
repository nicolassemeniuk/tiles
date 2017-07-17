package com.nsemeniuk.tiles;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandressemeniuk on 4/18/17.
 */

public class OptionExpandableTileAdapter extends RecyclerView.Adapter<OptionViewHolder> implements OptionViewHolder.HolderSelectionListener {

  private ArrayList<String> mValues;
  private OptionExpandableTile mOptionExpandableTile;
  private SparseBooleanArray selectedItems = new SparseBooleanArray();
  private OnSelectionListener mListener;
  private boolean mSingleSelection;

  public OptionExpandableTileAdapter(@NonNull ArrayList<String> values, @NonNull OptionExpandableTile optionTile, @Nullable ArrayList<String> selectedValues, @Nullable boolean singleSelection) {
    this.mValues = values;
    this.mOptionExpandableTile = optionTile;
    this.mSingleSelection = singleSelection;

    //Set the values as selected if provided any.
    if (selectedValues != null) {
      for (String value : values) {
        if (selectedValues.contains(value)) {
          selectedItems.put(values.indexOf(value), true);
        }
      }
    }


  }

  @Override
  public OptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new OptionViewHolder(this, parent);
  }

  @Override
  public void onBindViewHolder(OptionViewHolder holder, int position) {
    holder.getTextView().setText(mValues.get(position));
    holder.setPosition(position);
    holder.setSelected(selectedItems.get(position, false));
  }

  @Override
  public int getItemCount() {
    return mValues.size();
  }

  private void toggleSelection(int pos) {
    if (mSingleSelection) {
      //Single selection
      clearSelections();
      selectedItems.put(pos, true);
      notifyItemChanged(pos);
      mOptionExpandableTile.setCount(getSelectedItemCount());
    } else {
      //we are doing multiple selection
      if (selectedItems.get(pos, false)) {
        selectedItems.delete(pos);
      } else {
        selectedItems.put(pos, true);
      }
      notifyItemChanged(pos);
      mOptionExpandableTile.setCount(getSelectedItemCount());
    }

    if (mListener != null) {
      mListener.optionSelected(pos, selectedItems.size());
    }
  }


  public void clearSelections() {
    selectedItems.clear();
    notifyDataSetChanged();
  }

  private int getSelectedItemCount() {
    if(mSingleSelection){
      //For single selection, we return the value selected.
      return selectedItems.keyAt(0) + 1;
    } else {
      //For multiple selection, we return the amount of items selected.
      return selectedItems.size();
    }

  }

  public List<Integer> getSelectedItems() {
    List<Integer> items =
        new ArrayList<Integer>(selectedItems.size());
    for (int i = 0; i < selectedItems.size(); i++) {
      items.add(selectedItems.keyAt(i));
    }
    return items;
  }


  public OnSelectionListener getOptionSelectionListener() {
    return mListener;
  }

  public void setOptionSelectionListener(OnSelectionListener listener) {
    mListener = listener;
  }

  //------------------------------------------------------------------------------------------
  // INTERFACE METHODS

  @Override
  public void optionSelected(int optionNumber) {
    toggleSelection(optionNumber);
  }

  //------------------------------------------------------------------------------------------
  // INTERFACE

  public interface OnSelectionListener {

    void optionSelected(int position, int totalSelected);

  }


}
