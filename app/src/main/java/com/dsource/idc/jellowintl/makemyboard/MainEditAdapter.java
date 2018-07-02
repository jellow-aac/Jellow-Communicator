package com.dsource.idc.jellowintl.makemyboard;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dsource.idc.jellowintl.GlideApp;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.utility.JellowIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.File;
import java.util.ArrayList;

/**
 * This class will be the backbone of the Make my board feature of jellow
 */
public class MainEditAdapter extends RecyclerView.Adapter<MainEditAdapter.ViewHolder>{

    private Context mContext;
    public ArrayList<JellowIcon> mainList;
    public ArrayList<LeftIconPane> sortedList;
    public int Level=0;
    private OnItemClickListener mItemClickListener;
    private ArrayList<JellowIcon> populatedList;
    private int levelOneParent=-1;
    private int levelTwoParent=-1;
    private OnRemoveClickListener mRemoveClickListener;

    /**
    * public constructor
     * @param context
    *   @param
    */
    public MainEditAdapter(Context context, ArrayList<JellowIcon> mainList, ArrayList<LeftIconPane> sortedList) {
        mContext = context;
        this.mainList=mainList;
        this.sortedList=sortedList;
        this.Level=0;
        populatedList=getPopulationList(Level,-1,-1);

        }


    /**
     * This functions takes the level, parent 1 and parent to and organizes the data accordingly
     *
     * @param level
     * @param parent0
     * @param parent1
     * @return
     */
    private ArrayList<JellowIcon> getPopulationList(int level, int parent0,int parent1){


        //To get which parent we need to populate
        int pos=-1;
        for(int i=0;i<sortedList.size();i++)
            if(sortedList.get(i).pos==parent0)
                pos=i;
        Log.d("GetPopulationIcon","Vars are: level: "+level+" Parent0: "+parent0+" Parent 1: "+parent1+" pos:"+pos);

        ArrayList<JellowIcon> list=new ArrayList<>();

        //For home i.e Level 0
        if(level==0)
        {
             for(int i=0;i<sortedList.size();i++)
                 {
                     list.add(new JellowIcon(sortedList.get(i).category,"",sortedList.get(i).pos,-1,-1));
                 }
        }

        //For Level two icon, we need the level one icon that is pressed
        else if(level==1)
        {
            if(pos!=-1)
                list=sortedList.get(pos).subList;
            else Toast.makeText(mContext,"No Subchild",Toast.LENGTH_SHORT).show();
        }
        //For level three we need to have
        else if(level==2)
        {


            if(pos!=-1) {
                ArrayList<JellowIcon> tempList = sortedList.get(pos).subList;
                for (int i = 0; i < tempList.size(); i++) {
                    JellowIcon icon = tempList.get(i);
                    if (icon.parent1 == parent1 && icon.parent2 != -1)
                        list.add(icon);
                }
            }
            else Toast.makeText(mContext, "No sub child", Toast.LENGTH_SHORT).show();
        }
    return list;
    }

    private boolean onBackPressedAgain=false;
    public int onBackPressed()
    {
            if(Level==2)
            {
                --Level;
                levelTwoParent=-1;
                populatedList=UtilFunctions.getLevelTwoIcons(getPopulationList(Level,levelOneParent,-1),levelOneParent,mContext);
                notifyDataSetChanged();
            }
            else if(Level==1)
            {
                --Level;
                levelTwoParent = -1;
                levelOneParent=-1;
                populatedList=getPopulationList(Level,-1,-1);
                notifyDataSetChanged();
            }
            else if(Level==0)
            {
                if(!onBackPressedAgain)
                Toast.makeText(mContext,"Press again to exit edit board, all data would be lost",Toast.LENGTH_LONG).show();
                onBackPressedAgain=true;
                return -1;
            }

        return Level;
    }

