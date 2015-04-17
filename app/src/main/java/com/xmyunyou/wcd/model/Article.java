package com.xmyunyou.wcd.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanmee on 2015/1/12.
 */
public class Article implements Serializable{

    public int ID;
    public int CategoryID;
    public String CategoryName;
    public String Title;
    public String Description;
    public String Content;
    public int Target;
    public String CreateDate;
    public String UpdateDate;
    public String ImageUrl;
    public String ProductID;
    public String TargetUrl;
    public int Order;
    public String ImageUrl145145;
    public int IsCheck;
    public String Keywords;
    public int CommentNums;
    // 查看次数；先不加，待定
    public int ViewNums;
    public String ImageUrlHD;
    public String ImageUrlSmall1;
    public String ImageUrlSmall2;
    public String ImageUrlSmall3;
    public int XingZhi;
    public String Author;
    public int MobileOrder;
    // 汽车ID
    public int CarID;
    // 汽车品牌ID
    public int CarManufacturerID;
    // 汽车系列ID
    public int CarModelID;
    // 关联的改装
    public String GaiZhuangJianIDs;
    // 赞
    public int Zan;
    // 上首页
    public int ShangShouYe;
    // 上栏目页
    public int ShangLanMuYe;
    // 上栏目页幻灯
    public int LanMuYeHuanDeng;
    // 上首页幻灯
    public int ShouYeHuanDeng;
    // 上车系页顶
    public int ShangCheXiYeDing;
    // 上车系页右
    public int ShangCheXiYeYou;
    // 上车文章页右侧
    public int ShangNewsYou;
    // 上车文章页底部
    public int ShangNewsDibu;

    //收藏ID
    public int favID;

    public int getFavID() {
        return favID;
    }

    public void setFavID(int favID) {
        this.favID = favID;
    }

    public List<Article> HotNews = new ArrayList<Article>();
    public List<Article> SuijiNews = new ArrayList<Article>();

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getTarget() {
        return Target;
    }

    public void setTarget(int target) {
        Target = target;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(String updateDate) {
        UpdateDate = updateDate;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getTargetUrl() {
        return TargetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        TargetUrl = targetUrl;
    }

    public int getOrder() {
        return Order;
    }

    public void setOrder(int order) {
        Order = order;
    }

    public String getImageUrl145145() {
        return ImageUrl145145;
    }

    public void setImageUrl145145(String imageUrl145145) {
        ImageUrl145145 = imageUrl145145;
    }

    public int getIsCheck() {
        return IsCheck;
    }

    public void setIsCheck(int isCheck) {
        IsCheck = isCheck;
    }

    public String getKeywords() {
        return Keywords;
    }

    public void setKeywords(String keywords) {
        Keywords = keywords;
    }

    public int getCommentNums() {
        return CommentNums;
    }

    public void setCommentNums(int commentNums) {
        CommentNums = commentNums;
    }

    public int getViewNums() {
        return ViewNums;
    }

    public void setViewNums(int viewNums) {
        ViewNums = viewNums;
    }

    public String getImageUrlHD() {
        return ImageUrlHD;
    }

    public void setImageUrlHD(String imageUrlHD) {
        ImageUrlHD = imageUrlHD;
    }

    public String getImageUrlSmall1() {
        return ImageUrlSmall1;
    }

    public void setImageUrlSmall1(String imageUrlSmall1) {
        ImageUrlSmall1 = imageUrlSmall1;
    }

    public String getImageUrlSmall2() {
        return ImageUrlSmall2;
    }

    public void setImageUrlSmall2(String imageUrlSmall2) {
        ImageUrlSmall2 = imageUrlSmall2;
    }

    public String getImageUrlSmall3() {
        return ImageUrlSmall3;
    }

    public void setImageUrlSmall3(String imageUrlSmall3) {
        ImageUrlSmall3 = imageUrlSmall3;
    }

    public int getXingZhi() {
        return XingZhi;
    }

    public void setXingZhi(int xingZhi) {
        XingZhi = xingZhi;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public int getMobileOrder() {
        return MobileOrder;
    }

    public void setMobileOrder(int mobileOrder) {
        MobileOrder = mobileOrder;
    }

    public int getCarID() {
        return CarID;
    }

    public void setCarID(int carID) {
        CarID = carID;
    }

    public int getCarManufacturerID() {
        return CarManufacturerID;
    }

    public void setCarManufacturerID(int carManufacturerID) {
        CarManufacturerID = carManufacturerID;
    }

    public int getCarModelID() {
        return CarModelID;
    }

    public void setCarModelID(int carModelID) {
        CarModelID = carModelID;
    }

    public String getGaiZhuangJianIDs() {
        return GaiZhuangJianIDs;
    }

    public void setGaiZhuangJianIDs(String gaiZhuangJianIDs) {
        GaiZhuangJianIDs = gaiZhuangJianIDs;
    }

    public int getZan() {
        return Zan;
    }

    public void setZan(int zan) {
        Zan = zan;
    }

    public int getShangShouYe() {
        return ShangShouYe;
    }

    public void setShangShouYe(int shangShouYe) {
        ShangShouYe = shangShouYe;
    }

    public int getShangLanMuYe() {
        return ShangLanMuYe;
    }

    public void setShangLanMuYe(int shangLanMuYe) {
        ShangLanMuYe = shangLanMuYe;
    }

    public int getLanMuYeHuanDeng() {
        return LanMuYeHuanDeng;
    }

    public void setLanMuYeHuanDeng(int lanMuYeHuanDeng) {
        LanMuYeHuanDeng = lanMuYeHuanDeng;
    }

    public int getShouYeHuanDeng() {
        return ShouYeHuanDeng;
    }

    public void setShouYeHuanDeng(int shouYeHuanDeng) {
        ShouYeHuanDeng = shouYeHuanDeng;
    }

    public int getShangCheXiYeDing() {
        return ShangCheXiYeDing;
    }

    public void setShangCheXiYeDing(int shangCheXiYeDing) {
        ShangCheXiYeDing = shangCheXiYeDing;
    }

    public int getShangCheXiYeYou() {
        return ShangCheXiYeYou;
    }

    public void setShangCheXiYeYou(int shangCheXiYeYou) {
        ShangCheXiYeYou = shangCheXiYeYou;
    }

    public int getShangNewsYou() {
        return ShangNewsYou;
    }

    public void setShangNewsYou(int shangNewsYou) {
        ShangNewsYou = shangNewsYou;
    }

    public int getShangNewsDibu() {
        return ShangNewsDibu;
    }

    public void setShangNewsDibu(int shangNewsDibu) {
        ShangNewsDibu = shangNewsDibu;
    }

    public List<Article> getHotNews() {
        return HotNews;
    }

    public void setHotNews(List<Article> hotNews) {
        HotNews = hotNews;
    }

    public List<Article> getSuijiNews() {
        return SuijiNews;
    }

    public void setSuijiNews(List<Article> suijiNews) {
        SuijiNews = suijiNews;
    }
}
