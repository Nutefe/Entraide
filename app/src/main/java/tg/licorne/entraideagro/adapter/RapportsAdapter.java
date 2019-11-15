package tg.licorne.entraideagro.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import tg.licorne.entraideagro.R;
import tg.licorne.entraideagro.controllers.DetailRapportActivity;
import tg.licorne.entraideagro.model.Rapports;

/**
 * Created by Admin on 17/02/2018.
 */

public class RapportsAdapter extends RecyclerView.Adapter<RapportsAdapter.RapportViewHolder> {

    String string;
    public List<Rapports> rapportsList;

    public RapportsAdapter(List<Rapports> rapportsList) {
        this.rapportsList = rapportsList;
    }

    @Override
    public RapportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewRapport = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_rapport, parent, false);
        return new RapportViewHolder(viewRapport);
    }

    @Override
    public void onBindViewHolder(RapportViewHolder holder, int position) {
        Rapports rapports = rapportsList.get(position);
        //holder.textViewActivite.setText(rapports.getSujet());
        holder.textViewSujet.setText(rapports.getSujet());
        holder.textViewDate.setText(rapports.getDate());

    }

    @Override
    public int getItemCount() {
        return rapportsList.size();
    }

    public class RapportViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewActivite, textViewSujet, textViewDate;
        public RapportViewHolder(View itemView) {
            super(itemView);
            //textViewActivite = itemView.findViewById(R.id.idTextViewActiviteRapport);
            textViewSujet = itemView.findViewById(R.id.idTextViewSujetRapport);
            textViewDate = itemView.findViewById(R.id.idTextViewDaateRapport);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Rapports rapports = rapportsList.get(pos);
                        Intent intent = new Intent(view.getContext(), DetailRapportActivity.class);
                        intent.putExtra("TOKEN", rapports.getToken());
                        intent.putExtra("ID_RAPPORT", rapports.getId());
                        view.getContext().startActivity(intent);
                    }
                }
            });
        }
    }
}
