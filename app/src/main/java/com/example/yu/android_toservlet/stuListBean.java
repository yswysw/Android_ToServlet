package com.example.yu.android_toservlet;

import java.util.List;

/**
 * Created by yu on 2017/3/4.
 */

public class stuListBean {


    /**
     * headImg : http://192.168.191.1:8080/android_match_sql/img/a.jpg
     * sex : boy
     * name : 1
     * id : 1
     * age : 1
     */

    private List<StuListBean> stuList;

    public List<StuListBean> getStuList() {
        return stuList;
    }

    public void setStuList(List<StuListBean> stuList) {
        this.stuList = stuList;
    }

    public static class StuListBean {
        private String headImg;
        private String sex;
        private String name;
        private int id;
        private int age;

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
