package com.example.final_project.Mars;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.final_project.databinding.MarsFragmentBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Class for fragment showing photo details
 */
public class MarsDetailsFragment extends Fragment {
    /** marsResult object holding currently selected object*/
    private MarsResult selected;
    /** DAO for interacting with database*/
    private MarsResultDAO mrDAO;

    /**
     * Class constructor
     * @param r MarsResult currently selected
     * @param db database of saved photos
     */
    public MarsDetailsFragment(MarsResult r, MarsDatabase db){
        selected = r;
        mrDAO = db.mrDAO();
    }

    /**
     * OnCreateView method for displaying data in fragment
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return View from ViewBinding
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        MarsFragmentBinding binding = MarsFragmentBinding.inflate(inflater);

        binding.marsFragmentImage.setImageBitmap(selected.getBitmap());
        binding.cameraFragmentText.setText(selected.getCamName());
        binding.urlFragmentText.setText(selected.getImgSrc());

        //Checks if an instance already exists in database
        List<MarsResult> search = new ArrayList<>();
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            search.addAll(mrDAO.searchByID(selected.getImgID()));
            getActivity().runOnUiThread(() -> {
                if (search.size() == 0) { //If no match, show save button
                    binding.saveButton.setVisibility(View.VISIBLE);
                    binding.saveButton.setEnabled(true);
                    binding.delButton.setVisibility(View.INVISIBLE);
                    binding.delButton.setEnabled(false);
                } else { //If match, show delete button
                    binding.saveButton.setVisibility(View.INVISIBLE);
                    binding.saveButton.setEnabled(false);
                    binding.delButton.setVisibility(View.VISIBLE);
                    binding.delButton.setEnabled(true);
                }
            });
        });

        //Click listener to save a photo
        binding.saveButton.setOnClickListener(saveClk -> {
            binding.saveButton.setVisibility(View.INVISIBLE);
            binding.saveButton.setEnabled(false);
            binding.delButton.setVisibility(View.VISIBLE);
            binding.delButton.setEnabled(true);

            //Saves bitmap to local storage
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

            MarsResult fav = new MarsResult(
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

        //Click listener for delete button
        binding.delButton.setOnClickListener(delClk -> {
            binding.saveButton.setVisibility(View.INVISIBLE);
            binding.saveButton.setEnabled(false);
            binding.delButton.setVisibility(View.INVISIBLE);
            binding.delButton.setEnabled(false);

            thread.execute(() -> {
                mrDAO.deleteFav(selected);
            });

            //Snackbar after deleting, gives undo option
            Snackbar.make(binding.getRoot(), "Removed from favourites", Snackbar.LENGTH_LONG)
                    .setAction("Undo", snackClk ->{
                        thread.execute(() -> {
                            mrDAO.insertFav(selected);
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
