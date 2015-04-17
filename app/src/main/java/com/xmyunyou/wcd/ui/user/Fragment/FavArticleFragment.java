package com.xmyunyou.wcd.ui.user.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xmyunyou.wcd.R;
import com.xmyunyou.wcd.model.Article;
import com.xmyunyou.wcd.model.MyFav;
import com.xmyunyou.wcd.model.json.MyFavList;
import com.xmyunyou.wcd.ui.adapter.ArticleAdapter;
import com.xmyunyou.wcd.ui.user.UserFavactivity;
import com.xmyunyou.wcd.ui.view.LoadMoreView;
import com.xmyunyou.wcd.ui.view.MyScrollView;
import com.xmyunyou.wcd.utils.Constants;
import com.xmyunyou.wcd.utils.DataUtils;
import com.xmyunyou.wcd.utils.RsaHelper;
import com.xmyunyou.wcd.utils.net.RequestListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 95 on 2015/4/10.
 */
public class FavArticleFragment extends Fragment {

    public static final String PARAM_USERID ="PARAM_USERID";
    private UserFavactivity mActivity;
    private MyScrollView mMyScrollView;
    private LoadMoreView mListView;
    private List<Article> mNewsList;

    private ArticleAdapter mFavAdapter;
    private int  uid;
    private String userid;
    private int mPage = 1;
    private int load =1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (UserFavactivity)getActivity();
        uid  = getArguments().getInt(PARAM_USERID,0);
        userid = RsaHelper.encryptDataFromStr(uid + "", DataUtils.getRsaStr(mActivity, DataUtils.RSA_DATA));
        mNewsList = new ArrayList<Article>();
        mFavAdapter = new ArticleAdapter(mActivity, mNewsList,true);
        requestFavArticle();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_userfav_article, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMyScrollView = (MyScrollView)getView().findViewById(R.id.userfav_article_scrollview);
        mListView =(LoadMoreView) getView().findViewById(R.id.userfav_article_list);
        mListView.setAdapter(mFavAdapter);
        mMyScrollView.setOnScrollToBottomLintener(new MyScrollView.OnScrollToBottomListener() {
            @Override
            public void onScrollBottomListener() {
                if ((load+1)>=mPage) {
                    mPage++;
                    requestFavArticle();
                }
            }
        });

    }

    //请求文章列表
    private void requestFavArticle(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("type",1+"");
        params.put("UserID", userid);
        params.put("page", mPage + "");
        params.put("size", Constants.PAGE_SIZE + "");
        mActivity.requestPost(Constants.GET_FAV, params, MyFavList.class, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                MyFavList gl = (MyFavList) result;
                List<Article> list = new ArrayList<Article>();
                if (gl != null) {
                    final int size = gl.getList().size();
                    for (int index = 0; index < size; index++) {
                        MyFav fav = gl.getList().get(index);
                        Article n = fav.getNews();
                        n.setFavID(fav.getID());
                        list.add(n);
                    }
                }
                mNewsList.addAll(list);
                mFavAdapter.notifyDataSetChanged();
                boolean nextPage = (mNewsList.size()) == gl.getTotalCount();
                mListView.onLoadComplete(nextPage);
                load = (int)(gl.getTotalCount()/Integer.valueOf(Constants.PAGE_SIZE));
            }

            @Override
            public void onFailure(String errorMsg) {
                mActivity.showToast(errorMsg);
            }
        });
    }

}
