package com.example.crossandcircle;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GameField extends ConstraintLayout {

    //VAR
    ImageView fieldImgView;
    int xPos,yPos;
    boolean isEmpty;
    int fieldType;

    //METHODS
    public ImageView setFieldImage(int type,Context c){
        switch (type){
            case 0:
                fieldImgView = new ImageView(c);
                fieldImgView.setImageResource(R.drawable.empty_t);
                this.fieldType = 0; //EMPTY
                break;
            case 1:
                fieldImgView = new ImageView(c);
                fieldImgView.setImageResource(R.drawable.cross_t);
                this.fieldType = 1; //CROSS
                break;
            case 2:
                fieldImgView = new ImageView(c);
                fieldImgView.setImageResource(R.drawable.circle_t);
                this.fieldType = 2; //CIRCLE
                break;
        }
        fieldImgView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        this.addView(fieldImgView);
        this.isEmpty = false;
        return fieldImgView;
    }
    public void removeImage(Context c){
        this.removeAllViews();
        this.isEmpty = true;
    }
    //MAIN METHOD
    public GameField(Context context,int xP,int yP,int type) {
        super(context);
        xPos = xP;
        yPos = yP;
        setFieldImage(type,context);
    }
}