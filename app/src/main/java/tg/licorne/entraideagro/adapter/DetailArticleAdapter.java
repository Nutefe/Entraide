package tg.licorne.entraideagro.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import tg.licorne.entraideagro.R;
import tg.licorne.entraideagro.controllers.CommentairesActivity;
import tg.licorne.entraideagro.controllers.DetailImageArticle;
import tg.licorne.entraideagro.helper.CircleTransform;
import tg.licorne.entraideagro.model.ArticleDetail;

/**
 * Created by Admin on 30/04/2018.
 */

public class DetailArticleAdapter extends RecyclerView.Adapter<DetailArticleAdapter.DetailArticleViewHolder> {
    private List<ArticleDetail> articleDetailList;
    private static Context context;

    public DetailArticleAdapter(List<ArticleDetail> articleDetailList) {
        this.articleDetailList = articleDetailList;
    }

    @Override
    public DetailArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewDetailArticle = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_image_article, parent, false);
        context = parent.getContext();
        return new DetailArticleViewHolder(viewDetailArticle);
    }

    @Override
    public void onBindViewHolder(DetailArticleViewHolder holder, int position) {
        final ArticleDetail articleDetail = articleDetailList.get(position);
        //holder.textViewNbrCommentaire.setText(articleDetail.getNbrCommentair()+" Commentaires");
        Glide.with(context)
                .load(articleDetail.getPhoto())
                .asBitmap()
                .into(holder.imageViewPhoto);
        /*holder.linearLayoutCommentaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CommentairesActivity.class);
                intent.putExtra("TOKEN", articleDetail.getToken());
                intent.putExtra("ID_USER", articleDetail.getId_user());
                intent.putExtra("ID_ARTICLE", articleDetail.getId());
                view.getContext().startActivity(intent);
            }
        });*/

        holder.imageViewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailImageArticle.class);
                intent.putExtra("PATH_IMAGE", articleDetail.getPhoto());
                ((Activity) context).startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleDetailList.size();
    }

    public class DetailArticleViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNbrCommentaire;
        ImageView imageViewPhoto;
        LinearLayout linearLayoutCommentaire;
        public DetailArticleViewHolder(View itemView) {
            super(itemView);
            //textViewNbrCommentaire = itemView.findViewById(R.id.idTextViewNbrCommentaireDetailArticle);
            imageViewPhoto = itemView.findViewById(R.id.idImageViewPhotoDetail);
            //linearLayoutCommentaire = itemView.findViewById(R.id.idLinearLayoutCommentaireDetailArticle);
        }
    }
}
