package com.example.klubbasketdki.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.klubbasketdki.Model.PelatihModel;
import com.example.klubbasketdki.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PelatihAdapter extends RecyclerView.Adapter<PelatihAdapter.RecyclerViewHolder>{
    private Context mContext;
    private List<PelatihModel> PelatihModel;
    private OnItemClickListener mListener;

    public PelatihAdapter(Context context, List<com.example.klubbasketdki.Model.PelatihModel> uploads) {
        mContext = context;
        PelatihModel = uploads;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_pelatih, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        PelatihModel currentPelatihModel = PelatihModel.get(position);
        holder.nameTextView.setText(currentPelatihModel.getNama());
        holder.ketTextView.setText(currentPelatihModel.getKeterangan());
        //   holder.dateTextView.setText(getDateToday());

        Log.v("cekvalue", currentPelatihModel.getImageURL());
        Picasso.with(mContext)
                .load(currentPelatihModel.getImageURL())
                .error(R.drawable.dw_pic_klubadapter)
                .fit()
                .centerCrop()
                .into(holder.pelImageView);
    }

    @Override
    public int getItemCount() {
        return PelatihModel.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView nameTextView,ketTextView;
        public ImageView pelImageView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTextView =itemView.findViewById (R.id.namePelTextView );
            ketTextView = itemView.findViewById(R.id.ketPelTextView);
//            dateTextView = itemView.findViewById(R.id.TextView);
            pelImageView = itemView.findViewById(R.id.pelatihImageView);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem editItem = menu.add( Menu.NONE, 1, 1, "Edit");
            MenuItem deleteItem = menu.add(Menu.NONE, 2, 2, "Delete");

            editItem.setOnMenuItemClickListener(this);
            deleteItem.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onShowItemClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteItemClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onShowItemClick(int position);
        void onDeleteItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
