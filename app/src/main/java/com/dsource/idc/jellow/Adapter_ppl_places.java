package com.dsource.idc.jellow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by HP on 22/01/2017.
 */

public class Adapter_ppl_places extends android.support.v7.widget.RecyclerView.Adapter<Adapter_ppl_places.MyViewHolder> {
    private Context mContext;
    private SessionManager session;

    private int pos;
    public static Integer[] mThumbIds = new Integer[100];
    public static String[] belowText = new String[100];
    int j = 0;
    TextView menuItemBelowText;
    CircularImageView menuItemImage;
    private LinearLayout menuItemLinearLayout;

    public static int people_more = 0;
    public static int places_more = 0;

    int k = 0, l = 0, a = 0,kk=0;

    public Adapter_ppl_places(Context c, int posi, String[] temp, Integer[] image_temp) {
        mContext = c;
        this.pos = posi;
        session = new SessionManager(mContext);

        if (pos == 5) {
            if (session.getLanguage() == 0) {
                mThumbIds = image_temp;
                belowText = temp;

            } else {

                mThumbIds = image_temp;
                belowText = temp;
            }
        } else if (pos == 6) {
            System.out.println("eerr " + image_temp.length);
            if (session.getLanguage() == 0) {
                mThumbIds = image_temp;
                belowText = temp;

                for (int i = 0; i < image_temp.length; i++)
                    System.out.print("places" + belowText[i]);
            } else {
                mThumbIds = image_temp;
                belowText = temp;
            }
        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
    int position = -1;
        Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(),  "fonts/Mukta-Regular.ttf");

        public MyViewHolder(final View view) {

            super(view);
                menuItemImage = (CircularImageView) view.findViewById(R.id.icon1);
                menuItemLinearLayout = (LinearLayout) view.findViewById(R.id.linearlayout_icon1);
                menuItemBelowText = (TextView) view.findViewById(R.id.te1);
                menuItemBelowText.setTypeface(custom_font);
                menuItemBelowText.setTextColor(Color.rgb(64, 64, 64));
        }
    }

