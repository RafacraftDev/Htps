package <package>;

import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.URL;
import java.net.HttpURLConnection;

import android.app.Activity;

public class Htps {

    private HttpURLConnection conn;
    private URL url;
    private Activity activity;

    public Htps(Activity act, String urlString, String method) throws Exception {
        activity = act;
        url = new URL(urlString);
        conn = (HttpURLConnection) url.openConnection();

        if (method.equalsIgnoreCase("POST")) {
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
        } else if (method.equalsIgnoreCase("GET")) {
            conn.setRequestMethod("GET");
        } else {
            throw new Exception("Error: use GET or POST");
        }

        conn.setDoInput(true);
    }

    public void setProperty(String key, String value) {
        conn.setRequestProperty(key, value);
    }

    public void setContent(String value) throws Exception {
        OutputStream os = null;
        try {
            os = conn.getOutputStream();
            byte[] input = value.getBytes("utf-8");
            os.write(input, 0, input.length);
        } finally {
            if (os != null) os.close();
        }
    }

    public void getContentAsync(final ResponseCallback callback) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String result;
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }
                    result = response.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    result = e.toString();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    conn.disconnect();
                }

                if (callback != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onResponse(result);
                        }
                    });
                }
            }
        });
        t.start();
    }

    public interface ResponseCallback {
        void onResponse(String response);
    }
}
