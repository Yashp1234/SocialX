package com.example.newsfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder>{

    private Context context;
    private List<News> newsList;

    public NewsAdapter(Context context, List<News> newsList) {
        this.newsList = newsList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news,parent,false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
        News news = newsList.get(position);
        holder.title.setText(news.getTitle());
        holder.description.setText(news.getDescription());
        holder.publishedAt.setText(news.getPublishedAt());
        holder.source.setText(news.getSource());

        Glide.with(context)
                .load(news.getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.image_not_available)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void updateList(List<News> list){
        newsList = list;
        notifyDataSetChanged();
    }
    public class NewsHolder extends RecyclerView.ViewHolder{
        TextView title,description,publishedAt,source;
        ImageView imageView;

        public NewsHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.idTitle);
            description = itemView.findViewById(R.id.idDescription);
            publishedAt = itemView.findViewById(R.id.idPublishedAt);
            source = itemView.findViewById(R.id.idSource);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
