package cz.fsvoboda.moviedb.api;

import java.util.List;

/**
 * @author Filip Svoboda
 */

public class ApiConfig {
    private ImagesConfig images = null;
    private List<String> change_keys;

    public ImagesConfig getImages() {
        return images;
    }

    public void setImages(ImagesConfig images) {
        this.images = images;
    }

    public List<String> getChange_keys() {
        return change_keys;
    }

    public void setChange_keys(List<String> change_keys) {
        this.change_keys = change_keys;
    }
}
