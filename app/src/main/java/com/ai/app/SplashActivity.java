package com.ai.app;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;

import com.ai.base.AIBaseActivity;
import com.ai.base.ActivityConfig;
import com.ai.base.util.AESEncrypt;
import com.ai.base.util.LocalStorageManager;
import com.ai.webplugin.config.GlobalCfg;

import java.io.InputStream;

public class SplashActivity extends AIBaseActivity {

    private static String mGlabalCfgFile = "global.properties";
    private WebView mWebView;
    private static String kSplashHtml = "file:///android_asset/welcome.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clearAnswer();
        //
        setContentView(R.layout.splash_layout);

        mWebView = (WebView) findViewById(R.id.splash_webView);
        mWebView.loadUrl(kSplashHtml);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                enterHomeActivity();
            }
        }, 4000);
    }

    private void enterHomeActivity() {
        Intent intent = new Intent(this, PortalActivity.class);
        startActivity(intent);
        //finish();
    }

    private void clearAnswer () {
        try {
            InputStream is = this.getResources().getAssets().open(mGlabalCfgFile);
            GlobalCfg globalCfg = GlobalCfg.getInstance();
            globalCfg.parseConfig(is);

            String encryptKey = GlobalCfg.getInstance().attr(GlobalCfg.CONFIG_FIELD_ENCRYPTKEY);
            String publicKey = GlobalCfg.getInstance().attr(GlobalCfg.CONFIG_FIELD_PUBLICKEY);
            String key = AESEncrypt.decrypt(encryptKey, publicKey);

            // 初始化本地存储
            LocalStorageManager.getInstance().setContext(this);
            LocalStorageManager.getInstance().setEncryptKey(key);
            LocalStorageManager.getInstance().remove("answer");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 把手势密码逻辑的时间戳清零
        ActivityConfig.getInstance().setLockTime(0);
    }

    @Override
    protected void onResume() {
        mEnbleGesturePwd = false;
        super.onResume();
    }
}
