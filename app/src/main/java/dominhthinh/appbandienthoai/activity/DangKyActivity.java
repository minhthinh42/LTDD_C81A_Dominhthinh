package dominhthinh.appbandienthoai.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.caosiqui.quicaoit.appbandienthoaididong.R;
import dominhthinh.appbandienthoai.api.ApiHelper;
import dominhthinh.appbandienthoai.model.Taikhoan;
import dominhthinh.appbandienthoai.ultil.CheckConnection;

import retrofit2.Call;
import retrofit2.Callback;

import static android.widget.Toast.LENGTH_SHORT;

public class DangKyActivity extends AppCompatActivity {

    EditText txtUsername;
    EditText txtPassword;
    EditText txtFullname;
    EditText txtPhone;
    EditText txtEmail;
    EditText txtAddress;
    Button btnRegister;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        initIteminActivity();
        EventButton();
    }

    //    Ánh xạ element
    void initIteminActivity(){
        txtUsername = (EditText) findViewById(R.id.txtUserName);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtFullname = (EditText) findViewById(R.id.txtFullname);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        btnRegister = (Button) findViewById(R.id.btnRegister);
    }

    //    event login
    private void EventButton() {

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtUsername.getText().length()==0 || txtPassword.getText().length()==0 || txtFullname.getText().length()==0 || txtPhone.getText().length()==0||txtEmail.getText().length()==0|| txtAddress.getText().length()==0)
                    Toast.makeText(getBaseContext(),"Vui lòng nhập đầy đủ", LENGTH_SHORT).show();
                else{
                    if (CheckConnection.haveNetworkConnection(getApplicationContext())){
                        themmoitaikhoan();
                    }else {
                        CheckConnection.ShoToast_Short(getApplicationContext(),"Hãy kiểm tra lại kết nối");
                    }
                }
            }
        });

    }



    private void  themmoitaikhoan() {
        final String tenkhachhang = txtFullname.getText().toString().trim();
        final String sodienthoai = txtPhone.getText().toString().trim();
        final String email = txtEmail.getText().toString().trim();
        final String diachi = txtAddress.getText().toString().trim();
        final String tendn = txtUsername.getText().toString().trim();
        final String matkhau = txtPassword.getText().toString().trim();
        Call<Taikhoan> taikhoan  = ApiHelper.getAPIService().addData(tendn,matkhau,tenkhachhang,sodienthoai,email,diachi);

        taikhoan.enqueue(new Callback<Taikhoan>() {
            @Override
            public void onResponse(Call<Taikhoan> call, retrofit2.Response<Taikhoan> response) {
                if(!response.body().getTendangnhap().isEmpty()){
                    DangNhapActivity.idTaikoan = response.body().getID();
                    Toast.makeText(getBaseContext(),"Đăng ký thành công", LENGTH_SHORT).show();
                    Intent intent = new Intent(DangKyActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getBaseContext(),"Đăng ký thất bại", LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Taikhoan> call, Throwable t) {
                Toast.makeText(getBaseContext(),"Đăng ký thất bại", LENGTH_SHORT).show();
            }
        });
    }
}

