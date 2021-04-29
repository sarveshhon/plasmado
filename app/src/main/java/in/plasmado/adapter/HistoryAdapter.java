package in.plasmado.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.plasmado.R;
import in.plasmado.model.HistoryModel;

import static in.plasmado.helper.ParamHelper.ID;
import static in.plasmado.helper.ParentHelper.checkInternet;
import static in.plasmado.helper.ParentHelper.inputFormat;
import static in.plasmado.helper.UrlHelper.BASE_KEY;
import static in.plasmado.helper.UrlHelper.BASE_URL;
import static in.plasmado.helper.UrlHelper.DELETE;
import static in.plasmado.helper.UrlHelper.UPDATE;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    Context context;
    List<HistoryModel> list;

    public HistoryAdapter(Context context, List<HistoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {

        holder.tvBloodGroup.setText(list.get(position).getBloodgroup());
        holder.tvAge.setText("Age: " + list.get(position).getAge());
        holder.tvGender.setText("Gender: " + list.get(position).getGender());
        holder.tvName.setText(list.get(position).getName());
        holder.tvAddress.setText(list.get(position).getLandmark() + ", " + list.get(position).getCity() + ", " + list.get(position).getDistrict() + ", " + list.get(position).getState() + ", " + list.get(position).getPin());
        if (list.get(position).getStage().equals("true")) {
            holder.tvStage.setVisibility(View.VISIBLE);
            holder.tvStage.setTextColor(context.getResources().getColor(R.color.green));
            holder.tvStage.setText("Request Completed");
            holder.llBtn.setVisibility(View.GONE);
        } else if (list.get(position).getStage().equals("false")) {
            holder.tvStage.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvStage.setVisibility(View.VISIBLE);
            holder.tvStage.setText("Pending Request");
            holder.llBtn.setVisibility(View.VISIBLE);
        } else {
            holder.tvStage.setTextColor(context.getResources().getColor(R.color.black_light));
            holder.tvStage.setVisibility(View.GONE);
            holder.llBtn.setVisibility(View.GONE);
        }

        Date date = null;
        try {
            date = inputFormat.parse(list.get(position).getDatetime());
            String niceDateStr = (String) DateUtils.getRelativeTimeSpanString(date.getTime(), Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS);

            holder.tvTimeDate.setText("Requested : " + niceDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(v -> {

        });

        holder.btnCancle.setOnClickListener(v -> {
            if (checkInternet(context)) {
                holder.llBtn.setVisibility(View.GONE);
                cancleRequest(list.get(position).get_id());
                Toast.makeText(context, "Request Canceled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Please Turn ON Internet Connection.", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnCompleted.setOnClickListener(v -> {
            if (checkInternet(context)) {
                holder.llBtn.setVisibility(View.GONE);
                completeRequest(list.get(position).get_id());
                Toast.makeText(context, "Request Completed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Please Turn ON Internet Connection.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvBloodGroup, tvGender, tvAge, tvAddress, tvTimeDate, tvName, tvStage;
        TextView btnCancle, btnCompleted;
        LinearLayout llBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvBloodGroup = itemView.findViewById(R.id.tvBloodGroup);
            tvGender = itemView.findViewById(R.id.tvGender);
            tvAge = itemView.findViewById(R.id.tvAge);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvTimeDate = itemView.findViewById(R.id.tvTimeDate);
            tvName = itemView.findViewById(R.id.tvName);
            tvStage = itemView.findViewById(R.id.tvStage);
            btnCancle = itemView.findViewById(R.id.btnCancle);
            btnCompleted = itemView.findViewById(R.id.btnCompleted);
            llBtn = itemView.findViewById(R.id.llBtn);
        }
    }

    private void cancleRequest(String _id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL + DELETE + BASE_KEY, response -> {

        }, error -> {

        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<>();
                map.put(ID, _id);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    private void completeRequest(String _id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL + UPDATE + BASE_KEY, response -> {

        }, error -> {

        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<>();
                map.put(ID, _id);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

}
