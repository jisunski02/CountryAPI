package com.equicom.countryapi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.equicom.countryapi.Constants;
import com.equicom.countryapi.R;
import com.equicom.countryapi.data.CountryDataModel;
import com.equicom.countryapi.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> implements Filterable {

    Context context;
    private List<CountryDataModel> countryDataModelList;
    private OnItemClickListener onItemClickListener;
    int selected_position = -1;
    private List<CountryDataModel> searchedCountryList;


    public CountryAdapter(Context context, List<CountryDataModel> countryDataModelList){
        this.context = context;
        this.countryDataModelList = countryDataModelList;
        this.searchedCountryList = new ArrayList<>(countryDataModelList);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_country,parent,false);
        ViewHolder viewHolder = new ViewHolder(v, onItemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CountryDataModel countryDataModel = countryDataModelList.get(position);

        String region = countryDataModel.getRegion();
        String country = countryDataModel.getNameDataModel().getCommon();

        holder.tvCountry.setText(country);
    }

    @Override
    public int getItemCount() {
        return Math.min(10, countryDataModelList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCountry;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            tvCountry = itemView.findViewById(R.id.tvCountry);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onItemClick(position);
                            notifyItemChanged(selected_position);
                            selected_position = getAdapterPosition();
                            notifyItemChanged(selected_position);

                        }

                    }
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return searchCountry;
    }

    private final Filter searchCountry = new Filter(){
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CountryDataModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(searchedCountryList);
            }

            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (CountryDataModel item : searchedCountryList) {

                    if (item.getRegion().equals(Constants.selectedItem) && item.getNameDataModel().getCommon().contains(filterPattern)) {
                        filteredList.add(item);
                    }

                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            countryDataModelList.clear();
            countryDataModelList.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };
}
