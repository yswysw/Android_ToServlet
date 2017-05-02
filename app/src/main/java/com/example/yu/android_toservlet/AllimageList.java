package com.example.yu.android_toservlet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

public class AllimageList extends AppCompatActivity {

    private static final String TAG = "AllimageList";
    private RecyclerView mRecyclerView;
    private ImgMyAdapter mImgMyAdapter;
    private Gson mGson;
    private final String IMG_URL = "http://192.168.191.1:8080/android_match_sql/Imgs";
    private imgListBean imgs;
    private final String UPDATE_IMG_URL = "http://192.168.191.1:8080/android_match_sql/updateImg";
    private int StuId;
    private List<imgListBean.ImgListBean> mImgList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allimage_list);
        
        initViews();
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);
        final GridLayoutManager manager = new GridLayoutManager(this,3);

        OkHttpUtils.get().url(IMG_URL).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                imgs = new imgListBean();
                mGson = new Gson();
                imgs = mGson.fromJson(response.toString(),imgListBean.class);
                mImgList = imgs.getImgList();//得到所有图片集合。
                mImgMyAdapter = new ImgMyAdapter(mImgList);
                mRecyclerView.setLayoutManager(manager);
                mRecyclerView.setAdapter(mImgMyAdapter);
                mImgMyAdapter.notifyDataSetChanged();
            }
        });
        Intent intent = getIntent();
        StuId = intent.getIntExtra("id", 1);//跳转到选择图片的Activity的时候同时初始化StuId
        Toast.makeText(this, ""+StuId, Toast.LENGTH_SHORT).show();



    }


    class ImgMyAdapter extends RecyclerView.Adapter<ImgMyAdapter.ViewHolder>{

        private final List<imgListBean.ImgListBean> imgList;

        public ImgMyAdapter(List<imgListBean.ImgListBean> imgList) {
            this.imgList = imgList;
        }

        @Override
        public ImgMyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.img_list_item,parent,false);
            ViewHolder holder = new ViewHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ImgMyAdapter.ViewHolder holder, int position) {
            imgListBean.ImgListBean pic = imgList.get(position);
            Glide.with(AllimageList.this).load(pic.getImg()).into(holder.img);

            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = holder.getAdapterPosition();
                    //对应某个条目位置的图片，点击得到的图片。
                    imgListBean.ImgListBean image = imgList.get(index);

                    OkHttpUtils.get().url(UPDATE_IMG_URL)//选择图片UPDATE_IMG_URL，控制台打印   因为更新图片的方法中包含打印语句。
                            .addParams("id",String.valueOf(StuId))//根据id更新图片，图片的id只是存储学生的输送过来的id。
                            .addParams("headImg",image.getImg())//
                            .build().execute(new StringCallback() {//细节：hy的包装类用的都是 new StringCallback()方法。
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.e(TAG, "onResponse: " + "--------------------------------------------------------");
                            if(response.toString().equals("yes")){
                                Intent i = new Intent(AllimageList.this,MainActivity.class);
                                startActivity(i);
                                finish();
                            }else {
                                Toast.makeText(AllimageList.this, "更新失败！！！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });
        }

        @Override
        public int getItemCount() {
            return imgList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView img;

            public ViewHolder(View itemView) {
                super(itemView);
                img = (ImageView) itemView.findViewById(R.id.img);
            }
        }
    }
}
