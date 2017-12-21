package br.com.documentsolutions.projetods;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Maia on 20/12/2017.
 */

public class MyAdpter extends RecyclerView.Adapter<MyAdpter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public MyAdpter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListItem listItem = listItems.get(position);

        holder.txtID.setText(listItem.getID());
        holder.txtNome.setText(listItem.getNome());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtID;
        public TextView txtNome;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            txtID = (TextView) itemView.findViewById(R.id.txtID);
            txtNome = (TextView) itemView.findViewById(R.id.txtNome);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
