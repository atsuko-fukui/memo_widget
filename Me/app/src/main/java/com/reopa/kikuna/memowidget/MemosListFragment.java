package com.reopa.kikuna.memowidget;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.reopa.kikuna.memowidget.entity.MemosEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by atsuko on 15/07/19.
 */
public class MemosListFragment extends ListFragment {

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    updateList();
  }

  public void updateList() {
    final List<MemosEntity> entityList = MemosUtils.getLatestMemosFromDb(getActivity());
    if (entityList.size() == 0) {
      return;
    }

    List<String>  memoTexts = new ArrayList<>();
    for (MemosEntity entity : entityList) {
      memoTexts.add(entity.getMemoText());
    }
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
            android.R.layout.simple_list_item_1,
            memoTexts);
    setListAdapter(adapter);
    getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> items, View view, int position, long id) {
        final int selectedItemId = entityList.get(position).getId();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.confirm_delete);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    MemosUtils.deleteMemoById(selectedItemId, getActivity());
                    updateList();
                  }
                });
        builder.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                  }
                });
        builder.create().show();
      }
    });
    MyWidgetProvider.updateWidget(getActivity());
  }
}