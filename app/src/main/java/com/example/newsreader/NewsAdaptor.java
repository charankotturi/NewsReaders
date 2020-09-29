package com.example.newsreader;

import android.content.Context;
import android.content.Intent;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewsAdaptor extends RecyclerView.Adapter<NewsAdaptor.ViewHolder>{

    private ArrayList<NewsItems> news = new ArrayList<>();
    private Context mContext;

    public NewsAdaptor(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card_view, parent ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txtDate.setText(news.get(position).getDate());
        holder.txtTitle.setText(news.get(position).getTitle());
        holder.txtDec.setText(news.get(position).getDescription());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,WebSiteActivity.class);
                intent.putExtra("url",news.get(position).getLink());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public void setNews(ArrayList<NewsItems> news) {
        this.news = news;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CardView parent;
        private TextView txtTitle;
        private TextView txtDec;
        private TextView txtDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDec = itemView.findViewById(R.id.txtDec);
            txtDate = itemView.findViewById(R.id.txtdate);

        }
    }
}
