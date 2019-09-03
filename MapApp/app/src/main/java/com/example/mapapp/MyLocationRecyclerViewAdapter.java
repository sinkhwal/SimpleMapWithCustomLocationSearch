package com.example.mapapp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link RecyclerView.Adapter} that can display a  and makes a call to the
 * specified {@link }.
 * Created BY Suman Sinkhwal
 * TODO: Replace the implementation with code for your data type.
 */
public class MyLocationRecyclerViewAdapter extends RecyclerView.Adapter<MyLocationRecyclerViewAdapter.ViewHolder> {

    private final List<LocationEntry> mValues;
    private final LocationFragment.OnListFragmentInteractionListener mListener;

    public MyLocationRecyclerViewAdapter(List<LocationEntry> items, LocationFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mLocation = mValues.get(position);
        holder.mId.setText(mValues.get(position).id.toString());
        holder.mTitle.setText(mValues.get(position).title);
        holder.mLatitudeLogitue.setText("("+mValues.get(position).latitude+", "+mValues.get(position).longitude+")");
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mLocation);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mId;
        public final TextView mTitle;
        public final TextView mLatitudeLogitue;
        public LocationEntry mLocation;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mId= (TextView) view.findViewById(R.id.locationId);
            mTitle = (TextView) view.findViewById(R.id.title);
            mLatitudeLogitue= (TextView) view.findViewById(R.id.latitudeLangitude);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitle.getText() + "'";
        }
    }
}
