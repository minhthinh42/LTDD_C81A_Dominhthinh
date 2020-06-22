package dominhthinh.appbandienthoai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.caosiqui.quicaoit.appbandienthoaididong.R;
import dominhthinh.appbandienthoai.adapter.LoaispAdapter;
import dominhthinh.appbandienthoai.adapter.SanphamAdapter;
import dominhthinh.appbandienthoai.api.ApiHelper;
import dominhthinh.appbandienthoai.model.Giohang;
import dominhthinh.appbandienthoai.model.Loaisp;
import dominhthinh.appbandienthoai.model.SanPham;
import dominhthinh.appbandienthoai.model.Taikhoan;
import dominhthinh.appbandienthoai.ultil.CheckConnection;
import dominhthinh.appbandienthoai.ultil.Server;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerViewmanhinhchinh;
    NavigationView navigationView;
    ViewFlipper viewFlipper;
    ListView listViewmanhinhchinh;
    DrawerLayout drawerLayout;
    ArrayList<Loaisp> mangloaisp;
    LoaispAdapter loaispAdapter;
    int id = 0;
    String tenloaisp = "";
    String hinhanhloaisp = "";
    ArrayList<SanPham> mangsanpham;
    SanphamAdapter sanPhamAdapter;

    Taikhoan myTaikhoan;

    int myID = -1;

    public static ArrayList<Giohang> manggiohang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            myID = DangNhapActivity.idTaikoan ;
            ActionBar();
            ActionViewLipper();
            GetDuLieuLoaisp();
            GetDuLieuSPMoiNhat();
            CatchOnItemListview(); //bắt sự kiện cho listview sản phẩm

            //get Tài khoản
            getTaiKhoan();
                    
        }else{
            CheckConnection.ShoToast_Short(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối");
            finish();
        }

    }

    void getTaiKhoan(){
        Intent intent = getIntent();
        int id = intent.getIntExtra("id",0);
        Call<Taikhoan> taikhoan  = ApiHelper.getAPIService().findById(myID+"");
        taikhoan.enqueue(new Callback<Taikhoan>() {
            @Override
            public void onResponse(Call<Taikhoan> call, retrofit2.Response<Taikhoan> response) {
                Log.e("abcd",response.message());
                myTaikhoan = new Taikhoan();
                myTaikhoan.setID(response.body().getID());
                myTaikhoan.setTendangnhap(response.body().getTendangnhap());
                myTaikhoan.setSodienthoai(response.body().getSodienthoai());
                myTaikhoan.setEmail(response.body().getEmail());
                myTaikhoan.setDiachi(response.body().getDiachi());
                myTaikhoan.setTenkhachhang(response.body().getTenkhachhang());
                myTaikhoan.setMatkhau(response.body().getMatkhau());
            }

            @Override
            public void onFailure(Call<Taikhoan> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menugiohang:
                Intent intent = new Intent(getApplicationContext(), dominhthinh.appbandienthoai.activity.Giohang.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void CatchOnItemListview() {
        listViewmanhinhchinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this,MainActivity.class);
                            startActivity(intent);
                        }else {
                            CheckConnection.ShoToast_Short(getApplicationContext(),"Hãy kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this,DienThoaiIphoneActivity.class);
                            intent.putExtra("idloaisanpham",mangloaisp.get(position).getId());
                            startActivity(intent);
                        }else {
                            CheckConnection.ShoToast_Short(getApplicationContext(),"Hãy kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 2:
                        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this,DienThoaiSamSungActivity.class);
                            intent.putExtra("idloaisanpham",mangloaisp.get(position).getId());
                            startActivity(intent);
                        }else {
                            CheckConnection.ShoToast_Short(getApplicationContext(),"Hãy kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 3:
                        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this,ThongTinActivity.class);
                            startActivity(intent);
                        }else {
                            CheckConnection.ShoToast_Short(getApplicationContext(),"Hãy kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 4:
                        //change password
                        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this,DoiMatKhauActivity.class);
                            intent.putExtra("id",myTaikhoan.getID());
                            startActivity(intent);
                        }else {
                            CheckConnection.ShoToast_Short(getApplicationContext(),"Hãy kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 5:
                        //update info account
                        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this,UpdateInfoActivity.class);
                            intent.putExtra("tenkhachhang",myTaikhoan.getTenkhachhang());
                            intent.putExtra("email",myTaikhoan.getEmail());
                            intent.putExtra("sodienthoai",myTaikhoan.getSodienthoai());
                            intent.putExtra("diachi",myTaikhoan.getDiachi());
                            startActivity(intent);
                        }else {
                            CheckConnection.ShoToast_Short(getApplicationContext(),"Hãy kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 6:
                        //logout
                        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
                            //FacebookSdk.sdkInitialize(getApplicationContext());
                            //LoginManager.getInstance().logOut();
                            CheckConnection.ShoToast_Short(getApplicationContext(),"Đăng xuất thành công");
                            Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else {
                            CheckConnection.ShoToast_Short(getApplicationContext(),"Hãy kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                }
                }
        });
    }

    private void GetDuLieuSPMoiNhat() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.Duongdansanphammoinhat, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(@Nullable JSONArray response) {
                if (response != null){
                    int ID = 0;
                    String Tensanpham = "";
                    Integer Giasanpham = 0 ;
                    String Hinhanhsanpham = "";
                    String Motasanpham = "";
                    int IDsanpham = 0;
                    for (int i=0; i<response.length();i++){
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            ID =jsonObject.getInt("id");
                            Tensanpham = jsonObject.getString("tensp");
                            Giasanpham = jsonObject.getInt("giasp");
                            Hinhanhsanpham = jsonObject.getString("hinhanhsp");
                            Motasanpham = jsonObject.getString("motasp");
                            IDsanpham = jsonObject.getInt("idsanpham");
                            mangsanpham.add(new SanPham(ID,Tensanpham,Giasanpham,Hinhanhsanpham,Motasanpham,IDsanpham));
                            sanPhamAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void GetDuLieuLoaisp() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.Duongdanloaisp, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(@Nullable JSONArray response) {
                    if (response != null){
                        for (int i=0 ; i<response.length() ; i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                id = jsonObject.getInt("id");
                                tenloaisp = jsonObject.getString("tenloaisp");
                                hinhanhloaisp = jsonObject.getString("hinhanhloaisp");
                                mangloaisp.add(new Loaisp(id,tenloaisp,hinhanhloaisp));
                                loaispAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                         mangloaisp.add(3,new Loaisp(0,"Contacts","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSoFtQg82HdFmVDqtX0RBE0196xo5NBE_Y9Kz9yfsB0hCnQ5w_E"));
                        //thêm mới event tài khoản
                        mangloaisp.add(4,new Loaisp(0,"Change Password",getPathFromAsset("password.png")));
                        mangloaisp.add(5,new Loaisp(0,"Update Info",getPathFromAsset("taikhoan.png")));
                        mangloaisp.add(6,new Loaisp(0,"Logout", getPathFromAsset("logout.png")));
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                CheckConnection.ShoToast_Short(getApplicationContext(),error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    String getPathFromAsset(String nameFile){


        return  "file:///android_asset/"+nameFile;
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }


    private void ActionViewLipper() {
        ArrayList<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT7j4xAjtLZuV8jUHthUBXUThCl0h8PnsjAZGvLGkKvbDRSAJjmuA");
        mangquangcao.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTplJghmrd9qhszMhwH1-9xrayqOI4PczF2IxwCQQm2NOUa3xa4");
        mangquangcao.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcScqM_X5_M_QQx2BhZv-0XHNnCGtZWn6DgpkSz1vKwWTTB5f9A3");
        mangquangcao.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRnpVZbwYtzoo6l-tcSVik3O0dd3Tmg0dIkNpx7ymfkQ2y3SRxw7w");
        mangquangcao.add("http://static.vietnammoi.vn/stores/news_dataimages/tamkc/052017/17/08/croped/thumbnail/galaxy-note-8-co-man-hinh-63-inch-camera-kep-nam-doc-05-.5839.png");
        mangquangcao.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQCkr8jCUaYm3gGlIxV7eghXzOO4W7o1M1_n2a03JuLxTQKpzoAUA");
        for (int i=0;i<mangquangcao.size();i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Picasso.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(5000);//chay torng bao lau
        viewFlipper.setAutoStart(true); //auto start
        Animation animation_slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation animation_slide_out;
        animation_slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setOutAnimation(animation_slide_out);

    }



    private void Anhxa() {
        toolbar = (Toolbar) findViewById(R.id.toolbarmanhinhchinh);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
        recyclerViewmanhinhchinh = (RecyclerView) findViewById(R.id.recyclerview);
        navigationView = (NavigationView) findViewById(R.id.navigationview);
        listViewmanhinhchinh = (ListView) findViewById(R.id.listviewmanhinhchinh);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        mangloaisp = new ArrayList<>();
        mangloaisp.add(0,new Loaisp(0,"Home","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRckyAkbNbBKRnfGeOVjPbZB0x27aTtVfJfBChx5AoPQlbRCreP"));
        loaispAdapter = new LoaispAdapter(mangloaisp,getApplicationContext());
        listViewmanhinhchinh.setAdapter(loaispAdapter);
        mangsanpham = new ArrayList<>();
        sanPhamAdapter = new SanphamAdapter(getApplicationContext(),mangsanpham);
        recyclerViewmanhinhchinh.setHasFixedSize(true);
        recyclerViewmanhinhchinh.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        recyclerViewmanhinhchinh.setAdapter(sanPhamAdapter);



        if (manggiohang != null){

        }else {
            manggiohang = new ArrayList<>();
        }


    }
}
