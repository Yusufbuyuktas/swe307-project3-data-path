
package edu.swe307.web.controller;

import edu.swe307.common.PhotoService;
import edu.swe307.web.photo.WebHdfsPhotoService.PhotoNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PhotoController {

    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/photos/{filename:.+}")
    public ResponseEntity<byte[]> getPhoto(
            @PathVariable String filename) {

        byte[] photo = photoService.getPhoto(filename);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(photo);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleInvalidFilename(
            IllegalArgumentException exception) {

        return ResponseEntity
                .badRequest()
                .body("Invalid photo filename");
    }

    @ExceptionHandler(PhotoNotFoundException.class)
    public ResponseEntity<String> handlePhotoNotFound(
            PhotoNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Photo not found");
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleHdfsFailure(
            IllegalStateException exception) {

        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body("Photo storage unavailable");
    }
}
