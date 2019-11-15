package tg.licorne.entraideagro.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import tg.licorne.entraideagro.R;
import tg.licorne.entraideagro.controllers.CommentairesActivity;
import tg.licorne.entraideagro.controllers.DetailArticleActivity;
import tg.licorne.entraideagro.helper.CircleTransform;
import tg.licorne.entraideagro.helper.CircleTransforme;
import tg.licorne.entraideagro.model.Artilces;

/**
 * Created by Admin on 15/02/2018.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder> {

    private List<Artilces> artilcesList;
    private static Context context;

    public ArticlesAdapter(List<Artilces> artilcesList){
        this.artilcesList = artilcesList;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewArticle = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_article, parent, false);
        context = parent.getContext();
        return new ArticleViewHolder(viewArticle);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        final Artilces artilce = artilcesList.get(position);
        holder.textViewSujet.setText(artilce.getSujet());
        holder.textViewDate.setText(artilce.getDate());
        holder.textViewDateHeure.setText(artilce.getHeure());
        holder.textViewNomUser.setText(artilce.getNomUser());
        //holder.textViewNbrCommentaire.setText(String.valueOf(artilce.getNbrCommentaire())+" commantaire");
        Picasso.with(context)
                .load(artilce.getAvatar())
                .resize(90, 90)
                .centerCrop()
                .transform(new CircleTransforme())
                .into(holder.imageViewAvatar);
        Glide.with(context)
                .load(artilce.getPhoto1())
                .asBitmap()
                .into(holder.imageViewPhoto);


        holder.textViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),DetailArticleActivity.class);
                intent.putExtra("ID_ARTICLE", artilce.getId());
                intent.putExtra("ID_USER", artilce.getId_user());
                intent.putExtra("TOKEN", artilce.getToken());
                view.getContext().startActivity(intent);
            }
        });

        holder.imageViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //view.getContext().startActivity(new Intent(view.getContext(), DetailArticleActivity.class));
                Intent intent = new Intent(view.getContext(),DetailArticleActivity.class);
                intent.putExtra("ID_ARTICLE", artilce.getId());
                intent.putExtra("ID_USER", artilce.getId_user());
                intent.putExtra("TOKEN", artilce.getToken());
                view.getContext().startActivity(intent);
            }
        });

        /*holder.linearLayoutCommentaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CommentairesActivity.class);
                intent.putExtra("ID_ARTICLE", artilce.getId());
                intent.putExtra("ID_USER", artilce.getId_user());
                intent.putExtra("TOKEN", artilce.getToken());
                view.getContext().startActivity(intent);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return artilcesList.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewNomUser, textViewSujet, textViewDetail, textViewDate
                , textViewDateHeure, textViewNbrCommentaire;
        private ImageView imageViewDetail, imageViewAvatar, imageViewPhoto;
        private LinearLayout linearLayoutCommentaire;

        public ArticleViewHolder(final View itemView) {
            super(itemView);
            textViewNomUser = itemView.findViewById(R.id.idTextViewNomAdminArticle);
            textViewSujet = itemView.findViewById(R.id.idTextViewSujetArticle);
            textViewDetail = itemView.findViewById(R.id.idTextViewDetailArticle);
            //linearLayoutCommentaire = itemView.findViewById(R.id.idLinearLayoutCommentaire);
            imageViewDetail = itemView.findViewById(R.id.idImageViewDetailArticle);
            imageViewAvatar = itemView.findViewById(R.id.idImageViewAvatarUserConnect);
            textViewDate = itemView.findViewById(R.id.idTextViewDateArticle);
            textViewDateHeure = itemView.findViewById(R.id.idTextViewDateHeurArticle);
            //textViewNbrCommentaire = itemView.findViewById(R.id.idTextViewNbrCommentaireArticle);
            imageViewPhoto = itemView.findViewById(R.id.idImageViewPhoto);
        }
    }
}
