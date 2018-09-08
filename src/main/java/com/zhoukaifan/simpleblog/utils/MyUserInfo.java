package com.zhoukaifan.simpleblog.utils;

import com.jcraft.jsch.UserInfo;

public class MyUserInfo implements UserInfo {
    @Override
    public String getPassphrase() {
        return null;
    }
    @Override
    public String getPassword() {
        return null;
    }
    @Override
    public boolean promptPassphrase(String arg0) {
        return false;
    }
    @Override
    public boolean promptPassword(String arg0) {
        return false;
    }
    @Override
    public boolean promptYesNo(String arg0) {
         if (arg0.contains("The authenticity of host")) {
             return true;  
         }  
        return true;
    }
    @Override
    public void showMessage(String arg0) {
    }
}