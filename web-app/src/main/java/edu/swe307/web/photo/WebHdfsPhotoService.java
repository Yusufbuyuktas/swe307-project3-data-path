
package edu.swe307.web.photo;

import edu.swe307.common.PhotoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class WebHdfsPhotoService implements PhotoService {

    private final HttpClient httpClient;
    private final String webhdfsUrl;
    private final String photoDir;

    public WebHdfsPhotoService(
            @Value("${p3.hdfs.webhdfs-url}") String webhdfsUrl,
            @Value("${p3.hdfs.photo-dir}") String photoDir) {

        this.webhdfsUrl = removeTrailingSlash(webhdfsUrl);
        this.photoDir = "/" + removeSlashes(photoDir);

        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    @Override
    public byte[] getPhoto(String fileName) {
        validateFileName(fileName);

        URI uri = URI.create(
                webhdfsUrl + photoDir + "/" + fileName + "?op=OPEN"
        );

        HttpRequest request = HttpRequest.newBuilder(uri)
                .GET()
                .timeout(Duration.ofSeconds(15))
                .build();

        try {
            HttpResponse<byte[]> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofByteArray()
            );

            if (response.statusCode() == 404) {
                throw new PhotoNotFoundException(fileName);
            }

            if (response.statusCode() != 200) {
                throw new IllegalStateException(
                        "WebHDFS HTTP status: " + response.statusCode()
                );
            }

            return response.body();

        } catch (IOException e) {
            throw new IllegalStateException(
                    "WebHDFS connection failed", e
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    "WebHDFS request interrupted", e
            );
        }
    }

    private void validateFileName(String fileName) {
        if (fileName == null
                || !fileName.matches("[A-Za-z0-9_.-]+")
                || fileName.contains("..")
                || !fileName.endsWith(".jpg")) {

            throw new IllegalArgumentException(
                    "Invalid photo filename"
            );
        }
    }

    private static String removeTrailingSlash(String value) {
        return value.replaceAll("/+$", "");
    }

    private static String removeSlashes(String value) {
        return value.replaceAll("^/+|/+$", "");
    }

    public static class PhotoNotFoundException
            extends RuntimeException {

        public PhotoNotFoundException(String fileName) {
            super("Photo not found: " + fileName);
        }
    }
}
