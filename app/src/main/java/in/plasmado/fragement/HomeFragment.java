package in.plasmado.fragement;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.plasmado.R;
import in.plasmado.adapter.RequestAdapter;
import in.plasmado.databinding.FragmentHomeBinding;
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
import static in.plasmado.helper.ParentHelper.addFragment;
import static in.plasmado.helper.ParentHelper.checkInternet;
import static in.plasmado.helper.ParentHelper.decrypt;
import static in.plasmado.helper.ParentHelper.getTimestamp;
import static in.plasmado.helper.ParentHelper.showCustomDialog;
import static in.plasmado.helper.UrlHelper.BASE_KEY;
import static in.plasmado.helper.UrlHelper.BASE_URL;
import static in.plasmado.helper.UrlHelper.HISTORY;
import static in.plasmado.helper.UrlHelper.REQUEST;

public class HomeFragment extends Fragment {

    public FragmentHomeBinding mBinding;

    Dialog dialog;

    List<RequestModel> list = new ArrayList<>();
    RequestAdapter requestAdapter;

    private static int count = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentHomeBinding.inflate(getLayoutInflater());

        loadRequestData();
        checkRequestAvailable();
        setCheckBox();

        dialog = new Dialog(getActivity());

        // Configure Dialog Propertied
        showCustomDialog(dialog, getResources().getString(R.string.dialog_request), R.layout.dialog_request);
        dialog.findViewById(R.id.btnClose).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.findViewById(R.id.btnYes).setOnClickListener(v -> {
            requestBlood();
            dialog.dismiss();
            Toast.makeText(getActivity(), "Plasma Requested.", Toast.LENGTH_LONG).show();
        });

        mBinding.cvHistory.setOnClickListener(v -> {
            if (checkInternet(getActivity())) {
                addFragment(getActivity(), R.id.flHomeContainer, new HistoryFragment());
            } else {
                Toast.makeText(getActivity(), "Please Turn ON Internet Connection.", Toast.LENGTH_SHORT).show();
            }
        });

        mBinding.cvProfile.setOnClickListener(v -> {
            if (checkInternet(getActivity())) {
                addFragment(getActivity(), R.id.flHomeContainer, new ProfileFragment());
            } else {
                Toast.makeText(getActivity(), "Please Turn ON Internet Connection.", Toast.LENGTH_SHORT).show();
            }
        });

        mBinding.cvRequest.setOnClickListener(v -> {
            if (checkInternet(getActivity())) {
                dialog.show();
            } else {
                Toast.makeText(getActivity(), "Please Turn ON Internet Connection.", Toast.LENGTH_SHORT).show();
            }

        });

        mBinding.swipeRefresh.setOnRefreshListener(() -> {
            if (checkInternet(getActivity())) {
                mBinding.swipeRefresh.setRefreshing(true);
                loadRequestData();
                setCheckBox();
            } else {
                Toast.makeText(getActivity(), "Please Turn ON Internet Connection.", Toast.LENGTH_SHORT).show();
            }
        });

