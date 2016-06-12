package com.wrh.mvp.doubanmvp.app;

import android.app.Application;

import com.orhanobut.logger.AndroidLogTool;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.wrh.mvp.doubanmvp.BuildConfig;

/**
 * Created by user on 2016/6/2.
 */
public class WrhApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("DouBanMvp").logTool(new AndroidLogTool()).logLevel(parseLogLevel());
    }

    /**
     * 正式打包时关闭debug信息
     * @return
     */
    private LogLevel parseLogLevel() {
        if (BuildConfig.DEBUG) {
            return LogLevel.FULL;
        }
        return LogLevel.NONE;
    }
}
