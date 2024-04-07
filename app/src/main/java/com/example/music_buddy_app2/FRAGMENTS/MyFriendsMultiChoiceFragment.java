package com.example.music_buddy_app2.FRAGMENTS;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_buddy_app2.ADAPTERS.CONTEXT_RECS.FriendsPlaylistsAdapter;
import com.example.music_buddy_app2.ADAPTERS.USERS.FriendsChecklistAdapter;
import com.example.music_buddy_app2.ADAPTERS.USERS.ManageFriendsAdapter;
import com.example.music_buddy_app2.MODELS.User;
import com.example.music_buddy_app2.R;

import java.util.ArrayList;
import java.util.List;

public class MyFriendsMultiChoiceFragment extends DialogFragment {
    private List<User> friendsList;
    private List<User> selectedFriends = new ArrayList<>();
    private OnFriendsSelectedListener mListener;
    public List<User> getSelectedFriends() {
        return selectedFriends;
    }
    public interface OnFriendsSelectedListener {
        void onFriendsSelected(List<User> selectedFriends);
    }

    public MyFriendsMultiChoiceFragment(List<User> friendsList,OnFriendsSelectedListener listener) {
        this.friendsList = friendsList;
        mListener=listener;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_choose_friends, null);

        RecyclerView recyclerView = view.findViewById(R.id.friends_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FriendsChecklistAdapter adapter = new FriendsChecklistAdapter(getContext());
        adapter.setUsers(friendsList);
        recyclerView.setAdapter(adapter);

        Button okButton = view.findViewById(R.id.okButton);
        okButton.setOnClickListener(v -> {
            for (User user : friendsList) {
                if (user.isSelected()) {
                    selectedFriends.add(user);
                }
            }
            if (mListener != null) {
                mListener.onFriendsSelected(selectedFriends);
            }
            dismiss();
        });

        builder.setView(view);
        return builder.create();
    }
    public void setOnFriendsSelectedListener(OnFriendsSelectedListener listener) {
        mListener = listener;
    }

}
