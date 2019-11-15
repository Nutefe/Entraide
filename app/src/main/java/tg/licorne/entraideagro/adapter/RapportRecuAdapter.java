package tg.licorne.entraideagro.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tg.licorne.entraideagro.R;
import tg.licorne.entraideagro.model.Rapports;

/**
 * Created by Admin on 08/05/2018.
 */

public class RapportRecuAdapter extends RecyclerView.Adapter<RapportRecuAdapter.RapportRecuViewHolder> {
    String string;
    public List<Rapports> rapportsList;

    public RapportRecuAdapter(List<Rapports> rapportsList) {
        this.rapportsList = rapportsList;
    }

    @Override
    public RapportRecuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewRapport = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_rapport_recu, parent, false);
        return new RapportRecuViewHolder(viewRapport);
    }

    @Override
    public void onBindViewHolder(RapportRecuViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return rapportsList.size();
    }

    public class RapportRecuViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewRond,textViewNom, textViewSujet, textViewDate;
        public RapportRecuViewHolder(View itemView) {
            super(itemView);
            textViewRond= itemView.findViewById(R.id.idTextViewRondRecu);
            textViewNom = itemView.findViewById(R.id.idTextViewNomEnvoieRapport);
            textViewSujet = itemView.findViewById(R.id.idTextViewSujetRapportRecu);
            textViewDate = itemView.findViewById(R.id.idTextViewDaateRapportRecu);
        }
    }
}
