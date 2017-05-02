package com.example.yu.android_toservlet;

import java.util.List;

/**
 * Created by yu on 2017/3/12.
 */

public class imgListBean {
    /**
     * img : http://192.168.191.1:8080/android_match_sql/img/a.jpg
     * id : 0
     */

    private List<ImgListBean> imgList;

    public List<ImgListBean> getImgList() {
        return imgList;
    }

    public void setImgList(List<ImgListBean> imgList) {
        this.imgList = imgList;
    }

    public static class ImgListBean {
        private String img;
        private int id;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
