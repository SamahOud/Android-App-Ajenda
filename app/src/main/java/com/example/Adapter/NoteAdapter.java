package com.example.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DatabaseHelper.DataNoteHelper;
import com.example.ajenda_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    // Initialize variable
    Activity activity;
    JSONArray jsonArray;
    DataNoteHelper databaseHelper;

    // Create constructor
    public NoteAdapter(Activity activity, JSONArray jsonArray) {
        this.activity = activity;
        this.jsonArray = jsonArray;
    }

    // Create update array constructor
    public void updateArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Initialize view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_cell_two, parent,false);
        // Initialize database
        databaseHelper = new DataNoteHelper(view.getContext());
        // Pass holder view
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            // Initialize json object
            JSONObject object = jsonArray.getJSONObject(position);
            // Set title on textView
            holder.tv_title_txt.setText(object.getString("title"));
            // Set text on textView
            holder.tv_detail.setText(object.getString("detail"));
            // Set date on textView
            holder.tv_date.setText(object.getString("date"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Initialize json object
                    JSONObject object = jsonArray.getJSONObject(holder.getAdapterPosition());
                    // Get values from json array
                    String sID = object.getString("id");
                    String sTitle = object.getString("title");
                    String sDetail = object.getString("detail");

                    // Initialize dialog
                    Dialog dialog = new Dialog(activity);
                    // Set background transparent
                    dialog.getWindow().setBackgroundDrawable(new
                            ColorDrawable(Color.TRANSPARENT
                    ));
                    // Set view
                    dialog.setContentView(R.layout.dialog_note);
                    dialog.show(); // Display dialog

                    // Initialize and assign variable
                    EditText editTitle = dialog.findViewById(R.id.edit_title);
                    EditText editDetail = dialog.findViewById(R.id.edit_detail);
                    Button btUpdate = dialog.findViewById(R.id.bt_submit);

                    // Set previous title and text on edit text
                    editTitle.setText(sTitle);
                    editDetail.setText(sDetail);
                    // Set update title and text on button
                    btUpdate.setText("Update");

                    // Set listener on update button
                    btUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Get title and text from editText
                            String sTitle = editTitle.getText().toString().trim();
                            String sDetail = editDetail.getText().toString().trim();

                            // Update date
                            String sDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                                    .format(new Date());
                            // Update selected value
                            databaseHelper.updateData(sID, sTitle, sDetail, sDate);
                            // Refresh array
                            updateArray(databaseHelper.getArrayData());
                            // Notify adapter
                            notifyItemChanged(holder.getAdapterPosition());
                            dialog.dismiss(); // Close dialog
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Get selected item position
                int position = holder.getAdapterPosition();
                try {
                    // Initialize json object
                    JSONObject object = jsonArray.getJSONObject(position);
                    // Get value from json array
                    String sID = object.getString("id");

                    // Initialize alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Confirm");                   // Set title
                    builder.setMessage("Are you sure to delete?"); // Set message
                    builder.setIcon(R.drawable.history_img);       // Set Icon
                    // Set positive button
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            databaseHelper.deleteData(sID);  // Delete selected value
                            jsonArray.remove(position);  // Remove from json array
                            notifyItemRemoved(position); // Notify adapter
                            notifyItemRangeChanged(position,jsonArray.length());
                        }
                    });
                    // Set negative button
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); // Close dialog
                        }
                    });
                    builder.show(); // Display dialog

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return jsonArray.length(); // Pass json array length
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Initialize variable
        TextView tv_title_txt, tv_detail, tv_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Assign variable
            tv_title_txt = itemView.findViewById(R.id.tv_title_txt);
            tv_detail = itemView.findViewById(R.id.tv_detail);
            tv_date = itemView.findViewById(R.id.tv_date);

        }
    }

}
