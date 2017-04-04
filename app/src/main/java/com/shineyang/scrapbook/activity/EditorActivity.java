package com.shineyang.scrapbook.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.shineyang.scrapbook.R;
import com.shineyang.scrapbook.bean.ContentBean;
import com.shineyang.scrapbook.utils.DBUtils;
import com.shineyang.scrapbook.utils.DateUtils;
import com.shineyang.scrapbook.utils.EditTextUtil;
import com.shineyang.scrapbook.view.EditableToolBar;
import com.shineyang.scrapbook.view.Toaster;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EditorActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_editor)
    Toolbar toolbar_editor;
    @BindView(R.id.tv_editor_content_from)
    TextView tv_editor_content_from;
    @BindView(R.id.tv_editor_created_date)
    TextView tv_editor_created_date;
    @BindView(R.id.edt_content)
    EditText edt_content;
    @BindView(R.id.included_edit_tool_bar)
    EditableToolBar editableToolBar;
    @BindView(R.id.tv_text_count)
    TextView tv_text_count;
    @BindView(R.id.rl_editor_content_info)
    RelativeLayout rl_editor_content_info;

    private String id, content, from, date;
    private EditTextUtil editTextUtil;
    private Boolean isSaved = true;
    private MenuItem item_save;
    private Boolean isAddMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);
        initToolBar();
        getExtraContent();
        initEditToolBar();
        edt_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                isSaved = false;
                changeSaveIcon();
                int number = editable.length();
                tv_text_count.setText(number + getResources().getString(R.string.text_text));
            }
        });
    }

    public void initToolBar() {
        toolbar_editor.setTitle("文字编辑");
        setSupportActionBar(toolbar_editor);
        toolbar_editor.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIsSaved();
            }
        });
    }

    public void initEditToolBar() {
        editTextUtil = new EditTextUtil(edt_content);
        editableToolBar.setOnUndoClickListener(new EditableToolBar.OnUndoClickListener() {
            @Override
            public void onUndoClick() {
                editTextUtil.undo();
            }
        });

        editableToolBar.setOnRedoClickListener(new EditableToolBar.OnRedoClickListener() {
            @Override
            public void onRedoClick() {
                editTextUtil.redo();
            }
        });

        editableToolBar.setOnPullWordClickListener(new EditableToolBar.OnPullWordClickListener() {
            @Override
            public void onPullWordClick() {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("pullword://?extra_text=" + URLEncoder.encode(edt_content.getText().toString(), "utf-8"))));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        editableToolBar.setOnCopyClickListener(new EditableToolBar.OnCopyClickListener() {
            @Override
            public void onCopyClick() {
                copyText();
            }
        });

        editableToolBar.setOnShareClickListener(new EditableToolBar.OnShareClickListener() {
            @Override
            public void onShareClick() {
                shareText();
            }
        });
    }

    public void copyText() {
        if (!TextUtils.isEmpty(edt_content.getText().toString())) {
            ClipboardManager service = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            service.setPrimaryClip(ClipData.newPlainText("editor", edt_content.getText().toString()));
            Toaster.showShortToast(getApplicationContext(), "已复制");
        }
    }

    public void shareText() {
        String text = edt_content.getText().toString();
        if (!TextUtils.isEmpty(text)) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.text_share_to)));
        }
    }

    public List<ContentBean> getBeanById(String id) {
        return DBUtils.getContentBeanById(id);
    }

    public void getExtraContent() {
        Intent intent = getIntent();
        id = intent.getStringExtra("list_id");
        ContentBean contentBean = getBeanById(id).get(0);
        if (intent.hasExtra("list_content")) {
            content = contentBean.getContent();
            from = contentBean.getFrom();
            date = contentBean.getDate();
            setContentInfo();
        } else if (intent.getBooleanExtra("addMode", true)) {
            isAddMode = true;
            rl_editor_content_info.setVisibility(View.GONE);
        } else {
            Log.v("editor", "no content");
        }
    }

    public void setContentInfo() {
        tv_editor_created_date.setText(date);
        tv_editor_content_from.setText(from);
        edt_content.setText(content);
        edt_content.setSelection(content.length());//移动光标到最后
        tv_text_count.setText(content.length() + getResources().getString(R.string.text_text));
    }


    public void saveEditedContent() {
        if (isAddMode) {
            String date = DateUtils.getCurDateAndTime();
            String from = getResources().getString(R.string.app_name);
            ContentBean bean = new ContentBean(edt_content.getText().toString(), from, date);
            DBUtils.addItem(bean);
            isSaved = true;
            changeSaveIcon();
        } else {
            DBUtils.saveEditedContent(id, edt_content.getText().toString());
            isSaved = true;
            changeSaveIcon();
        }
    }

    public void changeSaveIcon() {
        if (isSaved) {
            item_save.setIcon(R.mipmap.ic_save_grey);
        } else {
            item_save.setIcon(R.mipmap.ic_save);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        item_save = menu.findItem(R.id.action_save);
        item_save.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (!isSaved) {
                    saveEditedContent();
                    Toaster.showShortToast(getApplicationContext(), R.string.text_toast_saved);
                } else Log.v("editor", "already save");

                return false;
            }
        });

        return true;
    }

    public void checkIsSaved() {
        if (isSaved) {
            finish();
        } else {
            new MaterialDialog.Builder(this)
                    .title(getResources().getString(R.string.text_save_dialog_title))
                    .positiveText(getResources().getString(R.string.text_save_dialog_pos))
                    .negativeText(getResources().getString(R.string.text_save_dialog_neg))
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            saveEditedContent();
                            finish();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                        }
                    })
                    .show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            checkIsSaved();
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //文字清空操作栈
        editTextUtil.clearHistory();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
