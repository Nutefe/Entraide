package tg.licorne.entraideagro.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import tg.licorne.entraideagro.R;
import tg.licorne.entraideagro.controllers.DetailFermeActivity;
import tg.licorne.entraideagro.helper.CircleTransform;
import tg.licorne.entraideagro.model.Fermes;

/**
 * Created by Admin on 18/02/2018.
 */

public class FermesAdapter extends RecyclerView.Adapter<FermesAdapter.FermeViewHolder> {

    private List<Fermes> fermeList;
    private static Context context;

    public FermesAdapter(List<Fermes> fermeList) {
        this.fermeList = fermeList;
    }

    @Override
    public FermesAdapter.FermeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewFerme = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_ferme, parent, false);
        context = parent.getContext();
        return new FermeViewHolder(viewFerme);
    }

    @Override
    public void onBindViewHolder(FermesAdapter.FermeViewHolder holder, int position) {
        Fermes fermes = fermeList.get(position);
        holder.textViewNom.setText(fermes.getNom());
        holder.textViewDimension.setText(String.valueOf(fermes.getDimenssion()));
        holder.textViewEquipe.setText(fermes.getEquipe());
        holder.textViewZone.setText(fermes.getZone());
        Glide.with(context)
                .load(fermes.getPhoto())
                .asBitmap()
                .into(holder.imageViewPhoto);
    }

    @Override
    public int getItemCount() {
        return fermeList.size();
    }

    public class FermeViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNom, textViewDimension, textViewEquipe, textViewZone;
        private ImageView imageViewDetail, imageViewPhoto;
        public FermeViewHolder(View itemView) {
            super(itemView);
            textViewNom = itemView.findViewById(R.id.idTextViewNomFerme);
            textViewDimension = itemView.findViewById(R.id.idTextViewDimensionFerme);
            textViewEquipe = itemView.findViewById(R.id.idTextViewEquipeFerme);
            textViewZone = itemView.findViewById(R.id.idTextViewZoneFerme);
            imageViewDetail = itemView.findViewById(R.id.idImageViewDetailFerme);
            imageViewPhoto = itemView.findViewById(R.id.idImageViewFermePhoto);

            imageViewDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Fermes fermes = fermeList.get(pos);
                        Intent intent = new Intent(view.getContext(), DetailFermeActivity.class);
                        intent.putExtra("ID", fermes.getId());
                        intent.putExtra("TOKEN", fermes.getToken());
                        view.getContext().startActivity(intent);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Fermes fermes = fermeList.get(pos);
                        Toast.makeText(view.getContext(), fermes.getNom().toString(), Toast.LENGTH_SHORT).show();
                        //view.getContext().startActivity(new Intent(view.getContext(), DetailFermeActivity.class));
                        Intent intent = new Intent(view.getContext(), DetailFermeActivity.class);
                        intent.putExtra("ID", fermes.getId());
                        intent.putExtra("TOKEN", fermes.getToken());
                        view.getContext().startActivity(intent);
                    }
                }
            });
        }
    }
}
