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

    public static String generateShareCode(){
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    public static boolean isAnyStringNullOrEmpty(String... strings) {
        for (String s : strings)
            if (s == null || s.isEmpty())
                return true;
        return false;
    }
}
