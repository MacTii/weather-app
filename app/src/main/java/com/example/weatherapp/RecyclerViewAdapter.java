package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class RecyclerViewAdapterFinder extends RecyclerView.Adapter<RecyclerViewAdapterFinder.ViewHolderFinder>{

    private final ArrayList<String> cityList;

    private final ClickListenerFinder clickListenerFinder;

    public RecyclerViewAdapterFinder(ArrayList<String> cityList, ClickListenerFinder clickListenerFinder) {
        this.cityList = cityList;
        this.clickListenerFinder = clickListenerFinder;
    }

    @NonNull
    @Override
    public ViewHolderFinder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == R.layout.fragment_city_row) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_city_row, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_favorite, parent, false);
        }
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int height = view.getMeasuredHeight();
        return new ViewHolderFinder(view,clickListenerFinder,height);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderFinder holder, int position) {
        if(position==0){
            holder.addToFavorite.setImageResource(R.drawable.ic_baseline_favorite_24);
        }else{
            holder.cityName.setText(cityList.get(position-1));
            // holder.alreadyAdded.setImageResource(R.drawable.ic_baseline_favorite_24);
            holder.deleteFromFavorite.setImageResource(R.drawable.ic_baseline_delete_24);
        }
    }

    @Override
    public int getItemViewType(final int position) {
        return (position == 0) ? R.layout.fragment_favorite : R.layout.fragment_city_row;
    }

    @Override
    public int getItemCount() {
        return cityList.size()+1;
    }

    public static class ViewHolderFinder extends RecyclerView.ViewHolder {

        EditText findCity;
        ImageButton addToFavorite;
        Button apply;
        TextView cityName;
        ImageButton deleteFromFavorite;
        ImageButton alreadyAdded;

        public ViewHolderFinder(@NonNull View itemView, ClickListenerFinder clickListenerFinder, int height) {
            super(itemView);

            itemView.setMinimumHeight(height);
            findCity = itemView.findViewById(R.id.CityFinder);
            addToFavorite = itemView.findViewById(R.id.addToFavorite);
            // apply = itemView.findViewById(R.id.apply);
            cityName = itemView.findViewById(R.id.City);
            deleteFromFavorite = itemView.findViewById(R.id.delete);
            //alreadyAdded = itemView.findViewById(R.id.favoriteIcon);

            itemView.setOnClickListener(v -> clickListenerFinder.onClickAlreadyAdded(getBindingAdapterPosition()-1));

            if(deleteFromFavorite!=null) {
                deleteFromFavorite.setOnClickListener(v -> clickListenerFinder.onClickTrash(getBindingAdapterPosition()-1));
            }
//            if(alreadyAdded!=null){
//                alreadyAdded.setOnClickListener(v -> System.out.println("AAAAAA"));
//            }
            if(addToFavorite!=null){
                addToFavorite.setOnClickListener(v -> clickListenerFinder.onClickAddToFavorite(getBindingAdapterPosition(),findCity.getText().toString()));
            }
//            if(apply!=null){
//                apply.setOnClickListener(v -> clickListenerFinder.onClickApply(0,findCity.getText().toString()));
//            }

        }
    }
}
