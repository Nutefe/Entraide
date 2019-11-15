package tg.licorne.entraideagro.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tg.licorne.entraideagro.R;
import tg.licorne.entraideagro.model.Agents;

/**
 * Created by Admin on 09/05/2018.
 */

public class FermeAgentAdapter extends RecyclerView.Adapter<FermeAgentAdapter.FermeAgentViewHolder> {
    private List<Agents> agentsList;
    private static Context context;

    public FermeAgentAdapter(List<Agents> agentsList) {
        this.agentsList = agentsList;
    }

    @Override
    public FermeAgentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewAgent = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_detail_ferm, parent, false);
        context = parent.getContext();
        return new FermeAgentViewHolder(viewAgent);
    }

    @Override
    public void onBindViewHolder(FermeAgentViewHolder holder, int position) {
        final Agents agents = agentsList.get(position);
        holder.textViewNum.setText(agents.getNum());
        holder.textViewNom.setText(agents.getNom());
        holder.textViewType.setText(agents.getType());
    }

    @Override
    public int getItemCount() {
        return agentsList.size();
    }

    public class FermeAgentViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNum, textViewNom, textViewType;
        public FermeAgentViewHolder(View itemView) {
            super(itemView);
            textViewNum = itemView.findViewById(R.id.idTextViewRondFermeDetail);
            textViewNom = itemView.findViewById(R.id.idTextViewDetaiFermeNom);
            textViewType = itemView.findViewById(R.id.idTextViewDetaiFermeTypeAgent);
        }
    }
}
