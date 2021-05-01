package in.plasmado.fragement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.plasmado.R;
import in.plasmado.adapter.HistoryAdapter;
import in.plasmado.adapter.RequestAdapter;
import in.plasmado.databinding.FragmentHistoryBinding;
import in.plasmado.model.HistoryModel;
import in.plasmado.model.RequestModel;

import static in.plasmado.MainActivity.sharedpreferences;
import static in.plasmado.helper.ParamHelper.AGE;
import static in.plasmado.helper.ParamHelper.BLOODGROUP;
import static in.plasmado.helper.ParamHelper.CITY;
import static in.plasmado.helper.ParamHelper.DATETIME;
import static in.plasmado.helper.ParamHelper.DISTRICT;
import static in.plasmado.helper.ParamHelper.EMAIl;
import static in.plasmado.helper.ParamHelper.GENDER;
import static in.plasmado.helper.ParamHelper.ID;
import static in.plasmado.helper.ParamHelper.LANDMARK;
import static in.plasmado.helper.ParamHelper.NAME;
import static in.plasmado.helper.ParamHelper.PHONE;
import static in.plasmado.helper.ParamHelper.PINCODE;
import static in.plasmado.helper.ParamHelper.STAGE;
import static in.plasmado.helper.ParamHelper.STATE;
import static in.plasmado.helper.ParentHelper.checkInternet;
import static in.plasmado.helper.UrlHelper.BASE_KEY;
import static in.plasmado.helper.UrlHelper.BASE_URL;
import static in.plasmado.helper.UrlHelper.HISTORY;
import static in.plasmado.helper.UrlHelper.REQUEST;

public class HistoryFragment extends Fragment {

    FragmentHistoryBinding mBinding;

    List<HistoryModel> list = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentHistoryBinding.inflate(getLayoutInflater());

        ImageView imageView = mBinding.getRoot().findViewById(R.id.ivNotFound);
        Glide.with(getContext()).load(R.raw.ic_not_found).into(imageView);

        loadHistoryData();

        mBinding.swipeRefresh.setOnRefreshListener(() -> {
            if(checkInternet(getContext())){
                mBinding.swipeRefresh.setRefreshing(true);
                loadHistoryData();
            }else{
                Toast.makeText(getContext(), "Please Turn ON Internet Connection.", Toast.LENGTH_SHORT).show();
            }

        });

        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {



    }


    public void loadHistoryData(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL+ HISTORY +BASE_KEY, response -> {

            list.clear();
            try {
                JSONArray jsonArray = new JSONArray(response);
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(new HistoryModel(jsonObject.getString(ID),
                            jsonObject.getString(NAME),
                            jsonObject.getString(PHONE),
                            jsonObject.getString(EMAIl),
                            jsonObject.getString(AGE),
                            jsonObject.getString(PINCODE),
                            jsonObject.getString(CITY),
                            jsonObject.getString(DISTRICT),
                            jsonObject.getString(LANDMARK),
                            jsonObject.getString(STATE),
                            jsonObject.getString(GENDER),
                            jsonObject.getString(BLOODGROUP),
                            jsonObject.getString(DATETIME),
                            jsonObject.getString(STAGE)
                    ));
                }

                if(jsonArray.length() == 0){
                    mBinding.inf.getRoot().setVisibility(View.VISIBLE);
                }else{
                    mBinding.inf.getRoot().setVisibility(View.GONE);
                }

                mBinding.swipeRefresh.setRefreshing(false);
                mBinding.rvHistory.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                HistoryAdapter historyAdapter = new HistoryAdapter(getActivity(), list);
                mBinding.rvHistory.setAdapter(historyAdapter);
                historyAdapter.notifyDataSetChanged();

            }catch (Exception e){

            }
        }, error -> {

        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String > map = new HashMap<>();
                map.put(PHONE,sharedpreferences.getString(PHONE,"unknown"));

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


}