    public void onHomePressed() {
        Level=0;
        levelTwoParent=-1;
        levelOneParent=-1;
        populatedList=getPopulationList(Level,-1,-1);
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //Each data item is just a string in this case
    public TextView iconTitle;
    public ImageView iconImage;
    public ImageView removeIcon;
    private View holder;
    public ViewHolder(View v) {
        super(v);
        iconTitle =v.findViewById(R.id.icon_title);
        iconImage=v.findViewById(R.id.icon_image_view);
        removeIcon=v.findViewById(R.id.icon_remove_button);
        removeIcon.setOnClickListener(this);
        v.setOnClickListener(this);
        v.setOnTouchListener(getDoubleTapListener());
        holder=v;

    }

    private void showLevelTwo() {

        ArrayList<JellowIcon> tempList;
        tempList = UtilFunctions.getLevelTwoIcons(getPopulationList((Level+1),sortedList.get(getAdapterPosition()).pos, -1),sortedList.get(getAdapterPosition()).pos,mContext);
        if(tempList.size()>0) {
            Level++;
            levelTwoParent = -1;
            levelOneParent = sortedList.get(getAdapterPosition()).pos;
            populatedList = UtilFunctions.getLevelTwoIcons(getPopulationList(Level, levelOneParent, -1), levelOneParent, mContext);
            notifyDataSetChanged();
        }
        else Toast.makeText(mContext,"No sub category",Toast.LENGTH_SHORT).show();
        }

    public View.OnTouchListener getDoubleTapListener()
    {
            final View.OnTouchListener list= new View.OnTouchListener() {
                private GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        if(Level==0)
                        showLevelTwo();
                        else if(Level==1)
                        {

                                ArrayList<JellowIcon> icons=getPopulationList((Level+1),levelOneParent,populatedList.get(getAdapterPosition()).parent1);
                                if(icons.size()>0) {
                                    Level++;
                                    levelTwoParent = populatedList.get(getAdapterPosition()).parent1;
                                    populatedList = getPopulationList(Level, levelOneParent, levelTwoParent);
                                    notifyDataSetChanged();
                                }
                                else
                                    Toast.makeText(mContext,"No sub category",Toast.LENGTH_SHORT).show();

                        }

                        return super.onDoubleTap(e);
                    }

                });

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return true;
                }
            };
            return list;
    }

    @Override
    public void onClick(View view) {
        if(view==removeIcon)
        {
           // ExplosionField explosionField=ExplosionField.attach2Window((EditBoard)mContext);
            //explosionField.explode(holder);
            if(Level==0)
            {

            }
            else if(Level==1)//When deleting a level two icon
            {
                JellowIcon icon=populatedList.get(getAdapterPosition());
                int parent=icon.parent1;
                //Removing all icon of that category from this list
                ArrayList<Integer> iconPositionToRemove=new ArrayList<>();
                for(int i=0;i<sortedList.get(levelOneParent).subList.size();i++) {
                    JellowIcon thisIcon=sortedList.get(levelOneParent).subList.get(i);
                    Log.d("IconRemove","List size is: "+sortedList.get(levelOneParent).subList.size()+" Icon is: p0 "+thisIcon.parent0+" p1: "+thisIcon.parent1+" p2 "+thisIcon.parent2+" Name:"+thisIcon.IconTitle);
                    if (sortedList.get(levelOneParent).subList.get(i).parent1 == parent) {
                        Log.d("IconRemove","Icon removed: "+sortedList.get(levelOneParent).subList.get(i).IconTitle);
                        iconPositionToRemove.add(i);
                    }


                }
                for(int i=0;i<iconPositionToRemove.size();i++)
                    sortedList.get(levelOneParent).subList.remove(iconPositionToRemove.get(i));

                //Remove all icon of the parent from the mail list also
                for(int i=0;i<mainList.size();i++)
                    if(mainList.get(i).parent1==parent)
                        mainList.remove(i);

                notifyDataSetChanged();


            }
            else if(Level==2) {
                JellowIcon iconToRemove = populatedList.get(getAdapterPosition());

                for (int i = 0; i < sortedList.get(levelOneParent).subList.size(); i++)
                    if (sortedList.get(levelOneParent).subList.get(i).isEqual(iconToRemove))
                        sortedList.get(levelOneParent).subList.remove(i);
                for (int i = 0; i < mainList.size(); i++)
                    if (mainList.get(i).isEqual(iconToRemove))
                        mainList.remove(i);

                populatedList.remove(getAdapterPosition());
                notifyDataSetChanged();
            }


        }

    }



    //End of viewholder
    }


    public interface OnItemClickListener {
    void onItemClick(View view, int position);
}
    public interface OnRemoveClickListener {
        void onItemClick(View view,JellowIcon Icon);
    }
    public void setOnRemoveClickListner(final MainEditAdapter.OnRemoveClickListener mRemoveClickListener) {
        this.mRemoveClickListener = mRemoveClickListener;
    }


    public void setOnItemClickListner(final MainEditAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public MainEditAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.icon_edit_pain_card, parent, false);


        return new MainEditAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    JellowIcon icon=populatedList.get(position);
        holder.iconTitle.setText(icon.IconTitle);
        if(icon.parent1==-1)
            holder.removeIcon.setVisibility(View.GONE);
        else holder.removeIcon.setVisibility(View.VISIBLE);
        if(icon.parent1==-1)
        {
            TypedArray mArray=mContext.getResources().obtainTypedArray(R.array.arrLevelOneIconAdapter);
            holder.iconImage.setImageDrawable(mArray.getDrawable(icon.parent0));
        }
        else
        {
            SessionManager mSession = new SessionManager(mContext);
            File en_dir = mContext.getDir(mSession.getLanguage(), Context.MODE_PRIVATE);
            String path = en_dir.getAbsolutePath() + "/drawables";
            GlideApp.with(mContext)
                    .load(path+"/"+ icon.IconDrawable+".png")
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(false)
                    .centerCrop()
                    .dontAnimate()
                    .into(holder.iconImage);
        }

        //holder.iconImage.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.jiggle));

    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        holder.iconImage.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.jiggle));
    }

    @Override
    public int getItemCount() {return populatedList.size();}



}