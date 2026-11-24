
package edu.swe307.web.controller;

import edu.swe307.common.PhotoService;
import edu.swe307.web.photo.WebHdfsPhotoService.PhotoNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PhotoController.class)
class PhotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PhotoService photoService;

    @Test
    void existingPhotoReturnsJpeg() throws Exception {
        byte[] image = { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF };

        when(photoService.getPhoto("scott.jpg"))
                .thenReturn(image);

        mockMvc.perform(get("/photos/scott.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/jpeg"))
                .andExpect(content().bytes(image));

        verify(photoService).getPhoto("scott.jpg");
    }

    @Test
    void missingPhotoReturns404() throws Exception {
        when(photoService.getPhoto("missing.jpg"))
                .thenThrow(new PhotoNotFoundException("missing.jpg"));

        mockMvc.perform(get("/photos/missing.jpg"))
                .andExpect(status().isNotFound());
    }

    @Test
    void invalidExtensionReturns400() throws Exception {
        when(photoService.getPhoto("test.txt"))
                .thenThrow(
                    new IllegalArgumentException("Invalid photo filename")
                );

        mockMvc.perform(get("/photos/test.txt"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void hdfsFailureReturns502() throws Exception {
        when(photoService.getPhoto("scott.jpg"))
                .thenThrow(
                    new IllegalStateException("WebHDFS unavailable")
                );

        mockMvc.perform(get("/photos/scott.jpg"))
                .andExpect(status().isBadGateway());
    }
}
