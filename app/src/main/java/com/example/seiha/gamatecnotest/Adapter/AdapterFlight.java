package com.example.seiha.gamatecnotest.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.seiha.gamatecnotest.Item.ItemFlight;
import com.example.seiha.gamatecnotest.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;

/**
 * Created by seiha on 2/6/2017.
 */

public class AdapterFlight extends RecyclerView.Adapter<AdapterFlight.ViewHolder> {

    private List<ItemFlight> list;
    private Context context;
    private View view;

    public AdapterFlight(Context context, List<ItemFlight> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public AdapterFlight.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listfidr, parent, false);
        AdapterFlight.ViewHolder holder = new AdapterFlight.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final AdapterFlight.ViewHolder holder, final int position) {
        if (position % 2 == 0) {
            holder.forHightlight.setBackgroundResource(android.R.color.white);
        } else {
            holder.forHightlight.setBackgroundResource(R.color.bitDark);
        }
        holder.TV_flight.setText(list.get(position).getFlight());
        holder.TV_airlineName.setText(list.get(position).getAirlineName());
        holder.TV_time.setText(list.get(position).getTime());
        holder.TV_remarks.setText(list.get(position).getRemarks());
        holder.TV_airportName.setText(list.get(position).getAirportName());
        holder.TV_destination.setText(list.get(position).getDestination());

        String logourl =list.get(position).getAirlineLogo();
        byte[] data = new byte[0];
        try {
            data = logourl.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);

        final File logo = new File( "/data/data/" + context.getPackageName() + "/cache/" + base64);
        if (logo.exists() && logo.length() > 0) {
            holder.IV_logo.setImageBitmap(BitmapFactory.decodeFile(logo.getPath()));
        } else {
            new AsyncTask<Void, Void, Void>() {
                Bitmap bmp;

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        InputStream in = new URL(list.get(position).getAirlineLogo()).openStream();
                        bmp = BitmapFactory.decodeStream(in);
                    } catch (Exception e) {
                        // log error
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    if (bmp != null)
                        holder.IV_logo.setImageBitmap(bmp);
                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(logo);
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                        // PNG is a lossless format, the compression factor (100) is ignored
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.execute();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout forHightlight;
        AppCompatTextView TV_airlineName, TV_flight, TV_time, TV_remarks, TV_airportName, TV_destination;
        AppCompatImageView IV_logo;

        ViewHolder(View itemView) {
            super(itemView);
            forHightlight = (LinearLayout) itemView.findViewById(R.id.forHightlight);
            IV_logo = (AppCompatImageView) itemView.findViewById(R.id.IV_logo);
            TV_flight = (AppCompatTextView) itemView.findViewById(R.id.TV_flight);
            TV_airlineName = (AppCompatTextView) itemView.findViewById(R.id.TV_airlineName);
            TV_time = (AppCompatTextView) itemView.findViewById(R.id.TV_time);
            TV_remarks = (AppCompatTextView) itemView.findViewById(R.id.TV_remarks);
            TV_airportName = (AppCompatTextView) itemView.findViewById(R.id.TV_airportName);
            TV_destination = (AppCompatTextView) itemView.findViewById(R.id.TV_destination);
        }
    }
}
