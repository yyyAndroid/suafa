package com.arcfun.afl

import android.app.Application
import android.content.Context
import android.text.TextUtils
import com.argusapm.android.api.ApmTask
import com.argusapm.android.api.Client
import com.argusapm.android.core.Config
import com.argusapm.android.network.cloudrule.RuleSyncRequest
import com.argusapm.android.network.upload.CollectDataSyncUpload


class JavaApplication : Application() {

    override fun onCreate() {
        super.onCreate()

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

//        val isMainProcess = TextUtils.equals(packageName, getCurrentProcessName)
        val builder: Config.ConfigBuilder = Config.ConfigBuilder()
            .setAppContext(this)
            .setRuleRequest(RuleSyncRequest())
            .setUpload(CollectDataSyncUpload())
            .setAppName("afl")
            .setAppVersion("0.0.1")
            .setApmid("sp2b32kp8baw")

//        if (!isMainProcess) {

//除了“主进程”，其他进程不需要进行数据上报、清理等逻辑。“主进程”通常为常驻进行，如果无常驻进程，即为UI进程。
            builder.setDisabled(ApmTask.FLAG_DATA_CLEAN)  //只有主进程才清理数据
                .setDisabled(ApmTask.FLAG_CLOUD_UPDATE)//只有主进程才执行云控
                .setDisabled(ApmTask.FLAG_DATA_UPLOAD)//只有主进程才执行数据上报
                .setDisabled(ApmTask.FLAG_COLLECT_ANR)//只有主进程才收集ANR
                .setDisabled(ApmTask.FLAG_COLLECT_FILE_INFO) //只有主进程才收集文件信息
                .setDisabled(ApmTask.FLAG_COLLECT_CPU);//只有主进程才收集CPU数据
//        }
//builder.setEnabled(ApmTask.FLAG_COLLECT_ACTIVITY_AOP); //activity采用aop方案时打开，默认关闭即可。
//builder.setEnabled(ApmTask.FLAG_LOCAL_DEBUG); //是否读取本地配置，默认关闭即可。

        Client.attach(builder.build())
        Client.isDebugOpen(true)
// Client.isDebugOpen(true, getPackageName());//  是否展示debug模式悬浮窗。根据项目需求添加
        Client.startWork()
    }
}