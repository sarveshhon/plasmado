package in.plasmado.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.plasmado.R;
import in.plasmado.model.RequestModel;

import static in.plasmado.helper.ParentHelper.inputFormat;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    Context context;
    List<RequestModel> list;

    public RequestAdapter(Context context, List<RequestModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.ViewHolder holder, int position) {

        holder.tvBloodGroup.setText(list.get(position).getBloodgroup());
        holder.tvAge.setText("Age: " + list.get(position).getAge());
        holder.tvGender.setText("Gender: " + list.get(position).getGender());
        holder.tvName.setText(list.get(position).getName());
        holder.tvAddress.setText(list.get(position).getLandmark() + ", " + list.get(position).getCity() + ", " + list.get(position).getDistrict() + ", " + list.get(position).getState() + ", " + list.get(position).getPin());

        Date date = null;
        try {
            date = inputFormat.parse(list.get(position).getDatetime());
            String niceDateStr = (String) DateUtils.getRelativeTimeSpanString(date.getTime() , Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS);

            holder.tvTimeDate.setText("Requested : " + niceDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+list.get(position).getPhone()));
            context.startActivity(callIntent);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvBloodGroup, tvGender, tvAge, tvAddress, tvTimeDate, tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvBloodGroup = itemView.findViewById(R.id.tvBloodGroup);
            tvGender = itemView.findViewById(R.id.tvGender);
            tvAge = itemView.findViewById(R.id.tvAge);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvTimeDate = itemView.findViewById(R.id.tvTimeDate);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
