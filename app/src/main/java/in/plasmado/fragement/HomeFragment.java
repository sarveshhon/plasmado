package in.plasmado.fragement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import in.plasmado.HomeActivity;
import in.plasmado.R;
import in.plasmado.databinding.FragmentHomeBinding;

import static in.plasmado.helper.ParentHelper.addFragment;
import static in.plasmado.helper.ParentHelper.replaceFragment;

public class HomeFragment extends Fragment {

    public FragmentHomeBinding mBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentHomeBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mBinding.cvHistory.setOnClickListener(v -> {
            addFragment(getActivity(), R.id.flHomeContainer, new HistoryFragment());
        });

        mBinding.cvProfile.setOnClickListener(v -> {

            addFragment(getActivity(), R.id.flHomeContainer, new ProfileFragment());
        });



    }
}