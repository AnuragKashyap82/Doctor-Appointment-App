package kashyap.anurag.medicalservice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kashyap.anurag.medicalservice.Models.ModelAllUsers;
import kashyap.anurag.medicalservice.R;

public class AdapterAllUsers extends RecyclerView.Adapter<AdapterAllUsers.HolderAllUsers> {

    private Context context;
    private ArrayList<ModelAllUsers> usersArrayList;

    public AdapterAllUsers(Context context, ArrayList<ModelAllUsers> usersArrayList) {
        this.context = context;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public HolderAllUsers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_user_items, parent, false);
        return new HolderAllUsers(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderAllUsers holder, int position) {
        ModelAllUsers modelAllUsers = usersArrayList.get(position);
        String name = modelAllUsers.getName();
        String email = modelAllUsers.getEmail();
        String profileImage = modelAllUsers.getProfileImage();

        holder.nameTv.setText(name);
        holder.emailTv.setText(email);

        try {
            Picasso.get().load(profileImage).placeholder(R.drawable.doctor).into(holder.usersIv);
        } catch (Exception e) {
            holder.usersIv.setImageResource(R.drawable.doctor);
        }
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class HolderAllUsers extends RecyclerView.ViewHolder {

        private ImageView usersIv;
        private TextView nameTv, emailTv;

        public HolderAllUsers(@NonNull View itemView) {
            super(itemView);

            usersIv = itemView.findViewById(R.id.usersIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            emailTv = itemView.findViewById(R.id.emailTv);
        }
    }
}
