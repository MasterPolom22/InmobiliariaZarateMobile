package com.example.inmobiliariazaratemobile.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.inmobiliariazaratemobile.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliariazaratemobile.databinding.FragmentInicioBinding;

public class InicioFragment extends Fragment {

    private static final double LAT = -33.14897835218867;
    private static final double LNG = -66.30736406356966;
    private static final int ZOOM = 16;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inicio, container, false);
        WebView web = v.findViewById(R.id.webMap);

        web.setWebViewClient(new WebViewClient());
        WebSettings ws = web.getSettings();
        ws.setJavaScriptEnabled(true);

        String html = buildLeafletHtml(LAT, LNG, ZOOM, "Inmobiliaria");
        web.loadDataWithBaseURL(
                "https://example.local/",
                html,
                "text/html",
                "UTF-8",
                null
        );
        return v;
    }

    private String buildLeafletHtml(double lat, double lng, int zoom, String titulo) {
        return "<!DOCTYPE html><html><head>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'/>" +
                "<link rel='stylesheet' href='https://unpkg.com/leaflet@1.9.4/dist/leaflet.css'/>" +
                "<style>html,body,#map{height:100%;margin:0;padding:0}</style>" +
                "</head><body><div id='map'></div>" +
                "<script src='https://unpkg.com/leaflet@1.9.4/dist/leaflet.js'></script>" +
                "<script>" +
                "var map=L.map('map').setView(["+lat+","+lng+"], "+zoom+");" +
                "L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',{" +
                "    maxZoom:19, attribution:'&copy; OpenStreetMap contributors'}).addTo(map);" +
                "L.marker(["+lat+","+lng+"]).addTo(map).bindPopup('"+titulo+"').openPopup();" +
                "</script></body></html>";
    }


}