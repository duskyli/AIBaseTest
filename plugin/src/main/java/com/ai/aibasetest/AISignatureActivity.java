package com.ai.aibasetest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.ai.base.gesture.AISignatureView;
import com.ryg.dynamicload.DLBasePluginActivity;

import java.io.File;
import java.io.IOException;


/**
 * Created by wuyoujian on 17/4/28.
 */

public class AISignatureActivity extends DLBasePluginActivity {

    public static final String kSignatureSavePathKey = "kSignatureSavePathKey";

    private AISignatureView mSignatureView;
    private Button mClearButton;
    private Button mFinishButton;
    private Button mCancelButton;

    private String mSavePath;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        String savePath = getIntent().getStringExtra(kSignatureSavePathKey);
        if (savePath != null) {
            mSavePath = savePath;
        } else {
            String filePath = that.getCacheDir().getAbsolutePath()+ "/images";
            File file = new File(filePath);
            file.mkdir();
            mSavePath = filePath + "/signature.png";
        }

        mSignatureView = (AISignatureView)findViewById(R.id.signature_pad);
        mCancelButton = (Button)findViewById(R.id.btn_cancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignatureView.delete(mSavePath);
                finish();
            }
        });

        // 重签
        mClearButton = (Button)findViewById(R.id.btn_rewrite);
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignatureView.clear();
            }
        });

        // 确定
        mFinishButton = (Button)findViewById(R.id.btn_ok);
        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSignatureView.getmIsTouched()) {

                    try {
                        mSignatureView.save(mSavePath,0.5f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Intent intent = new Intent();
                intent.putExtra("savePath",mSavePath);

                String username = getIntent().getStringExtra("username");
                String functionName = getIntent().getStringExtra("functionName");
                intent.putExtra("username",username);
                intent.putExtra("functionName",functionName);

                that.setResult(that.RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
