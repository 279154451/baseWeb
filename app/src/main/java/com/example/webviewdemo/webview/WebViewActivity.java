package com.example.webviewdemo.webview;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uibase.basefragment.BaseWebViewFragment;
import com.example.uibase.command.Command;
import com.example.uibase.command.CommandsManager;
import com.example.uibase.command.ResultBack;
import com.example.uibase.utils.WebConstants;
import com.example.webviewdemo.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建时间：2020/3/19
 * 创建人：singleCode
 * 功能描述：
 **/
public class WebViewActivity extends AppCompatActivity {
    BaseWebViewFragment webviewFragment;
    private String title;
    private String url;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_web);
        title = getIntent().getStringExtra(WebConstants.INTENT_TAG_TITLE);
        url = getIntent().getStringExtra(WebConstants.INTENT_TAG_URL);
        setTitle(title);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        CommandsManager.getInstance().registerCommand(WebConstants.LEVEL_LOCAL, titleUpdateCommand);
        int level = getIntent().getIntExtra("level", WebConstants.LEVEL_BASE);
        webviewFragment = null;
        if (level == WebConstants.LEVEL_BASE) {
            webviewFragment = BaseWebFragment.newInstance(url);
        } else {
            webviewFragment = AccountWebFragment.newInstance(url, (HashMap<String, String>) getIntent().getExtras().getSerializable(WebConstants.INTENT_TAG_HEADERS), true);
        }
        transaction.replace(R.id.web_view_fragment, webviewFragment).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    /**
     * 页面路由
     */
    private final Command titleUpdateCommand = new Command() {
        @Override
        public String actionName() {
            return Command.COMMAND_UPDATE_TITLE;
        }

        @Override
        public void exec(Context context, Map params, ResultBack resultBack) {
            if(params.containsKey(Command.COMMAND_UPDATE_TITLE_PARAMS_TITLE)) {
                setTitle((String)params.get(Command.COMMAND_UPDATE_TITLE_PARAMS_TITLE));
            }
        }
    };
}