        mBinding.cbState.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                loadRequestDataState();
            } else {
                if (!mBinding.cbDistrict.isChecked() && !mBinding.cbCity.isChecked() && !mBinding.cbPincode.isChecked()) {
                    loadRequestData();
                }
            }
        });

        mBinding.cbDistrict.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                loadRequestDataDistrict();
            } else {
                if (!mBinding.cbState.isChecked() && !mBinding.cbCity.isChecked() && !mBinding.cbPincode.isChecked()) {
                    loadRequestData();
                }
            }
        });

        mBinding.cbCity.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                loadRequestDataCity();
            } else {
                if (!mBinding.cbState.isChecked() && !mBinding.cbDistrict.isChecked() && !mBinding.cbPincode.isChecked()) {
                    loadRequestData();
                }
            }
        });

        mBinding.cbPincode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                loadRequestDataPincode();
            } else {
                if (!mBinding.cbState.isChecked() && !mBinding.cbDistrict.isChecked() && !mBinding.cbCity.isChecked()) {
                    loadRequestData();
                }
            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


    }

    private void setCheckBox() {
        mBinding.cbState.setChecked(false);
        mBinding.cbDistrict.setChecked(false);
        mBinding.cbCity.setChecked(false);
        mBinding.cbPincode.setChecked(false);
    }

    private void loadRequestDataState() {
        final int[] count = {0};

        list.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL + REQUEST + BASE_KEY, response -> {

            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);


                    if (decrypt(jsonObject.getString(STATE)).equals(decrypt(sharedpreferences.getString(STATE, "unknown")))) {

                        count[0] = 1;

                        list.add(new RequestModel(jsonObject.getString(ID),
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
                                jsonObject.getString(DATETIME)
                        ));

                    }
                }

                if (jsonArray.length() == 0) {
                    mBinding.inf.getRoot().setVisibility(View.VISIBLE);
                } else {
                    mBinding.inf.getRoot().setVisibility(View.GONE);
                }

                if (count[0] == 0) {
                    Toast.makeText(getActivity(), "Not Found", Toast.LENGTH_SHORT).show();
                }


                mBinding.swipeRefresh.setRefreshing(false);
                mBinding.rvHome.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                requestAdapter = new RequestAdapter(getActivity(), list);
                mBinding.rvHome.setAdapter(requestAdapter);
                requestAdapter.notifyDataSetChanged();

            } catch (Exception ignored) {

            }
        }, error -> {

        }) {

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void loadRequestDataDistrict() {
        final int[] count = {0};

        list.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL + REQUEST + BASE_KEY, response -> {

            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);


                    if (decrypt(jsonObject.getString(DISTRICT)).equals(decrypt(sharedpreferences.getString(DISTRICT, "unknown")))) {

                        count[0] = 1;

                        list.add(new RequestModel(jsonObject.getString(ID),
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
                                jsonObject.getString(DATETIME)
                        ));

                    }
                }

                if (jsonArray.length() == 0) {
                    mBinding.inf.getRoot().setVisibility(View.VISIBLE);
                } else {
                    mBinding.inf.getRoot().setVisibility(View.GONE);
                }


                if (count[0] == 0) {
                    Toast.makeText(getActivity(), "Not Found", Toast.LENGTH_SHORT).show();
                }


                mBinding.swipeRefresh.setRefreshing(false);
                mBinding.rvHome.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                requestAdapter = new RequestAdapter(getActivity(), list);
                mBinding.rvHome.setAdapter(requestAdapter);
                requestAdapter.notifyDataSetChanged();

            } catch (Exception ignored) {

            }
        }, error -> {

        }) {

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void loadRequestDataCity() {
        final int[] count = {0};

        list.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL + REQUEST + BASE_KEY, response -> {

            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);


                    if (decrypt(jsonObject.getString(CITY)).equals(decrypt(sharedpreferences.getString(CITY, "unknown")))) {

                        count[0] = 1;

                        list.add(new RequestModel(jsonObject.getString(ID),
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
                                jsonObject.getString(DATETIME)
                        ));

                    }
                }

                if (jsonArray.length() == 0) {
                    mBinding.inf.getRoot().setVisibility(View.VISIBLE);
                } else {
                    mBinding.inf.getRoot().setVisibility(View.GONE);
                }


                if (count[0] == 0) {
                    Toast.makeText(getActivity(), "Not Found", Toast.LENGTH_SHORT).show();
                }


                mBinding.swipeRefresh.setRefreshing(false);
                mBinding.rvHome.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                requestAdapter = new RequestAdapter(getActivity(), list);
                mBinding.rvHome.setAdapter(requestAdapter);
                requestAdapter.notifyDataSetChanged();

            } catch (Exception ignored) {

            }
        }, error -> {

        }) {

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void loadRequestDataPincode() {
        final int[] count = {0};

        list.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL + REQUEST + BASE_KEY, response -> {

            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);


                    if (decrypt(jsonObject.getString(PINCODE)).equals(decrypt(sharedpreferences.getString(PINCODE, "unknown")))) {

                        count[0] = 1;

                        list.add(new RequestModel(jsonObject.getString(ID),
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
                                jsonObject.getString(DATETIME)
                        ));

                    }
                }

                if (jsonArray.length() == 0) {
                    mBinding.inf.getRoot().setVisibility(View.VISIBLE);
                } else {
                    mBinding.inf.getRoot().setVisibility(View.GONE);
                }

                if (count[0] == 0) {
                    Toast.makeText(getActivity(), "Not Found", Toast.LENGTH_SHORT).show();
                }


                mBinding.swipeRefresh.setRefreshing(false);
                mBinding.rvHome.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                requestAdapter = new RequestAdapter(getActivity(), list);
                mBinding.rvHome.setAdapter(requestAdapter);
                requestAdapter.notifyDataSetChanged();

            } catch (Exception ignored) {

            }
        }, error -> {

        }) {

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void loadRequestData() {
        list.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL + REQUEST + BASE_KEY, response -> {

            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    list.add(new RequestModel(jsonObject.getString(ID),
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
                            jsonObject.getString(DATETIME)
                    ));
                }

                if (jsonArray.length() == 0) {
                    mBinding.inf.getRoot().setVisibility(View.VISIBLE);
                } else {
                    mBinding.inf.getRoot().setVisibility(View.GONE);
                }

                mBinding.swipeRefresh.setRefreshing(false);
                mBinding.rvHome.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                requestAdapter = new RequestAdapter(getActivity(), list);
                mBinding.rvHome.setAdapter(requestAdapter);
                requestAdapter.notifyDataSetChanged();

            } catch (Exception ignored) {

            }
        }, error -> {

        }) {

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void requestBlood() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL + REQUEST + BASE_KEY, response -> {

        }, error -> {

        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<>();
                map.put(NAME, sharedpreferences.getString(NAME, "unknown"));
                map.put(PHONE, sharedpreferences.getString(PHONE, "unknown"));
                map.put(EMAIl, sharedpreferences.getString(EMAIl, "unknown"));
                map.put(AGE, sharedpreferences.getString(AGE, "unknown"));
                map.put(PINCODE, sharedpreferences.getString(PINCODE, "unknown"));
                map.put(CITY, sharedpreferences.getString(CITY, "unknown"));
                map.put(DISTRICT, sharedpreferences.getString(DISTRICT, "unknown"));
                map.put(LANDMARK, sharedpreferences.getString(LANDMARK, "unknown"));
                map.put(STATE, sharedpreferences.getString(STATE, "unknown"));
                map.put(GENDER, sharedpreferences.getString(GENDER, "unknown"));
                map.put(BLOODGROUP, sharedpreferences.getString(BLOODGROUP, "unknown"));
                map.put(STAGE, "false");
                map.put(DATETIME, getTimestamp());

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }


    public void checkRequestAvailable() {
        list.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL + HISTORY + BASE_KEY, response -> {

            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if (jsonObject.getString("stage").equals("true")) {
                        count++;
                    }


                }


            } catch (Exception ignored) {

            }
        }, error -> {

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put(PHONE, sharedpreferences.getString(PHONE, "unknown"));

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


}