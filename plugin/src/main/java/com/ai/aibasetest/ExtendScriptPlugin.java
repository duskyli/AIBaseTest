package com.ai.aibasetest;


import android.webkit.JavascriptInterface;

import com.ai.webplugin.dl.AIWebViewBasePlugin_dl;
import com.ai.webplugin.dl.GlobalCfg_dl;
import com.ryg.dynamicload.DLBasePluginActivity;
import com.ryg.dynamicload.internal.DLIntent;

/**
 * Created by wuyoujian on 2017/5/4.
 */

public class ExtendScriptPlugin extends AIWebViewBasePlugin_dl {

    public ExtendScriptPlugin(DLBasePluginActivity activity) {
        super(activity);
    }

    // 电子签名
    @JavascriptInterface
    @NotProguard
    public void JN_Signature(String functionName, String username) {
        DLIntent intent = new DLIntent(getDLActivity().getPackageName(), AISignatureActivity.class);
        intent.putExtra("username",username);
        intent.putExtra("functionName",functionName);
        getDLActivity().startPluginActivityForResult(intent, 100);
    }


    @JavascriptInterface
    @NotProguard
    public String JN_PluginVersion() {
        GlobalCfg_dl globalCfg = GlobalCfg_dl.getInstance();
        String version = globalCfg.attr(GlobalCfg_dl.CONFIG_FIELD_VERSION);

        return version;
    }

    @JavascriptInterface
    @NotProguard
    public void JN_Exit() {
        System.exit(0);
    }

    @JavascriptInterface
    @NotProguard
    public void JN_BackToPortal() {
        getDLActivity().finish();
    }
}
