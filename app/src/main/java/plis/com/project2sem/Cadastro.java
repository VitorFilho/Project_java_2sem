package plis.com.project2sem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Cadastro extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button btnCadastro;
    private Button btnExit;
    String[] mensagens = {"Preencha todos os campos","Cadastro realizado com sucesso"};
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        getSupportActionBar().hide();
        IniciarComponentes();

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Cadastro.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String senha = editTextPassword.getText().toString();
                if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()){//se o nome/email/Password estiverem vazio
                    Snackbar snackbar = Snackbar.make(view,mensagens[0],Snackbar.LENGTH_SHORT);// escolha da mensagem e o tempo de visualização
                    snackbar.setBackgroundTint(Color.WHITE);//Cor de fundo na msg
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else{
                    email = editTextEmail.getText().toString();
                    senha = editTextPassword.getText().toString();
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){ // se o cadastro for realizado com sucesso

                                CadastrarUsuario(view);

                                Snackbar snackbar = Snackbar.make(view,mensagens[1],Snackbar.LENGTH_SHORT);// escolha da mensagem e o tempo de visualização
                                snackbar.setBackgroundTint(Color.WHITE);
                                snackbar.setTextColor(Color.BLACK);
                                snackbar.show();
                            }else{
                                String erro;
                                try{
                                    throw task.getException(); //throw vai tentar obter uma exceção

                                }catch (FirebaseAuthWeakPasswordException e){//tratar msg de erro de senha
                                    erro = "Digite uma senha com no minimo 6 caracteres";

                                }catch (FirebaseAuthUserCollisionException e){//tratar msg de erro de duplicidade de email
                                    erro = "Está conta já esta cadastrada";

                                }catch (FirebaseAuthInvalidCredentialsException e){// tratar msg de erro de digitação de email
                                    erro = "Email inválido ";

                                }catch (Exception e){
                                    erro = "Erro ao cadastrar usuário";
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

    private void CadastrarUsuario(View view){
        String email = editTextEmail.getText().toString();
        String senha = editTextPassword.getText().toString();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){ // se o cadastro for realizado com sucesso

                    SalvarDadosUsuario();

                    Snackbar snackbar = Snackbar.make(view,mensagens[1],Snackbar.LENGTH_SHORT);// escolha da mensagem e o tempo de visualização
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else{
                    String erro;
                    try{
                        throw task.getException(); //throw vai tentar obter uma exceção

                    }catch (FirebaseAuthWeakPasswordException e){//tratar msg de erro de senha
                        erro = "Digite uma senha com no minimo 6 caracteres";

                    }catch (FirebaseAuthUserCollisionException e){//tratar msg de erro de duplicidade de email
                        erro = "Está conta já esta cadastrada";

                    }catch (FirebaseAuthInvalidCredentialsException e){// tratar msg de erro de digitação de email
                        erro = "Email inválido ";

                    }catch (Exception e){
                        erro = "Erro ao cadastrar usuário";
                    }
                    Snackbar snackbar = Snackbar.make(view,erro,Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }

    private void IniciarComponentes(){
        editTextName     = findViewById(R.id.editTextName);
        editTextEmail    = findViewById(R.id.editText_Email);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnCadastro = findViewById(R.id.btnCadastro);
        btnExit = findViewById(R.id.btnExit);

    }

    /*public  void onClickCadastrar(View view){
        editTextName     = findViewById(R.id.editTextName);
        editTextEmail    = findViewById(R.id.editText_Email);
        editTextPassword = findViewById(R.id.editTextPassword);

        String nome = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String senha = editTextPassword.getText().toString();
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()){//se o nome/email/Password estiverem vazio
            Snackbar snackbar = Snackbar.make(view,mensagens[0],Snackbar.LENGTH_SHORT);// escolha da mensagem e o tempo de visualização
            snackbar.setBackgroundTint(Color.WHITE);//Cor de fundo na msg
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
        }else{
            //email = editTextEmail.getText().toString();
            //senha = editTextPassword.getText().toString();
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){ // se o cadastro for realizado com sucesso

                        SalvarDadosUsuario();

                        Snackbar snackbar = Snackbar.make(view,mensagens[1],Snackbar.LENGTH_SHORT);// escolha da mensagem e o tempo de visualização
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }else{
                        String erro;
                        try{
                            throw task.getException(); //throw vai tentar obter uma exceção

                        }catch (FirebaseAuthWeakPasswordException e){//tratar msg de erro de senha
                            erro = "Digite uma senha com no minimo 6 caracteres";

                        }catch (FirebaseAuthUserCollisionException e){//tratar msg de erro de duplicidade de email
                            erro = "Está conta já esta cadastrada";

                        }catch (FirebaseAuthInvalidCredentialsException e){// tratar msg de erro de digitação de email
                            erro = "Email inválido ";

                        }catch (Exception e){
                            erro = "Erro ao cadastrar usuário";
                        }
                        Snackbar snackbar = Snackbar.make(view,erro,Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }
                }
            });
        }
    }*/
    private void SalvarDadosUsuario() {
        String nome = editTextName.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object>usuarios = new HashMap<>();
        usuarios.put("nome",nome);
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("db", "Sucesso ao salvar os dados");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db_error", "Erro ao salvar os dados" + e.toString());//concatenando erro com a Exception
            }
        });

        }
    }

