// InicioViewModel.java
package com.example.inmobiliariazaratemobile.ui.inicio;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InicioViewModel extends ViewModel {

    public static final double LAT = -33.14897835218867;
    public static final double LNG = -66.30736406356966;
    public static final int ZOOM = 16;
    private static final String TITLE = "Inmobiliaria";
    private static final String BASE_URL = "https://example.local/";

    public static class UiModel {
        public final String baseUrl;
        public final String html;
        public final boolean javascriptEnabled;
        public final int webVisibility;

        public UiModel(String baseUrl, String html, boolean javascriptEnabled, int webVisibility) {
            this.baseUrl = baseUrl;
            this.html = html;
            this.javascriptEnabled = javascriptEnabled;
            this.webVisibility = webVisibility;
        }
    }

    private final MutableLiveData<UiModel> uiModel = new MutableLiveData<>();

    public InicioViewModel() {
        String html = buildLeafletHtml(LAT, LNG, ZOOM, TITLE);
        uiModel.setValue(new UiModel(
                BASE_URL,
                html,
                true,
                View.VISIBLE
        ));
    }

    public LiveData<UiModel> getUiModel() {
        return uiModel;
    }

    // Toda la composición de la UI queda resuelta aquí.
    private String buildLeafletHtml(double lat, double lng, int zoom, String titulo) {
        return "<!DOCTYPE html><html><head>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'/>" +
                "<link rel='stylesheet' href='https://unpkg.com/leaflet@1.9.4/dist/leaflet.css'/>" +
                "<style>html,body,#map{height:100%;margin:0;padding:0}</style>" +
                "</head><body><div id='map'></div>" +
                "<script src='https://unpkg.com/leaflet@1.9.4/dist/leaflet.js'></script>" +
                "<script>" +
                "var map=L.map('map').setView([" + lat + "," + lng + "], " + zoom + ");" +
                "L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',{" +
                "maxZoom:19, attribution:'&copy; OpenStreetMap contributors'}).addTo(map);" +
                "L.marker([" + lat + "," + lng + "]).addTo(map).bindPopup('" + titulo + "').openPopup();" +
                "</script></body></html>";
    }


}
