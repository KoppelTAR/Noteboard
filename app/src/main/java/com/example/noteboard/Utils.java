package com.example.noteboard;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class Utils {
    public static boolean isEditTextEmpty(EditText editText, Context context){
        if(editText.getText().toString().isEmpty()){
            Toast.makeText(context, context.getString(R.string.emptyInput),Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
