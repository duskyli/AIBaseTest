package com.ai.aibasetest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.ai.base.AIBaseActivity;
import com.ai.webplugin.AIWebViewClient;
import com.ai.webplugin.dl.AIWebViewPluginEngine_dl;
import com.ai.webplugin.dl.GlobalCfg_dl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

public class MainActivity extends AIBaseActivity {

    private WebView mWebView;
    private LinearLayout mLinearLayout;

    private static String mGlabalCfgFile = "global.properties";
    private static String mPluginCfgFile = "h5Plugin.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initParam();
        initWebView();
    }

    private void initParam() {
        try {
            InputStream is = this.getResources().getAssets().open(mGlabalCfgFile);
            GlobalCfg_dl globalCfg = GlobalCfg_dl.getInstance();
            globalCfg.parseConfig(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setH5PluginEngine() {
        AIWebViewPluginEngine_dl engine = AIWebViewPluginEngine_dl.getInstance();
        engine.registerPlugins(this, mWebView, mPluginCfgFile);
    }

    private void initWebView() {

        mLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mLinearLayout.setLayoutParams(params);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        mWebView = new WebView(this);
        mWebView.setLayoutParams(params);
        mWebView.setWebViewClient(new AIWebViewClient("",""));

        mLinearLayout.addView(mWebView,tvParams);
        setContentView(mLinearLayout);

        // 加载H5插件
        setH5PluginEngine();

        String url = GlobalCfg_dl.getInstance().attr(GlobalCfg_dl.CONFIG_FIELD_ONLINEADDR);
        mWebView.loadUrl(url);
    }

    /**
     * 获取Base64字符串
     * @return
     */
    private String getBase64Str(String path) {
        String base64Str = "";
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        try {
            base64Str = Base64.encodeToString(data, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64Str;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_FIRST_USER) {
            finish();
        }

        if (resultCode == this.RESULT_OK) {

            if(requestCode == 100) {

                if (data == null) {
                    return;
                }

                String imagePath = data.getStringExtra("savePath");
                File file = new File(imagePath);
                if (file.exists()) {
                    String functionName = data.getStringExtra("functionName");
                    String base64 = getBase64Str(imagePath);

                    //
                    AIWebViewPluginEngine_dl engine = AIWebViewPluginEngine_dl.getInstance();
                    String JS = String.format("%s('%s')",functionName,base64);
                    engine.excuteJavascript(JS, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                        }
                    });
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        // 创建构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置参数
        builder.setTitle("提示").setIcon(R.mipmap.ic_launcher)
                .setMessage("您是否要退出插件")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        //that.moveTaskToBack(false);
                    }
                });
        builder.create().show();
    }
}
