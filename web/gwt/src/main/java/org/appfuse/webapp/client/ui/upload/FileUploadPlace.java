/**
 * 
 */
package org.appfuse.webapp.client.ui.upload;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * @author ivangsa
 *
 */
public class FileUploadPlace extends Place {

    @Prefix("upload")
    public static class Tokenizer implements PlaceTokenizer<FileUploadPlace> {
        @Override
        public String getToken(FileUploadPlace place) {
            return "";
        }

        @Override
        public FileUploadPlace getPlace(String token) {
            return new FileUploadPlace();
        }
    }

}
