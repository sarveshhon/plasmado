package in.plasmado.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.plasmado.R;
import in.plasmado.model.RequestModel;

import static in.plasmado.MainActivity.sharedpreferences;
import static in.plasmado.helper.ParamHelper.PHONE;
import static in.plasmado.helper.ParentHelper.inputFormat;
import static in.plasmado.helper.ParentHelper.showCustomDialog;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.CALL_PHONE};
    Dialog dialog;

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
        dialog = new Dialog(context);


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
            String niceDateStr = (String) DateUtils.getRelativeTimeSpanString(date.getTime(), Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS);

            holder.tvTimeDate.setText("Requested : " + niceDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(v -> {
            // Configure Dialog Propertied
            showCustomDialog(dialog, context.getResources().getString(R.string.dialog_call), R.layout.dialog_call);
            dialog.findViewById(R.id.btnClose).setOnClickListener(i -> {
                dialog.dismiss();
            });

            dialog.findViewById(R.id.btnYes).setOnClickListener(i -> {
                dialog.dismiss();
                if (!hasPermissions(context, PERMISSIONS)) {
                    ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, PERMISSION_ALL);
                } else {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + list.get(position).getPhone()));
                    context.startActivity(callIntent);
                }

            });

            if (!(list.get(position).getPhone().equals(sharedpreferences.getString(PHONE, "unknown")))) {
                dialog.show();
            } else {
                Toast.makeText(context, "You Can't Call Yourself", Toast.LENGTH_SHORT).show();
            }
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

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
