package com.example.yu.android_toservlet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

//    private String url = "http://192.168.191.1:8080/android_match_sql/ServletToAndroid";
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "AAAAAAAAAAAA";

    private RecyclerView mRecyclerView;

    private Gson mGson;


    private final String ALL_STU_URL= "http://192.168.191.1:8080/android_match_sql/findAllStu";
    private final String DEL_URL = "http://192.168.191.1:8080/android_match_sql/delete";
    private final String INSERT_URL = "http://192.168.191.1:8080/android_match_sql/insert";
    private final String UPDATE_URL = "http://192.168.191.1:8080/android_match_sql/update";


    private MyAdapter mMyAdapter;

    private Button mbtn_insert;
    private Button mbtn_del;
    private Button mbtn_update;
    private EditText medt_id;
    private EditText medt_name;
    private EditText medt_age;
    private EditText medt_sex;

    private int id;
    private String name;
    private int age;
    private String sex;

    private stuListBean Bean;
    private List<stuListBean.StuListBean> AllStuList;

    private EditText edt_delete;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

    }


    private void findViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mbtn_insert = (Button) findViewById(R.id.btn_insert);
        mbtn_del = (Button) findViewById(R.id.btn_del);
        mbtn_update = (Button) findViewById(R.id.btn_update);
        medt_id = (EditText) findViewById(R.id.edt_id);
        medt_age = (EditText) findViewById(R.id.edt_age);
        medt_sex = (EditText) findViewById(R.id.edt_sex);
        medt_name = (EditText) findViewById(R.id.edt_name);
        edt_delete = (EditText) findViewById(R.id.edt_delete);

        //获取所有学生显示在RecyclerView里面。
        getAllStu();

        //插入
        mbtn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (medt_id.getText().toString().trim().isEmpty() || medt_name.getText().toString().trim().isEmpty() ||
                        medt_age.getText().toString().trim().isEmpty() || medt_sex.getText().toString().trim().isEmpty()
                        ) {
                    Toast.makeText(MainActivity.this, "id,name,age,sex不能为空！！！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //获取编辑框中的信息。
                getContent();
                insert_Student(id,name,age,sex);
                clearEdt();
            }
        });

//      更新
        mbtn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (medt_id.getText().toString().trim().isEmpty() || medt_name.getText().toString().trim().isEmpty() ||
                        medt_age.getText().toString().trim().isEmpty() || medt_sex.getText().toString().trim().isEmpty()
                        ) {
                    Toast.makeText(MainActivity.this, "id,name,age,sex不能为空！！！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //获取编辑框中的信息。
                getContent();
                updateStu(id, name, age, sex);
                clearEdt();
            }
        });

        //删除
        mbtn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edt_delete.getText().toString().trim().isEmpty()){
                    Toast.makeText(MainActivity.this, "id不能为空！！！", Toast.LENGTH_SHORT).show();
                    return;
                }
                int id = Integer.parseInt(edt_delete.getText().toString().trim());
                OkHttpUtils.get().url(DEL_URL).addParams("id",id+"").build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        //更新列表
                        getAllStu();
                    }
                });
                clearEdt();

            }
        });

    }

    private void updateStu(int id, String name, int age, String sex) {
        Map params = new HashMap();
        params.put("id", id+"");
        params.put("name", name);
        params.put("age", age+"");
        params.put("sex", sex);

        OkHttpUtils.get().url(UPDATE_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                getAllStu();
            }
        });


    }

    private void clearEdt() {
        edt_delete.setText("");
        medt_id.setText("");
        medt_name.setText("");
        medt_sex.setText("");
        medt_age.setText("");
    }

    private void insert_Student(int id, String name, int age, String sex) {

        //细节
        Map params = new Hashtable();
        params.put("id", id+"");//o....这是为了转化为字符串加的引号。
        params.put("name", name);
        params.put("age", age+"");//o....这是为了转化为字符串加的引号。
        params.put("sex", sex);

        OkHttpUtils.get().url(INSERT_URL).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                getAllStu();


                Toast.makeText(MainActivity.this,response.toString(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void getContent() {

        id = Integer.parseInt(medt_id.getText().toString().trim());
        name = medt_name.getText().toString().trim();
        age = Integer.parseInt(medt_age.getText().toString().trim());
        sex = medt_sex.getText().toString().trim();
    }

    private void getAllStu() {
         final LinearLayoutManager manager = new LinearLayoutManager(this);


        OkHttpUtils.get().url(ALL_STU_URL).build().execute(new StringCallback() {//选择图片过后，跳转回来还会执行ALL_STU_URL对应的放过。控制台打印。
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
//
//                try {
//                    response = new String(response.getBytes("iso8859-1"),"utf-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                            Log.d(TAG, "onResponse: " + response.toString());
                Bean = new stuListBean();//细节记得要初始化
                mGson = new Gson();
                Bean = mGson.fromJson(response.toString(),stuListBean.class);
                AllStuList = Bean.getStuList();
                mRecyclerView.setLayoutManager(manager);
                mMyAdapter = new MyAdapter(AllStuList);
                mRecyclerView.setAdapter(mMyAdapter);
            }
        });
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private final List<stuListBean.StuListBean> AllStuList;
         ;

        public MyAdapter(List<stuListBean.StuListBean> AllStuList) {
            this.AllStuList = AllStuList;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.sut_item, parent, false);
            ViewHolder holder = new ViewHolder(itemview);

            return holder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
            final stuListBean.StuListBean student = AllStuList.get(position);
            Glide.with(MainActivity.this).load(student.getHeadImg()).into(holder.miv);
            holder.stu_id.setText(String.valueOf(student.getId()));
            holder.stu_name.setText(student.getName());
            holder.stu_age.setText(String.valueOf(student.getAge()));
            holder.stu_sex.setText(student.getSex());


            //更新图片的点击事件。
            holder.miv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent  = new Intent(MainActivity.this,AllimageList.class);
                    intent.putExtra("id",student.getId());//细节：传过去的是学生id。证明图片表中的id为null。
                    startActivity(intent);
                }
            });

        }


        @Override
        public int getItemCount() {
            return AllStuList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView miv;
            private TextView stu_id;
            private TextView stu_name;
            private TextView stu_age;
            private TextView stu_sex;

            public ViewHolder(View itemView) {
                super(itemView);
                miv = (ImageView) itemView.findViewById(R.id.miv);
                stu_id = (TextView) itemView.findViewById(R.id.stu_id);
                stu_name = (TextView) itemView.findViewById(R.id.stu_name);
                stu_age = (TextView) itemView.findViewById(R.id.stu_age);
                stu_sex = (TextView) itemView.findViewById(R.id.stu_sex);

            }
        }
    }


}


