package com.xmyunyou.wcd.ui.user;

import android.os.Bundle;
import android.widget.TextView;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Opinion;
import com.xmyunyou.wcd.utils.BaseActivity;
import com.xmyunyou.wcd.utils.Globals;

/**
 * Created by 95 on 2015/2/9.
 */
public class FeedBackDetailActivity extends BaseActivity{
    public static final String PARAMS_INFO_FEED_BACK = "PARAMS_INFO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_detail);
        TextView title = (TextView) findViewById(R.id.feed_back_title);
        TextView date = (TextView) findViewById(R.id.feed_back_date);
        TextView content = (TextView) findViewById(R.id.feed_back_content);
        TextView reply = (TextView) findViewById(R.id.feed_back_reply_content);

        Opinion o = (Opinion) getIntent().getSerializableExtra(PARAMS_INFO_FEED_BACK);
        title.setText(o.getName());
        date.setText(Globals.convertDateHHMM(o.getCreateDate()));
        content.setText(o.getContent());
        reply.setText(o.getReply());
    }
}
