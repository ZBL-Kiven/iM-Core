package com.zj.imcore.ui.chat.func;

public class AppBean {

    private int id;
    private int icon;
    private String funcName;

    int getIcon() {
        return icon;
    }

    String getFuncName() {
        return funcName;
    }

    public int getId() {
        return id;
    }

    AppBean(int id, int icon, String funcName) {
        this.id = id;
        this.icon = icon;
        this.funcName = funcName;
    }
}
