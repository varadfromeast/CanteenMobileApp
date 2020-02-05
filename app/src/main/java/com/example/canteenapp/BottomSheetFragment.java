package com.example.canteenapp;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


/**
 * A simple {@link Fragment} subclass.
 */
public class BottomSheetFragment extends BottomSheetDialogFragment {

    int preparationTime;
    double total;
    TextView prepText;
    ImageView qrImage;
    TextView orderNoText;
    TextView totalText;

    public BottomSheetFragment() {
        // Required empty public constructor
        preparationTime = 0;
        total = 0.0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
       prepText = (TextView) view.findViewById(R.id.prepTimeTextView);
       qrImage = (ImageView) view.findViewById(R.id.qrImage);
       orderNoText = (TextView) view.findViewById(R.id.orderNoText);
       totalText = (TextView) view.findViewById(R.id.totalAmtText);
       prepText.setText("Thank you for your order!");
       QRGEncoder qrgEncoder = new QRGEncoder(String.valueOf(preparationTime), null, QRGContents.Type.TEXT, 200);
        try {
            qrImage.setImageBitmap(qrgEncoder.encodeAsBitmap());
        } catch (WriterException e) {
            e.printStackTrace();
        }
        orderNoText.setText(String.format("Order#%d", preparationTime));
        totalText.setText(String.format("Total: Rs.%.2f/-", total));
       return view;
    }
}
