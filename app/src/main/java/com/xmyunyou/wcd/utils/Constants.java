package com.xmyunyou.wcd.utils;

/**
 * Created by sanmee on 2014/12/10.
 */
public class Constants {

    public static final String PAGE_SIZE = "10";
    public static final int PAGE_SIZE_ = 10;

    private static final String HOST_URL = "http://data.wanchezhijia.com/data.ashx?action=";

    //获取分类
    public static final String CATEGORY_LIST = HOST_URL + "getnewscategories";

    //发表评论
    public static final String CREATE_COMMENT = HOST_URL + "CreateNewsComment";

    //获取图片预览
    public static final String PIC_LIST = "http://www.wanchezhijia.com/api/getnewsimagelist";

    //评论
    public static final String COMMENT = HOST_URL + "getnewscomments";

    //幻灯片
    public static final String BANNER_LIST = HOST_URL + "getjiaodiannewsformobile";

    //首页文章
    public static final String ARTICLE_LIST = HOST_URL + "getparentnewsformobile";

    //首页文章和幻灯片接口
    public static  final  String HOST_ALL_URL = "http://data.wanchedang.com/data.ashx?action=getmobileindex";

    //获取文章详情
    public static final String ARTICLE_DETAIL = HOST_URL + "getwpnewsbyid";

    //根据类型获取文章
    public static final String ARTICLE_CATEGORY_LIST = HOST_URL + "getwpnewsforadmin";

    //版本更新
    public static final String UPDATE_VERSION = HOST_URL + "wcdcheckupdate";

    //登录
    public static final String USER_LOGIN = HOST_URL+"userlogin";

    //注册
    public static final String USER_REGISTER = HOST_URL+"createuser";

    //意见反馈提交

    public static final String OPINION_SUBMIT = HOST_URL+ "createfeedback";

    //意见反馈列表

    public static final String OPINION_LIST = HOST_URL+"getfeedbacks";

    //重置密码
    public static final String RESETPWD = HOST_URL + "resetpassword";

    //找回密码
    public static final String SENDPASSWORD = HOST_URL + "sendpassword";

    //RSA
    public static final String RSA = "http://www.wanchezhijia.com/ajax/getrsakey";

    //添加收藏
    public static final String FAV = HOST_URL +"createmyfavrsa";

    //删除收藏
    public static final String DEL_FAV = HOST_URL + "deletemyfavbyuser";

    //获取收藏
    public static final String GET_FAV = HOST_URL + "getmyfavsformy";

    //搜索
    public static final String SEARCH_LIST =HOST_URL +"searchwpnews";

    //搜索热门车型起始页
    public static final String SEARCH_LIST_INIT = HOST_URL + "gethotcarmodels";

    //搜索获取所有品牌

    public static final String SEARCH_ALL = HOST_URL + "getallcarmanufacturers";

    //搜索获取某个品牌汽车系列

    public static final String SEARCH_ALL_LINE = HOST_URL + "getcarmodelsbymid";

    //获取单个品牌汽车系列的所有车型
    public static final String SEARCH_ALL_LINE_TYPE = HOST_URL+"getcars";

    //获取搜索目录的接口

    public static final String SEARCH_CATALOGUE = HOST_URL +"GetNewsForCarModelID";

    //获取搜索车型配件接口

    public static final String SEARCH_CAR_PRODUCT = HOST_URL + "getallgzj";

    //获取搜索车型配件跳转接口

    public static final String SEARCH_CAR_PRODUCT_RETURN = HOST_URL + "GetGZJNews";

//    获取改装店首页接口

    public static final String  GAIZHUANG_SERVER = HOST_URL+"getgaizhuangmobileindex";

    //获取改装店店铺首页接口

    public static final String GAIZHUANG_STOP = HOST_URL +"getgaizhuangmobileindexshangjia";

    //获取改装店铺第二页之后接口

    public static final String GAIZHUANG_STOP_NETX = HOST_URL+"getshopsbyshopserviceformobile";

    //获取改装店改装作品接口

    public static final String  GAIZHUANG_PRODUCT =  HOST_URL + "getnewsforspdc";

    //获取改装店店铺详情页接口

    public static final String GAIZHUANG_STOP_DETAILS = HOST_URL + "getshopbyidformobile";

    //获取改装店铺，店铺评论

    public static final String GAIZHUANG_SHOP_COMMENT = HOST_URL + "getshopcomments";

    //签到
    public static final String CREATE_SIGN = HOST_URL + "createcheckin";

    //广场接口
    public static final String CIRCLE_BBS_INDEX = HOST_URL + "getbbsindex";

    //回复列表
    public static final String REPLY_LIST = HOST_URL + "getreplybytopicid";

    //发表回复
    public static final String SEND_REPLY = HOST_URL + "createreply";

    //获取帖子详情
    public static final String TOPIC_DETAIL = HOST_URL + "gettopicbyidandreply";

    public static String getMedalPic(String id) {
        return getMedalSmallPic(id);
    }

    public static String getMedalBigPic(String id) {
        return "http://img.shouyouzhijia.net/medals/big/" + id + ".png";
    }

    private static String getMedalSmallPic(String id) {
        return "http://img.shouyouzhijia.net/medals/small/" + id + ".png";
    }

    //帖子列表
    public static final String TOPIC_LIST = HOST_URL + "gettopicbygameidnocontent";

    //用户详情页
    public static final String USER_DETAIL = HOST_URL + "getuserprofilebyuserid";

    //上传图片 UserID RSA   头像：userpic  发帖回复：bbspic
    public static final String UPLOAD_PIC = "http://img.wanchezhijia.com/upload.ashx";



    public static final String CREATE_TOPIC =  HOST_URL + "createtopic";

    //我的帖子列表
    public static final String MY_POSTS_LIST = HOST_URL + "gettopicbyuserid";

    //我的积分列表
    public static final String MY_JIFEN_LIST = HOST_URL+"getjifenlogs";

    //我的车型
    public static final String MY_CAR = HOST_URL+"getcar";

    //上传我的车型ID
    public static final String MY_CAR_ID = HOST_URL+"changecaridrsa";

    //提交商家店铺评价
    public static final String GAIZHUANG_STOP_COMMENT = HOST_URL + "createshopcomment";

}
