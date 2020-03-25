package com.example.klubbasketdki.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.klubbasketdki.Model.KlubModel;
import com.example.klubbasketdki.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class KlubAdapter extends RecyclerView.Adapter<KlubAdapter.RecyclerViewHolder> {
    private Context mContext;
    private List<KlubModel> klubModels;
    private OnItemClickListener mListener;

    public KlubAdapter (Context context, List<KlubModel> uploads) {
        mContext = context ;
        klubModels = uploads;
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_klub, viewGroup, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        KlubModel currentKlubModel = klubModels.get(position);
        holder.KlubName_tv.setText(currentKlubModel.getName());
        holder.Lokasi_tv.setText(currentKlubModel.getLokasi());

        Log.v("cekvalue", currentKlubModel.getImageURL());
        Picasso.with(mContext)
                .load(currentKlubModel.getImageURL())
                .error(R.drawable.dw_pic_klubadapter)
                .fit()
                .centerCrop()
                .into(holder.KlubImageLogo);
    }

    @Override
    public int getItemCount() {
        return klubModels.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView KlubName_tv, Lokasi_tv;
        public ImageView KlubImageLogo;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            KlubName_tv =itemView.findViewById ( R.id.tv_rowklub_namaklub);
            Lokasi_tv = itemView.findViewById(R.id.tv_rowklub_lokasi);
//            dateTextView = itemView.findViewById(R.id.TextView);
            KlubImageLogo = itemView.findViewById(R.id.klubLogoView);

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
            MenuItem editItem = menu.add(Menu.NONE, 1, 1, "Edit");
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
