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
import tg.licorne.entraideagro.model.Commentaires;

/**
 * Created by Admin on 18/02/2018.
 */

public class CommentairesAdapter extends RecyclerView.Adapter<CommentairesAdapter.CommentaireViewHolder> {
    private List<Commentaires> commentairesList;
    private static Context context;

    public CommentairesAdapter(List<Commentaires> commentairesList) {
        this.commentairesList = commentairesList;
    }

    @Override
    public CommentaireViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewCommentaire = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_commentaire, parent, false);
        context = parent.getContext();
        return new CommentaireViewHolder(viewCommentaire);
    }

    @Override
    public void onBindViewHolder(CommentaireViewHolder holder, int position) {
        Commentaires commentaires = commentairesList.get(position);
        holder.textViewContenu.setText(commentaires.getContenu());
        holder.textViewUser.setText(commentaires.getNom());
        holder.textViewDate.setText(commentaires.getDate());
        Glide.with(context)
                .load(commentaires.getAvatar())
                .asBitmap()
                .into(holder.imageViewAvatar);
    }

    @Override
    public int getItemCount() {
        return commentairesList.size();
    }

    public class CommentaireViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewContenu, textViewUser, textViewDate;
        private ImageView imageViewAvatar;
        public CommentaireViewHolder(View itemView) {
            super(itemView);
            textViewContenu = itemView.findViewById(R.id.idTextViewCommentaire);
            textViewUser = itemView.findViewById(R.id.idTextViewUserCommentaire);
            textViewDate = itemView.findViewById(R.id.idTextViewDateCommentaire);
            imageViewAvatar = itemView.findViewById(R.id.idImageViewAvatarCommentaire);
        }
    }
}
