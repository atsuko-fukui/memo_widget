package com.reopa.kikuna.memowidget;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.reopa.kikuna.memowidget.dao.MemosDao;
import com.reopa.kikuna.memowidget.entity.MemosEntity;

import java.util.List;

/**
 * Created by atsuko on 15/07/19.
 */
public class MemosListFragment extends ListFragment {

    private List<MemosEntity> mListItems;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateList();
    }

    public void updateList() {
        MemosDao dao = new MemosDao(getActivity());
        mListItems = dao.findAll();

        MemoListAdapter adapter = new MemoListAdapter(getActivity());
        setListAdapter(adapter);

        // リスト内各ViewのonClick()イベントを検知したい為、リスナの設定処理をMemoListAdapter.getView()に移動
        /*
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
        */

        MyWidgetProvider.updateWidget(getActivity());
    }

    private class MemoListAdapter extends ArrayAdapter<MemosEntity> {

        /**
         * Memolist layout resource ID.
         */
        private static final int ID_RESOURCE = R.layout.memos_list_fragment_layout;

        private LayoutInflater mInflater;


        public MemoListAdapter(Context context) {
            super(context, ID_RESOURCE, mListItems);
            this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get contentView by arg.
            final MemoViewHolder holder;

            // If contentView is null, create new View by LayoutInflater.
            if (convertView == null) {
                convertView = mInflater.inflate(ID_RESOURCE, null);
                holder = new MemoViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (MemoViewHolder) convertView.getTag();
            }

            if (mListItems.size() == 0) {
                return convertView;
            }

            // Get MemoEntity object that match the position.
            MemosEntity memo = mListItems.get(position);

            // Set memotext.
            holder.setMemoText(position, memo.getMemoText());

            return convertView;
        }
    }

    private class MemoViewHolder {
        /**
         * Textview ID.
         */
        private static final int ID_LAYOUT_MEMOTEXT = R.id.memo_text;
        /**
         * Imagebutton ID.
         */
        private static final int ID_LAYOUT_DETAIL_BUTTON = R.id.memo_detail_button;

        TextView memoText;
        ImageButton memodetailButton;

        public MemoViewHolder(View convertView) {
            memoText = (TextView) convertView.findViewById(ID_LAYOUT_MEMOTEXT);
            memodetailButton = (ImageButton) convertView.findViewById(ID_LAYOUT_DETAIL_BUTTON);
        }

        public void setMemoText(final int position, final String text) {
            final int ID_DRAWABLE_MEMO_BG[] = {R.drawable.husen_0, R.drawable.husen_1, R.drawable.husen_2, R.drawable.husen_3};

            // Set memo text.
            memoText.setText(text);

            // Create a randam number to select a background image.
            int num = text.length() % 4;
            memoText.setBackgroundResource(ID_DRAWABLE_MEMO_BG[num]);

            // Set TextView's onClickListener
            memoText.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    viewDelDialog(position);
                }
            });

            // Set detail action button.
            memodetailButton.setOnClickListener(new View.OnClickListener() {
                final int selectedItemId = mListItems.get(position).getId();

                public void onClick(View v) {
                    LinearLayout layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.list_popup_layout, null);

                    ImageButton copyButton = (ImageButton) layout.findViewById(R.id.detail_copy);
                    ImageButton editButton = (ImageButton) layout.findViewById(R.id.detail_edit);
                    ImageButton deleteButton = (ImageButton) layout.findViewById(R.id.detail_delete);

                    // Copy Button setting.
                    copyButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String copyString = mListItems.get(position).getMemoText();

                            String[] mimeType = new String[1];
                            mimeType[0] = ClipDescription.MIMETYPE_TEXT_PLAIN;
                            ClipData cd = new ClipData(
                                    new ClipDescription("text_data", mimeType),
                                    new ClipData.Item(copyString));

                            ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                            cm.setPrimaryClip(cd);

                            Toast.makeText(getActivity(), "Copy text [" + copyString + "]", Toast.LENGTH_SHORT).show();

                        }
                    });

                    // Edit Button setting.
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewEditDialog(position);
                        }
                    });

                    // Delete Button setting.
                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewDelDialog(position);
                        }
                    });

                    PopupWindow popupWindow = new PopupWindow(getActivity());
                    popupWindow.setWindowLayoutMode(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    popupWindow.setContentView(layout);
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setFocusable(true);
                    popupWindow.showAsDropDown(v);

                    int xoff = getListView().getWidth();
                    int yoff = v.getHeight();
                    popupWindow.showAsDropDown(v, xoff, -yoff);

                }
            });

        }

        /**
         * Create memo delete dialog.
         */
        private void viewDelDialog(int position) {
            final int selectedItemId = mListItems.get(position).getId();

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.confirm_delete);
            builder.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MemosDao dao = new MemosDao(getActivity());
                            dao.delete(selectedItemId);
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

        private void viewEditDialog(final int position) {
            final EditText editText = new EditText(getActivity());
            editText.setHint(R.string.new_memo_hint);
            editText.setText(mListItems.get(position).getMemoText());

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.edit_memo);
            builder.setView(editText);
            builder.setPositiveButton(R.string.save,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MemosEntity memo = new MemosEntity();
                            memo.setId(mListItems.get(position).getId());
                            memo.setMemoText(editText.getText().toString());
                            MemosDao dao = new MemosDao(getActivity());
                            dao.update(memo);
                            updateList();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            dialog.show();

        }

    }
}