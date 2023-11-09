package com.example.sicc.fragments;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.sicc.R;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;

public class ProgressFragment extends Fragment {
    private StepView stepView;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_progress, container, false);

        init();

        return view;
    }

    private void init() {
        stepView = view.findViewById(R.id.step_view);

        settingStepView();

        // Initial fragment Progress
        requireActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                .replace(R.id.fragment_container_progress, MsgSuccesFragment.class, null)
                .addToBackStack(null)
                .commit();

        // Handle step click events
        stepView.setOnStepClickListener(step -> {
            if (step != stepView.getCurrentStep()) {
                switch (step) {
                    case 0:
                        replaceFragment(new MsgSuccesFragment());
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }

                stepView.go(step, true);
            }
        });
    }

    private void settingStepView() {
        stepView.getState()
                .animationType(StepView.ANIMATION_ALL)
                .steps(new ArrayList<String>() {{
                    add("Kelompok");
                    add("Dospem");
                    add("Judul");
                    add("Proposal");
                    add("Lolos");
                }})
                .stepsNumber(5)
                .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .commit();

        // Set an initial step as selected
        stepView.go(0, true);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragment_container_progress, fragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}