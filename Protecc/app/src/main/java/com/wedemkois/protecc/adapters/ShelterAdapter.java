package com.wedemkois.protecc.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.wedemkois.protecc.R;
import com.wedemkois.protecc.model.Shelter;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecyclerView adapter for a list of Shelters.
 */
public class ShelterAdapter extends FirestoreAdapter<ShelterAdapter.ViewHolder> {

    public interface OnShelterSelectedListener {

        void onShelterSelected(DocumentSnapshot Shelter);

    }

    private final OnShelterSelectedListener mListener;

    public ShelterAdapter(Query query, OnShelterSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.shelter_list_content, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.shelterName)
        TextView nameView;

        /* @BindView(R.id.Shelter_item_rating)
        MaterialRatingBar ratingBar;

        @BindView(R.id.Shelter_item_num_ratings)
        TextView numRatingsView;

        @BindView(R.id.Shelter_item_price)
        TextView priceView;

        @BindView(R.id.Shelter_item_category)
        TextView categoryView;

        @BindView(R.id.Shelter_item_city)
        TextView cityView; */

        ViewHolder(View itemView) {
            super(itemView);
            //noinspection ThisEscapedInObjectConstruction
            ButterKnife.bind(this, itemView);
        }

        void bind(final DocumentSnapshot snapshot,
                  final OnShelterSelectedListener listener) {

            Shelter shelter = snapshot.toObject(Shelter.class);

            Log.d("shelterAdapter", Objects.requireNonNull(shelter).toString());
            nameView.setText(shelter.getName());
//            ratingBar.setRating((float) Shelter.getAvgRating());
//            cityView.setText(Shelter.getCity());
//            categoryView.setText(Shelter.getCategory());
//            numRatingsView.setText(resources.getString(R.string.fmt_num_ratings,
//                    Shelter.getNumRatings()));
//            priceView.setText(ShelterUtil.getPriceString(Shelter));

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onShelterSelected(snapshot);
                    }
                }
            });
        }

    }
}
