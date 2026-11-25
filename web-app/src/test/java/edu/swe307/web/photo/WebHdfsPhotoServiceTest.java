
package edu.swe307.web.photo;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class WebHdfsPhotoServiceTest {

    private HttpServer server;
    private WebHdfsPhotoService photoService;

    @BeforeEach
    void setUp() throws IOException {
        server = HttpServer.create(
                new InetSocketAddress("127.0.0.1", 0), 0
        );

        server.start();

        String baseUrl = "http://127.0.0.1:"
                + server.getAddress().getPort();

        photoService = new WebHdfsPhotoService(
                baseUrl + "/webhdfs/v1",
                "/photos"
        );
    }

    @AfterEach
    void tearDown() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    void rejectsInvalidFilenames() {
        String[] invalidNames = {
                "../scott.jpg",
                "folder/scott.jpg",
                "test.txt",
                "scott.png",
                "",
                "scott..jpg",
                "scott.jpg?op=DELETE"
        };

        for (String fileName : invalidNames) {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> photoService.getPhoto(fileName),
                    "Should reject: " + fileName
            );
        }

        assertThrows(
                IllegalArgumentException.class,
                () -> photoService.getPhoto(null)
        );
    }

    @Test
    void readsExistingPhoto() {
        byte[] expected = {
                (byte) 0xFF,
                (byte) 0xD8,
                (byte) 0xFF
        };

        server.createContext(
                "/webhdfs/v1/photos/scott.jpg",
                exchange -> {
                    assertEquals(
                            "op=OPEN",
                            exchange.getRequestURI().getQuery()
                    );

                    exchange.sendResponseHeaders(
                            200, expected.length
                    );

                    try (var output = exchange.getResponseBody()) {
                        output.write(expected);
                    }
                }
        );

        byte[] actual = photoService.getPhoto("scott.jpg");

        assertArrayEquals(expected, actual);
    }

    @Test
    void followsWebHdfsRedirect() {
        byte[] expected = "JPEG-DATA"
                .getBytes(StandardCharsets.UTF_8);

        server.createContext(
                "/webhdfs/v1/photos/scott.jpg",
                exchange -> {
                    String redirectUrl =
                            "http://127.0.0.1:"
                            + server.getAddress().getPort()
                            + "/datanode/scott.jpg";

                    exchange.getResponseHeaders().add(
                            "Location", redirectUrl
                    );

                    exchange.sendResponseHeaders(307, -1);
                    exchange.close();
                }
        );

        server.createContext(
                "/datanode/scott.jpg",
                exchange -> {
                    exchange.sendResponseHeaders(
                            200, expected.length
                    );

                    try (var output = exchange.getResponseBody()) {
                        output.write(expected);
                    }
                }
        );

        assertArrayEquals(
                expected,
                photoService.getPhoto("scott.jpg")
        );
    }

    @Test
    void missingPhotoThrowsNotFoundException() {
        server.createContext(
                "/webhdfs/v1/photos/missing.jpg",
                exchange -> {
                    exchange.sendResponseHeaders(404, -1);
                    exchange.close();
                }
        );

        assertThrows(
                WebHdfsPhotoService.PhotoNotFoundException.class,
                () -> photoService.getPhoto("missing.jpg")
        );
    }

    @Test
    void serverFailureThrowsIllegalStateException() {
        server.createContext(
                "/webhdfs/v1/photos/scott.jpg",
                exchange -> {
                    exchange.sendResponseHeaders(503, -1);
                    exchange.close();
                }
        );

        assertThrows(
                IllegalStateException.class,
                () -> photoService.getPhoto("scott.jpg")
        );
    }
}
