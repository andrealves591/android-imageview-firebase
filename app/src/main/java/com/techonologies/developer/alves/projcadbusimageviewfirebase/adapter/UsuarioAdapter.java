package com.techonologies.developer.alves.projcadbusimageviewfirebase.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.techonologies.developer.alves.projcadbusimageviewfirebase.R;
import com.techonologies.developer.alves.projcadbusimageviewfirebase.model.Usuario;

import java.util.List;

/**
 * @author  ALVESDEVTEC - André Luiz Alves
 * @since 07/03/2019 - 12H10
 * @version 2.0 turboMax
 */

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.ViewHolder> {

    private Context mContext;
    private List<Usuario> mUploads;

    public UsuarioAdapter (Context context, List<Usuario> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @NonNull
    @Override
    public UsuarioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(
                R.layout.linha,
                parent,
                false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioAdapter.ViewHolder holder, int position) {
        Usuario uploadCurrent = mUploads.get(position);
        holder.tvNome.setText(uploadCurrent.getNome());
        Picasso.get()
                .load(uploadCurrent.getUrlImagem())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imgFoto);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNome;
        ImageView imgFoto;

        ViewHolder(View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.lb_tv_nome);
            imgFoto = itemView.findViewById(R.id.lb_iv_foto);
        }
    }
}
