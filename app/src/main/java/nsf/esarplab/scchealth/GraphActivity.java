package nsf.esarplab.scchealth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;


public class GraphActivity extends Activity {

    EditText num1, num2, num3, num4, num5;
    Button btnShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

       /* num1 = (EditText)findViewById(R.id.num1);
        num2 = (EditText)findViewById(R.id.num2);
        num3 = (EditText)findViewById(R.id.num3);
        num4 = (EditText)findViewById(R.id.num4);
        num5 = (EditText)findViewById(R.id.num5);*/
        btnShow = (Button)findViewById(R.id.show);

        btnShow.setOnClickListener(btnShowOnClickListener);
    }

    OnClickListener btnShowOnClickListener =
            new OnClickListener(){

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(
                            GraphActivity.this,
                            ShowWebChart.class);

                   /* intent.putExtra("NUM1", getNum(num1));
                    intent.putExtra("NUM2", getNum(num2));
                    intent.putExtra("NUM3", getNum(num3));
                    intent.putExtra("NUM4", getNum(num4));
                    intent.putExtra("NUM5", getNum(num5));
*/

                    intent.putExtra("NUM1", 40);
                    intent.putExtra("NUM2", 50);
                    intent.putExtra("NUM3", 70);
                    intent.putExtra("NUM4", 35);
                    intent.putExtra("NUM5", 50);

                    startActivity(intent);
                }

            };

    private int getNum(EditText editText){

        int num = 0;

        String stringNum = editText.getText().toString();
        if(!stringNum.equals("")){
            num = Integer.valueOf(stringNum);
        }

        return (num);
    }

}