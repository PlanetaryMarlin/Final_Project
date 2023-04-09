package com.example.final_project.Mars;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.R;
import com.example.final_project.databinding.MarsFragmentBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MarsDetailsFragment extends Fragment {
    private MarsObj selected;
    private MarsFavDAO mrDAO;

    public MarsDetailsFragment(MarsObj r, MarsDatabase db){
        selected = r;
        mrDAO = db.mrDAO();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        MarsFragmentBinding binding = MarsFragmentBinding.inflate(inflater);

        binding.marsFragmentImage.setImageBitmap(selected.getBitmap());
        binding.cameraFragmentText.setText(selected.getCamName());
        binding.urlFragmentText.setText(selected.getImgSrc());

        List<MarsFav> search = new ArrayList<>();
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            search.addAll(mrDAO.searchByID(selected.getImgID()));
            getActivity().runOnUiThread(() -> {
                if (search.size() == 0) {
                    binding.saveButton.setVisibility(View.VISIBLE);
                    binding.saveButton.setEnabled(true);
                    binding.delButton.setVisibility(View.INVISIBLE);
                    binding.delButton.setEnabled(false);
                } else {
                    binding.saveButton.setVisibility(View.INVISIBLE);
                    binding.saveButton.setEnabled(false);
                    binding.delButton.setVisibility(View.VISIBLE);
                    binding.delButton.setEnabled(true);
                }
            });
        });

        binding.saveButton.setOnClickListener(saveClk -> {
            binding.saveButton.setVisibility(View.INVISIBLE);
            binding.saveButton.setEnabled(false);
            binding.delButton.setVisibility(View.VISIBLE);
            binding.delButton.setEnabled(true);
            FileOutputStream fOut = null;
            String filename = selected.getImgID() + ".png";
            try { fOut = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
                selected.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            MarsFav fav = new MarsFav(
                    selected.getImgID(),
                    selected.getImgSrc(),
                    selected.getCamName(),
                    selected.getRoverName(),
                    filename
            );
            thread.execute(() -> {
                mrDAO.insertFav(fav);
            });
        });

        binding.delButton.setOnClickListener(delClk -> {
            binding.saveButton.setVisibility(View.VISIBLE);
            binding.saveButton.setEnabled(true);
            binding.delButton.setVisibility(View.INVISIBLE);
            binding.delButton.setEnabled(false);
            thread.execute(() -> {
                mrDAO.deleteFav((MarsFav) (MarsObj) selected);
            });
            Snackbar.make(binding.getRoot(), "Removed from favourites", Snackbar.LENGTH_LONG)
                    .setAction("Undo", snackClk ->{
                        thread.execute(() -> {
                            mrDAO.insertFav((MarsFav) selected);
                        });
                        binding.saveButton.setVisibility(View.INVISIBLE);
                        binding.saveButton.setEnabled(false);
                        binding.delButton.setVisibility(View.VISIBLE);
                        binding.delButton.setEnabled(true);
                    })
                    .show();
        });

        return binding.getRoot();
    }
}
