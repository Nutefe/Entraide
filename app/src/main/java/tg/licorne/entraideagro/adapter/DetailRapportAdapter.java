package tg.licorne.entraideagro.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import tg.licorne.entraideagro.R;
import tg.licorne.entraideagro.model.RapportDetail;

/**
 * Created by Admin on 08/05/2018.
 */

public class DetailRapportAdapter extends RecyclerView.Adapter<DetailRapportAdapter.DetailRapportViewHolder> {
    private List<RapportDetail> rapportDetails;
    private static Context context;

    public DetailRapportAdapter(List<RapportDetail> rapportDetails) {
        this.rapportDetails = rapportDetails;
    }

    @Override
    public DetailRapportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewImageRapport = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_image_rapport, parent, false);
        context = parent.getContext();
        return new DetailRapportViewHolder(viewImageRapport);
    }

    @Override
    public void onBindViewHolder(DetailRapportViewHolder holder, int position) {
        final  RapportDetail rapportDetail = rapportDetails.get(position);
        holder.textViewNom.setText(rapportDetail.getNom());
        Glide.with(context)
                .load(rapportDetail.getPhoto())
                .asBitmap()
                .into(holder.imageViewPhoto);
    }

    @Override
    public int getItemCount() {
        return rapportDetails.size();
    }

    public class DetailRapportViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewPhoto, imageViewDownload;
        private TextView textViewNom;
        public DetailRapportViewHolder(View itemView) {
            super(itemView);
            textViewNom = itemView.findViewById(R.id.idImageViewRapportNomFile);
            imageViewDownload = itemView.findViewById(R.id.idImageViewRapportDownload);
            imageViewPhoto = itemView.findViewById(R.id.idTextViewRapportFile);
        }
    }
}
