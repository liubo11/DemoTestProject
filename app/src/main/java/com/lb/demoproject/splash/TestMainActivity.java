package com.lb.demoproject.splash;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lb.demoproject.BaseActivity;
import com.lb.demoproject.R;

/**
 * Created by LiuBo on 2016-11-08.
 */

public class TestMainActivity extends BaseActivity {

    private TextView tvShow;
    private EditText editInput;
    private Button btnNext;

    private String mContainerWords;
    private final String mSigned = " -> ";

    private ClipboardManager myClipboard;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_words);

        mContainerWords = getString(R.string.words);

        editInput = (EditText) findViewById(R.id.input);
        btnNext = (Button) findViewById(R.id.next);
        tvShow = (TextView) findViewById(R.id.show);

        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editInput.getEditableText().toString().trim();
                if (input.length() == 0) {
                    return;
                }
                int idx = mContainerWords.indexOf(input);
                if (idx > 0 && idx < mContainerWords.length() - 5) {
                    int start = idx + 4 + mSigned.length();
                    String w = mContainerWords.substring(start, start + 4);
                    tvShow.setText(w);

                    ClipData myClip;
                    myClip = ClipData.newPlainText("text", w);
                    myClipboard.setPrimaryClip(myClip);
                }
            }
        });
    }


}
