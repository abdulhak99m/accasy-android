package com.starglare.accasy.fragments;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.starglare.accasy.R;
import com.starglare.accasy.core.Helper;

import org.w3c.dom.Text;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FeedbackFragment extends Fragment {



    public FeedbackFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feedback, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();

        final TextView name = (TextView) view.findViewById(R.id.name);
        final TextView email = (TextView) view.findViewById(R.id.email);
        final TextView feedback = (TextView) view.findViewById(R.id.feedback);
        Button send = (Button) view.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean sendFeedback = true;
                if(TextUtils.isEmpty(name.getText())){
                    name.setError("Required");
                    sendFeedback = false;
                }
                if(TextUtils.isEmpty(email.getText())){
                    email.setError("Required");
                    sendFeedback = false;
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()){
                    email.setError("Invalid email");
                    sendFeedback = false;
                }

                if(TextUtils.isEmpty(feedback.getText())) {
                    feedback.setError("Required");
                    sendFeedback = false;
                }

                if(sendFeedback){
                    new SweetAlertDialog(getContext(), SweetAlertDialog.BUTTON_NEGATIVE)
                            .setTitleText("Sorry")
                            .setContentText("Couldn't send your feedback. Try again later.")
                            .show();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
