package com.alliot.inventory;

import com.alliot.inventory.model.DefaultPicture;
import com.alliot.inventory.model.ImageUrl;
import com.alliot.inventory.model.PictureFile;

import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultPictureTest {

    @Test
    public void defaultConstructor_fileIsNull() {
        DefaultPicture dp = new DefaultPicture();
        assertNull(dp.getFile());
    }

    @Test
    public void setFile_thenGetFile_returnsSameInstance() {
        DefaultPicture dp = new DefaultPicture();
        PictureFile file = new PictureFile();
        dp.setFile(file);
        assertSame(file, dp.getFile());
    }

    @Test
    public void setFile_toNull_returnsNull() {
        DefaultPicture dp = new DefaultPicture();
        PictureFile file = new PictureFile();
        dp.setFile(file);
        dp.setFile(null);
        assertNull(dp.getFile());
    }

    @Test
    public void setFile_withPopulatedPictureFile_accessibleViaGetter() {
        DefaultPicture dp = new DefaultPicture();
        PictureFile file = new PictureFile();
        ImageUrl medium = new ImageUrl();
        medium.setUrl("https://cdn.example.com/medium.jpg");
        file.setMedium(medium);
        dp.setFile(file);

        assertNotNull(dp.getFile());
        assertEquals("https://cdn.example.com/medium.jpg",
                dp.getFile().getMedium().getUrl());
    }
}

