package com.ai.app;

import android.webkit.JavascriptInterface;
import android.widget.Toast;
import com.ai.base.AIBaseActivity;
import com.ai.webplugin.AIWebViewBasePlugin;
import com.ai.webplugin.config.GlobalCfg;
import com.qihoo360.replugin.RePlugin;

import java.io.File;

/**
 * Created by wuyoujian on 2017/5/4.
 *
 */

public class PortalScriptPlugin extends AIWebViewBasePlugin {

    public PortalScriptPlugin(AIBaseActivity activity) {
        super(activity);
    }

    @JavascriptInterface
    @NotProguard
    public void JN_EnterPlugin(String token,String subAcc, String pluginName ) {

        AppConfig appConfig = AppConfig.getInstance();
        AppConfig.APKPluginInfo apkPluginInfo = appConfig.getAPKPlugin(pluginName);
        String apkPath = AppConfig.getInstance().getAPKAbsolutePath(pluginName);
        File apkFile = new File(apkPath);
        if (apkFile.exists()) {
            appConfig.loadAPK(apkPluginInfo,token,subAcc);
        } else {
            int downloadStatus = appConfig.getAPKDownloadStatus(pluginName);
            if (downloadStatus == 2) {
                appConfig.loadAPK(apkPluginInfo,token,subAcc);
            } else if (downloadStatus == 0){
                // 下载
                Toast.makeText(getActivity(),"开始下载插件...",Toast.LENGTH_LONG).show();
                appConfig.downloadAPKPlugin(apkPluginInfo,token,subAcc);
            } else if (downloadStatus == 1) {
                Toast.makeText(getActivity(),"插件正在下载...",Toast.LENGTH_SHORT).show();
            } else  {
                Toast.makeText(getActivity(),"插件配置异常",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @JavascriptInterface
    @NotProguard
    public void JN_Exit() {
        System.exit(0);
    }

    @JavascriptInterface
    @NotProguard
    public String JN_AppVersion() {
        GlobalCfg globalCfg = GlobalCfg.getInstance();
        String version = globalCfg.attr(GlobalCfg.CONFIG_FIELD_VERSION);

        return version;
    }

}
