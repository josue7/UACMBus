package mobauacm.naveli.com.uacmbus;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Comentario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentario);
    }

    public void enviar(View v){
        String code = PasswordGenerator.getPassword(6);
        final String commit;
        EditText name = (EditText) findViewById(R.id.text_nombre);
        EditText comentario = (EditText) findViewById(R.id.text_comentarios);
        if (!comentario.getText().toString().isEmpty()){
            DatabaseReference busFirebase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference db;

            commit = name.getText().toString() + " ... " + comentario.getText().toString();

            db = busFirebase .child("com");

            db.child(code).setValue(commit)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Write was successful!
                            // ...
                            Toast.makeText(Comentario.this, "Gracias por enviar su comentario", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Write failed
                            // ...
                            Toast.makeText(Comentario.this, "Error al enviar el mensaje", Toast.LENGTH_SHORT).show();
                        }

                    });

            name.setText("");
            comentario.setText("");

        }else {
            Toast.makeText(Comentario.this, "El campo de comentario no puede estar vacio", Toast.LENGTH_SHORT).show();
        }

    }
}
