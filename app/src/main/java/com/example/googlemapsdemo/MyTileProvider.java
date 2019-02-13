package com.example.googlemapsdemo;

import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;

public class MyTileProvider extends UrlTileProvider {
    private static final String TILE_IMAGE_URL_STRING = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3a/Cat03.jpg/200px-Cat03.jpg";
    private static final URL TILE_IMAGE_URL;
    private static final int MIN_TILE_SUPPORTED_ZOOM_LEVEL = 3;
    private static final int MAX_TILE_SUPPORTED_ZOOM_LEVEL = 15;

    static {
        URL tileImageUrl = null;
        try {
            tileImageUrl = new URL(TILE_IMAGE_URL_STRING);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        TILE_IMAGE_URL = tileImageUrl;
    }

    public MyTileProvider() {
        super(250, 250);
    }

    @Override
    public URL getTileUrl(int x, int y, int zoom) {
        if (checkIfTileNeeded(x, y, zoom)) {
            return TILE_IMAGE_URL;
        } else {
            return null;
        }
    }

    private static boolean checkIfTileNeeded(int x, int y, int zoom) {
        return (zoom >= MIN_TILE_SUPPORTED_ZOOM_LEVEL) && (zoom <= MAX_TILE_SUPPORTED_ZOOM_LEVEL);
    }
}
