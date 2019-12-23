package com.zj.imcore;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import static com.zj.base.BaseApplication.application;

@SuppressWarnings("unused")
public class Environment {

    private static Environment environment;
    private final String protocol;
    private final String host;
    private final String appId;
    private final String applicationId;

    private Environment(String protocol, String host, String appId, String applicationId) {
        this.protocol = protocol;
        this.appId = appId;
        this.host = host;
        this.applicationId = applicationId;
    }

    private static Environment createEnv(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String host = appInfo.metaData.getString("ENV_HOST");
            String protocol = appInfo.metaData.getString("ENV_PROTOCOL");
            String appId = appInfo.metaData.getString("ENV_APP_ID");
            String applicationId = appInfo.metaData.getString("APPLICATION_ID");
            return new Environment(protocol, host, appId, applicationId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Environment getEnv() {
        if (null == environment) {
            environment = createEnv(application);
        }
        return environment;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public String getUrl() {
        return String.format("%s://%s", protocol, host);
    }

    public String getAppId() {
        return appId;
    }

    public String getApplicationId() {
        return applicationId;
    }
}