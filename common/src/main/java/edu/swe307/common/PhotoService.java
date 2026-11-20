package edu.swe307.common;

/**
 * Contract for retrieving employee portrait images from HDFS (Owner: B2).
 */
public interface PhotoService {

    /**
     * Reads raw image bytes for the given file name under {@link ProjectConstants#HDFS_PHOTOS_DIR}.
     *
     * @param fileName image file name (e.g., "scott.jpg")
     * @return image byte array, or empty/fallback bytes if not found
     */
    byte[] getPhoto(String fileName);
}
