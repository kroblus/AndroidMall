package com.example.fruit.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


/**
 * 虚拟键盘操作
 */
public class KeyBoardUtil {
    /**
     * 打开键盘
     * @param view
     */
    public static void showKeyboard(View view){
        InputMethodManager imm= (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm!=null){
            view.requestFocus();
            imm.showSoftInput(view,0);
        }
    }
    /**
     * 关闭键盘
     * @param view
     */
    public static void hideKeyboard(View view){
        InputMethodManager imm= (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm!=null){
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
}
