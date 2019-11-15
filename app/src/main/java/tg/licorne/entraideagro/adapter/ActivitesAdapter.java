package tg.licorne.entraideagro.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tg.licorne.entraideagro.R;
import tg.licorne.entraideagro.model.Activites;

/**
 * Created by Admin on 17/02/2018.
 */

public class ActivitesAdapter extends RecyclerView.Adapter<ActivitesAdapter.ActiviteVewHolder> {

    List<Activites> activitesList;

    public ActivitesAdapter(List<Activites> activitesList) {
        this.activitesList = activitesList;
    }

    @Override
    public ActiviteVewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewActivite= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_activite, parent, false);
        return new ActiviteVewHolder(viewActivite);
    }

    @Override
    public void onBindViewHolder(ActiviteVewHolder holder, int position) {
        Activites activites = activitesList.get(position);
        holder.textViewNom.setText(activites.getNomActivite());
        holder.textViewDescription.setText(activites.getDescription());
    }

    @Override
    public int getItemCount() {
        return activitesList.size();
    }

    public class ActiviteVewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNom, textViewDescription;
        public ActiviteVewHolder(View itemView) {
            super(itemView);
            textViewNom = itemView.findViewById(R.id.idTextViewNomActivite);
            textViewDescription = itemView.findViewById(R.id.idTextViewDescriptionActivite);
        }
    }
}