    @Override
    public Adapter_ppl_places.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myscrolllist2, parent, false);
       /* DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        if (session.getGridSize()==0){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myscrolllist2, parent, false);
        }else if (dpHeight >= 720 && session.getGridSize()==1)
            rowView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.myscrollist233, parent, false);
        else if (dpWidth >640 && dpWidth <=1024 && session.getGridSize()==1)
            rowView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.myscrollist033, parent, false);
        else if (dpWidth > 600 && dpWidth <=640 && session.getGridSize()==1) {
            rowView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.myscrolllist33, parent, false);
        }else {
            rowView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.myscrolllist33, parent, false);
        }*/

        return new Adapter_ppl_places.MyViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position1) {
        int position;
        j=mThumbIds.length;

        //if (session.getGridSize()==0){
        if (session.getGridSize() == 0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 336, 0, 336);
            menuItemLinearLayout.setLayoutParams(params);
        }else {
            menuItemLinearLayout.setScaleX(0.7f);
            menuItemLinearLayout.setScaleY(0.7f);
        }
            menuItemBelowText.setText(belowText[position1]);
            menuItemImage.setImageResource(mThumbIds[position1]);
            menuItemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    putpos(belowText[position1]);
                }
            });
       /* for(int i=0;i<j;i++)
        {
            Log.d("ppll0",belowText[i]+" "); // ppll (initial)
        }
        position=holder.getAdapterPosition();
        Log.d("Possss0",position+"");
        k=position;
        if(k>0) {
            j=j-position*3;
            Log.d("kvalueold0",k+" ");
            k = k + 3*position - position;
            Log.d("kvalue0",k+" ");

        }if(j>0) {
                menuItemBelowText.setText(belowText[k]);
                menuItemImage.setImageResource(mThumbIds[k]);
                j = j - 1;
            }

            else {
                menuItemBelowText.setText(" ");
                menuItemImage.setImageResource(R.drawable.background_circle);
        //        menuItemImage.setAlpha(0.5f);
                menuItemImage.setEnabled(false);
            }
            if(j>0) {
               // tv2.setText(belowText[k + 1]);
                //img2.setImageResource(mThumbIds[k + 1]);
                j=j-1;
            }
            else {
                //tv2.setText(" ");
               // img2.setImageResource(R.drawable.background_circle);
          //      img2.setAlpha(0.5f);
                //img2.setEnabled(false);
            }

            if(j>0) {
              //  tv3.setText(belowText[k + 2]);
                //img3.setImageResource(mThumbIds[k + 2]);
                j = j - 1;
            }
            else {
               // tv3.setText(" ");
               // img3.setImageResource(R.drawable.background_circle);
             //   img3.setAlpha(0.5f);
              //  img3.setEnabled(false);
            }*/
       // }

 /*       if (session.getGridSize()==1){
        for(int i=0;i<j;i++)
        {
            Log.d("ppll",belowText[i]+" ");
        }
        position=holder.getAdapterPosition();
        Log.d("Possss",position+"");
        k=position;
        if(k>0) {
            j=j-position*9;
            Log.d("kvalueold",k+" ");
            k = k + 9*position - position;
            Log.d("kvalue",k+" ");
        }
            if(j>0) {
                menuItemBelowText.setText(belowText[k]);
                menuItemImage.setImageResource(mThumbIds[k]);
                Log.d("belowtext",belowText[k]+" ");
                j = j - 1;
            }

            else {
                menuItemBelowText.setText(" ");
                menuItemImage.setImageResource(R.drawable.background_circle);
               // menuItemImage.setAlpha(0.5f);
                menuItemImage.setEnabled(false);
            }
            if(j>0) {
               // tv2.setText(belowText[k + 1]);
               // img2.setImageResource(mThumbIds[k + 1]);
                j=j-1;
            }
            else {
              //  tv2.setText(" ");
                //img2.setImageResource(R.drawable.background_circle);
            //    img2.setAlpha(0.5f);
              //  img2.setEnabled(false);
            }

            if(j>0) {
              //  tv3.setText(belowText[k + 2]);
              //  img3.setImageResource(mThumbIds[k + 2]);
                j = j - 1;
            }
            else {
                //tv3.setText(" ");
               // img3.setImageResource(R.drawable.background_circle);
            //    img3.setAlpha(0.5f);
               // img3.setEnabled(false);
            }

            if(j>0) {
               // tv4.setText(belowText[k + 3]);
                //img4.setImageResource(mThumbIds[k + 3]);
                j = j - 1;
            }

            else {
                //tv4.setText(" ");
                //img4.setImageResource(R.drawable.background_circle);
         //       img4.setAlpha(0.5f);
               // img4.setEnabled(false);
            }
            if(j>0) {
               // tv5.setText(belowText[k + 4]);
               // img5.setImageResource(mThumbIds[k + 4]);
                j=j-1;
            }
            else {
                //tv5.setText(" ");
               // img5.setImageResource(R.drawable.background_circle);
         //       img5.setAlpha(0.5f);
               // img5.setEnabled(false);
            }

            if(j>0) {
                //tv6.setText(belowText[k + 5]);
               // img6.setImageResource(mThumbIds[k + 5]);
                j = j - 1;
            }
            else {
               // tv6.setText(" ");
                //img6.setImageResource(R.drawable.background_circle);
           //     img6.setAlpha(0.5f);
               // img6.setEnabled(false);
            }
            if(j>0) {
               // tv7.setText(belowText[k + 6]);
               // img7.setImageResource(mThumbIds[k + 6]);
                j = j - 1;
            }

            else {
                //tv7.setText(" ");
               // img7.setImageResource(R.drawable.background_circle);
             //   img7.setAlpha(0.5f);
                //img7.setEnabled(false);
            }
            if(j>0) {
               // tv8.setText(belowText[k + 7]);
                //img8.setImageResource(mThumbIds[k + 7]);
                j=j-1;
            }
            else {
                //tv8.setText(" ");
               ///img8.setImageResource(R.drawable.background_circle);
           //     img8.setAlpha(0.5f);
               // img8.setEnabled(false);
            }

            if(j>0) {
              //  tv9.setText(belowText[k + 8]);
                //img9.setImageResource(mThumbIds[k + 8]);
                j = j - 1;
            }
            else {
               // tv9.setText(" ");
                //img9.setImageResource(R.drawable.background_circle);
         //       img9.setAlpha(0.5f);
                //img9.setEnabled(false);
            }
        }*/
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        System.out.println("MTHUMBSID"+(mThumbIds.length));

        /*if (session.getGridSize()==0) {
            kk = (mThumbIds.length % 3);
            System.out.println("kkkkkkkk" + kk);
            l = (mThumbIds.length / 3);
            System.out.println("llllll" + l);
            if (kk > 0) {
                l++;
                System.out.println("COOUT" + l);
            }
            System.out.println("DOOUT" + l);

        }

        if (session.getGridSize()==1) {
            kk = (mThumbIds.length % 9);
            System.out.println("kkkkkkkk" + kk);
            l = (mThumbIds.length / 9);
            System.out.println("llllll" + l);
            if (kk > 0) {
                l++;
                System.out.println("COOUT" + l);
            }
            System.out.println("DOOUT" + l);
        }*/
        return mThumbIds.length;
    }

private void handleItemClick(String position1, int local) {
}

    private String putpos(String s) {
        String position="";
        position+=s;
        System.out.println("ppppp "+position);
        return position;
    }
}