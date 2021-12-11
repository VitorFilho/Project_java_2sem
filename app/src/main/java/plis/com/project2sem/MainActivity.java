package plis.com.project2sem;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private EditText editText_Email;
    private EditText editText_Password;
    private Button btnLogin;
    String[] mensagens = {"Preencha todos os campos","Login Efetuado com sucesso"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();//esconde a primeira  barra

        IniciarComponentes();

        btnLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String email = editText_Email.getText().toString();
                String password = editText_Password.getText().toString();

                if (email.isEmpty() || password.isEmpty()){//Se campo estiver vazio
                    Snackbar snackbar = Snackbar.make(view,mensagens[0],Snackbar.LENGTH_SHORT);//Preencha todos os campos
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else{
                    AutenticarUsuario(view);
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){

                                Intent signup = new Intent(MainActivity.this,TelaPrincipal.class);
                                startActivity(signup);
                                FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();// passando usuario atual
                                if (usuarioAtual != null){
                                    TelaPrincipal();
                                }
                            }else{
                                String erro;
                                try {
                                    throw task.getException();
                                }catch (Exception e){
                                    erro = "Erro ao logar usuario";
                                }
                                Snackbar snackbar = Snackbar.make(view,erro,Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(Color.WHITE);
                                snackbar.setTextColor(Color.BLACK);
                                snackbar.show();
                            }
                        }
                    });
                }
            }
        });
    
    }

    private  void AutenticarUsuario(View view){
        String email = editText_Email.getText().toString();
        String password = editText_Password.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){//Se autenticação for um sucesso

                    Intent signup = new Intent(MainActivity.this,TelaPrincipal.class);
                    startActivity(signup);
                    FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();// passando usuario atual
                    if (usuarioAtual != null){
                        TelaPrincipal();
                    }
                }else{
                    String erro;
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        erro = "Erro ao logar usuario";
                    }
                    Snackbar snackbar = Snackbar.make(view,erro,Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }

    private void IniciarComponentes() {
        editText_Email    = findViewById(R.id.editText_Email);//capturando email da activity
        editText_Password =  findViewById(R.id.editText_Password);
        btnLogin =  findViewById(R.id.btnLogin);
        //String email = editText_Email.getText().toString();
        //String password = editText_Password.getText().toString();
    }


  @Override
    protected void onStart() {//criando ciclo de vida, toda vez que inciar aplicação carrega conteudo
        super.onStart();
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();// passando usuario atual
        if (usuarioAtual != null){
            TelaPrincipal();
        }
    }
    private  void TelaPrincipal(){
        Intent signup = new Intent(MainActivity.this,TelaPrincipal.class);
        startActivity(signup);
        finish();
    }

    public void onClicSignup(View v){
        Intent signup = new Intent(MainActivity.this, Cadastro.class);
        startActivity(signup);
    }
}
