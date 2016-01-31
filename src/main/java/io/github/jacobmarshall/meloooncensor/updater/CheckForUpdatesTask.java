package io.github.jacobmarshall.meloooncensor.updater;

import com.bugsnag.Client;
import io.github.jacobmarshall.meloooncensor.MelooonCensor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class CheckForUpdatesTask implements Runnable {

    public static final String API_URL = "https://api.github.com";
    public static final String API_ACCEPT = "application/vnd.github.v3+json";
    public static final String REPO = "jacobmarshall/meloooncensor";

    private MelooonCensor plugin;
    private Version version;
    private Client bugsnag;

    private boolean isOutdated;
    private Release latestRelease;

    public CheckForUpdatesTask (MelooonCensor plugin, Client bugsnag) {
        this.plugin = plugin;
        this.bugsnag = bugsnag;
        this.version = new Version(plugin.getDescription().getVersion());
    }

    @Override
    public void run () {
        boolean isOutdated = false;
        Release latestRelease = null;

        try {
            String releasesText = sendRequest("/repos/" + REPO + "/releases");
            if (releasesText != null) {
                JSONArray releases = new JSONArray(releasesText);

                if (releases.length() > 0) {
                    for (int index = 0; index < releases.length(); index++) {
                        Release release = Release.from(releases.getJSONObject(index));

                        if ( ! release.isPreRelease()) {
                            Version releaseVersion = new Version(release.getVersion());

                            if (releaseVersion.compareTo(version) > 0) {
                                isOutdated = true;
                                latestRelease = release;
                            }

                            break;
                        }
                    }
                }
            }
        } catch (Exception err) {
            bugsnag.notify(err);
        } finally {
            this.isOutdated = isOutdated;
            this.latestRelease = latestRelease;
        }
    }

    private String sendRequest (String api) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(API_URL + "/" + api);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", API_ACCEPT);

            int statusCode = connection.getResponseCode();

            if (statusCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = null;

                try {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    return response.toString();
                } catch (IOException err) {
                    bugsnag.notify(err);
                } finally {
                    if (reader != null) {
                        reader.close();
                    }
                }
            }
        } catch (Exception err) {
            bugsnag.notify(err);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    public boolean isUpdateAvailable () {
        return isOutdated;
    }

    public Release getLatestRelease () {
        return latestRelease;
    }

}
