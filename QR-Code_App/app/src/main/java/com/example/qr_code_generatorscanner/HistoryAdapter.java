package com.example.qr_code_generatorscanner;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Set;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private ArrayList<String> list;
    private int layout;
    private Context context;

    public HistoryAdapter(ArrayList list, int layout, Context context) {
        this.layout = layout;
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       LayoutInflater inflater =  LayoutInflater.from(parent.getContext());
       View view = inflater.inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.fillData(list.get(position));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            textView  = itemView.findViewById(R.id.txtHistory);
            imageView = itemView.findViewById(R.id.imgIcon);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Coppy text", textView.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, "Coppied", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
        public void fillData(String A){
            String B = A.substring(0, 3);
            String C = A.substring(3);
            switch (B){
                case "SCA":
                    imageView.setImageResource(R.drawable.sanner);
                    break;
                case "GEN":
                    imageView.setImageResource(R.drawable.generate);
                    break;
                case "LIB":
                    imageView.setImageResource(R.drawable.gallery);
                    break;
            }
            textView.setText(C);
        }
    }
}
