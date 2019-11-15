package tg.licorne.entraideagro.adapter;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import tg.licorne.entraideagro.R;
import tg.licorne.entraideagro.config.ServerConfig;
import tg.licorne.entraideagro.model.Documents;

/**
 * Created by Admin on 01/04/2018.
 */

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {

    private List<Documents> documentsList;
    DownloadManager downloadManager;
    Context context;

    public DocumentAdapter(List<Documents> documentsList) {
        this.documentsList = documentsList;
    }

    @Override
    public DocumentAdapter.DocumentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewDocument = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_document, parent, false);
        context = viewDocument.getContext();
        return new DocumentViewHolder(viewDocument);
    }

    @Override
    public void onBindViewHolder(DocumentAdapter.DocumentViewHolder holder, int position) {
        final Documents documents = documentsList.get(position);
        holder.textViewTitre.setText(documents.getTitre());
        holder.textViewDescription.setText(documents.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), documents.getTitre().toString(), Toast.LENGTH_SHORT).show();

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Téléchargement");
                builder.setMessage("Voulez vous Telecharger ce fichier");
                builder.setPositiveButton(
                        "Oui",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String urlFile = ServerConfig.url_sever+"documents/download/"+
                                        documents.getTypeFile()+"/"+documents.getTitre();
                                downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
                                Uri uri = Uri.parse(urlFile);
                                DownloadManager.Request  request = new DownloadManager.Request(uri);
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                Long reference = downloadManager.enqueue(request);
                            }
                        }
                );

                builder.setNegativeButton(
                        "Annuler",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }
                );
                builder.create();
                builder.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return documentsList.size();
    }

    public class DocumentViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitre, textViewDescription;
        public DocumentViewHolder(View itemView) {
            super(itemView);
            textViewTitre = itemView.findViewById(R.id.idTextViewTitre);
            textViewDescription = itemView.findViewById(R.id.idTextViewDescriptionDoc);
        }
    }

    /*private void download(){
        //Toast.makeText(DetailRapportActivity.this, "type : "+typeFile+" nom fichier "+nomFile, Toast.LENGTH_SHORT).show();
        String urlFile = "http://";
        downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(urlFile);
        DownloadManager.Request  request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Long reference = downloadManager.enqueue(request);
    }*/

}
