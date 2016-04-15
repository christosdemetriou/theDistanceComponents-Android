package uk.co.thedistance.slimmingworlddemo.rest;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedInput;


/**
 * Created by benbaggley on 11/08/15.
 */
class LocalClient implements Client {

    private AssetManager assets;
    public static final String ASSETS_ENDPOINT = "asset:///";
    private final boolean fromAssets;

    public LocalClient(Context context, boolean fromAssets) {
        if (fromAssets) {
            this.assets = context.getAssets();
        }
        this.fromAssets = fromAssets;
    }

    @Override
    public Response execute(Request request) throws IOException {
        InputStream inputStream;
        try {
            if (fromAssets) {
                String path = Uri.parse(request.getUrl()).getPath();
                if (path.startsWith("/")) {
                    path = path.substring(1);
                }
                inputStream = assets.open(path);
            } else {
                inputStream = new FileInputStream(request.getUrl());
            }
        } catch (IOException e) {
            return new Response(request.getUrl(), 404, e.getMessage(), new ArrayList<Header>(), null);
        }

        String mimeType = "application/json";

        TypedInput body = new TypedInputStream(mimeType, inputStream.available(), inputStream);
        return new Response(request.getUrl(), 200, "Content from " + request.getUrl(), new ArrayList<Header>(), body);
    }

    private static class TypedInputStream implements TypedInput {
        private final String mimeType;
        private final long length;
        private final InputStream stream;

        private TypedInputStream(String mimeType, long length, InputStream stream) {
            this.mimeType = mimeType;
            this.length = length;
            this.stream = stream;
        }

        @Override
        public String mimeType() {
            return mimeType;
        }

        @Override
        public long length() {
            return length;
        }

        @Override
        public InputStream in() throws IOException {
            return stream;
        }
    }
}
