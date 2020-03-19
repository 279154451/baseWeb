package com.example.webviewdemo.webview;

import android.os.Bundle;

import com.example.uibase.basefragment.BaseWebViewFragment;
import com.example.uibase.utils.WebConstants;
import com.example.webviewdemo.R;

/**
 * 创建时间：2020/3/19
 * 创建人：singleCode
 * 功能描述：普通WebViewFragment
 **/
public class BaseWebFragment extends BaseWebViewFragment {
    public String url;

    public static BaseWebFragment newInstance(String url) {
        BaseWebFragment fragment = new BaseWebFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("url", url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_common_webview;
    }

    @Override
    public int getCommandLevel() {
        return WebConstants.LEVEL_BASE;
    }
}
