package com.example.sqllitedb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtRollno,edtName,edtMarks;
    Button btnAdd,btnDelt,btnModify,btnView,btnViewAll,btnShow;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtRollno=findViewById(R.id.editRollno);
        edtName=findViewById(R.id.editName);
        edtMarks=findViewById(R.id.editMarks);

        btnAdd=findViewById(R.id.btnAdd);
        btnDelt=findViewById(R.id.btnDelete);
        btnModify=findViewById(R.id.btnModify);
        btnView=findViewById(R.id.btnView);
        btnViewAll=findViewById(R.id.btnViewAll);
        btnShow=findViewById(R.id.btnShow);

        btnAdd.setOnClickListener(this);
        btnDelt.setOnClickListener(this);
        btnModify.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnViewAll.setOnClickListener(this);
        btnShow.setOnClickListener(this);

        sqLiteDatabase=openOrCreateDatabase("StaudentDB",MODE_PRIVATE,null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR,name VARCHAR,marks VARCHAR);");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAdd:
                if (edtRollno.getText().toString().trim().length()==0 ||
                        edtName.getText().toString().trim().length()==0 ||
                        edtMarks.getText().toString().trim().length()==0){
                    Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
                    showMessage("Error","Invalid input");
                    return;
                }
                sqLiteDatabase.execSQL("INSERT INTO student VALUES('"+edtRollno.getText()+"','"+edtName.getText()+
                        "','"+edtMarks.getText()+"');");
                showMessage("Success", "Record added");
                clearText();
                Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnDelete:
                if(edtRollno.getText().toString().trim().length()==0)
                {
                    showMessage("Error", "Please enter Rollno");
                    return;
                }
                Cursor c=sqLiteDatabase.rawQuery("SELECT * FROM student WHERE rollno='"+edtRollno.getText()+"'", null);
                if(c.moveToFirst())
                {
                    sqLiteDatabase.execSQL("DELETE FROM student WHERE rollno='"+edtRollno.getText()+"'");
                    showMessage("Success", "Record Deleted");
                }
                else
                {
                    showMessage("Error", "Invalid Rollno");
                }
                clearText();
                break;

            case R.id.btnModify:
                if(edtRollno.getText().toString().trim().length()==0)
                {
                    showMessage("Error", "Please enter Rollno");
                    return;
                }
                Cursor c1=sqLiteDatabase.rawQuery("SELECT * FROM student WHERE rollno='"+edtRollno.getText()+"'", null);
                if(c1.moveToFirst())
                {
                    sqLiteDatabase.execSQL("UPDATE student SET name='"+edtName.getText()+"',marks='"+edtMarks.getText()+
                            "' WHERE rollno='"+edtRollno.getText()+"'");
                    showMessage("Success", "Record Modified");
                }
                else
                {
                    showMessage("Error", "Invalid Rollno");
                }
                clearText();
                break;

            case R.id.btnView:
                if(edtRollno.getText().toString().trim().length()==0)
                {
                    showMessage("Error", "Please enter Rollno");
                    return;
                }
                Cursor c2=sqLiteDatabase.rawQuery("SELECT * FROM student WHERE rollno='"+edtRollno.getText()+"'", null);
                if(c2.moveToFirst())
                {
                    edtName.setText(c2.getString(1));
                    edtMarks.setText(c2.getString(2));
                }
                else
                {
                    showMessage("Error", "Invalid Rollno");
                    clearText();
                }
                break;
            case R.id.btnViewAll:
                Cursor c3=sqLiteDatabase.rawQuery("SELECT * FROM student", null);
                if(c3.getCount()==0)
                {
                    showMessage("Error", "No records found");
                    return;
                }
                StringBuffer buffer=new StringBuffer();
                while(c3.moveToNext())
                {
                    buffer.append("Rollno: "+c3.getString(0)+"\n");
                    buffer.append("Name: "+c3.getString(1)+"\n");
                    buffer.append("Marks: "+c3.getString(2)+"\n\n");
                }
                showMessage("Student Details", buffer.toString());
                break;
            case R.id.btnShow:
                Toast.makeText(this, "Show", Toast.LENGTH_SHORT).show();
                showMessage("Developed By-","Ms. Seema Kale");
                break;
        }

    }

    private void clearText() {
        edtMarks.setText("");
        edtName.setText("");
        edtRollno.setText("");
    }

    private void showMessage(String title, String msg) {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
        alertDialog.setCancelable(true);
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setIcon(R.mipmap.ic_launcher_round);
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
