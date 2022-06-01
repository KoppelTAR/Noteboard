package com.example.noteboard;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Utils {
    public static boolean isEditTextEmpty(EditText editText, Context context){
        if(editText.getText().toString().isEmpty()){
            Toast.makeText(context, context.getString(R.string.emptyInput),Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public static Map<TimeUnit,Long> getDiffBetweenDates(Date date1, Date date2) {

        long diffInMillies = date2.getTime() - date1.getTime();

        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);

        Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
        long millisRest = diffInMillies;

        for ( TimeUnit unit : units ) {

            long diff = unit.convert(millisRest,TimeUnit.MILLISECONDS);
            long diffInMillisForUnit = unit.toMillis(diff);
            millisRest = millisRest - diffInMillisForUnit;

            result.put(unit,diff);
        }

        return result;
    }
}
