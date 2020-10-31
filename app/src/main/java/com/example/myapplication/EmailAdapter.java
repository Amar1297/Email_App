package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.ViewHolder> implements Filterable {

    private List<EmailData> modelActivityList ;
    private List<EmailData> searchMails ;
    private Context context;

    public EmailAdapter(List<EmailData> modelActivityList, Context context) {
        this.modelActivityList = modelActivityList;
        this.context = context;
        searchMails=new ArrayList<>(modelActivityList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_mails,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final EmailData modelActivity = modelActivityList.get(position);

        //This will randomly color all circle
        Random mRandom = new Random();
        final int color = Color.argb(255, mRandom.nextInt(260), mRandom.nextInt(560), mRandom.nextInt(260));
        ((GradientDrawable) holder.textCircle.getBackground()).setColor(color);
         final char s=modelActivity.getFrom().toUpperCase().charAt(0);
        holder.textCircle.setText(String.valueOf(s));
        holder.textHead.setText(modelActivity.getFrom());
        holder.textSub.setText(modelActivity.getSubject());
        holder.textDes.setText(modelActivity.getMessage());
        holder.textDate.setText(String.valueOf(modelActivity.getId()));
        holder.cardView.setBackgroundColor(modelActivity.isSelected()?Color.parseColor("#90EE90"):Color.WHITE);
        holder.textCircle.setText(modelActivity.isSelected()?"✓":String.valueOf(s));
        ((GradientDrawable) holder.textCircle.getBackground()).setColor(modelActivity.isSelected()?Color.parseColor("#90EE90"):color);

        holder.textCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelActivity.setIsSelected(!modelActivity.isSelected());
                ((GradientDrawable) holder.textCircle.getBackground()).setColor(modelActivity.isSelected()?Color.GREEN:color);
                holder.textCircle.setText(modelActivity.isSelected()?"✓":String.valueOf(s));
                holder.view.setBackgroundColor(modelActivity.isSelected()?Color.parseColor("#90EE90"):Color.WHITE);
                holder.cardView.setBackgroundColor(modelActivity.isSelected()?Color.parseColor("#90EE90"):Color.WHITE);
             }
        });


    }

    @Override
    public int getItemCount() {
        return modelActivityList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textCircle,textHead,textSub,textDes,textDate;
        private View view;
        private CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            view=itemView;

            cardView=itemView.findViewById(R.id.cardviewmails);
            textCircle = (TextView)itemView.findViewById(R.id.circleText);
            textHead = (TextView)itemView.findViewById(R.id.headText);
            textSub = (TextView)itemView.findViewById(R.id.subText);
            textDes = (TextView)itemView.findViewById(R.id.desText);
            textDate = (TextView)itemView.findViewById(R.id.dateText);



        }
    }


    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<EmailData> filterlist=new ArrayList<>();

            if(constraint==null || constraint.length()==0){
                filterlist.addAll(searchMails);
            }else {
                String filterpattern=constraint.toString().toLowerCase().trim();

                for(EmailData item:searchMails)
                {
                    if(item.getSubject().toLowerCase().contains(filterpattern) || item.getFrom().toLowerCase().contains(filterpattern)
                     ||item.getMessage().toLowerCase().contains(filterpattern)){
                        filterlist.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filterlist;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            modelActivityList.clear();
            modelActivityList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

}
