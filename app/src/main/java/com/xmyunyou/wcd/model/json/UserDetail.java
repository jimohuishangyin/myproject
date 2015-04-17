package com.xmyunyou.wcd.model.json;

import com.xmyunyou.wcd.model.CheckIns;
import com.xmyunyou.wcd.model.Integral;
import com.xmyunyou.wcd.model.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanmee on 2015/4/10.
 */
public class UserDetail {

    private List<Integral> JiFenLog = new ArrayList<>();
    private List<CheckIns> MyCheckIns = new ArrayList<>();
    private List<Topic> Topics = new ArrayList<>();


    public List<Topic> getTopics() {
        return Topics;
    }

    public void setTopics(List<Topic> topics) {
        Topics = topics;
    }

    public List<Integral> getJiFenLog() {
        return JiFenLog;
    }

    public void setJiFenLog(List<Integral> jiFenLog) {
        JiFenLog = jiFenLog;
    }

    public List<CheckIns> getMyCheckIns() {
        return MyCheckIns;
    }

    public void setMyCheckIns(List<CheckIns> myCheckIns) {
        MyCheckIns = myCheckIns;
    }
}
