package com.alliot.inventory;

import com.alliot.inventory.model.ImageUrl;

import org.junit.Test;

import static org.junit.Assert.*;

public class ImageUrlTest {

    @Test
    public void defaultConstructor_urlIsNull() {
        ImageUrl img = new ImageUrl();
        assertNull(img.getUrl());
    }

    @Test
    public void setUrl_thenGetUrl_returnsValue() {
        ImageUrl img = new ImageUrl();
        img.setUrl("https://cdn.example.com/photo.jpg");
        assertEquals("https://cdn.example.com/photo.jpg", img.getUrl());
    }

    @Test
    public void setUrl_toNull_returnsNull() {
        ImageUrl img = new ImageUrl();
        img.setUrl("something");
        img.setUrl(null);
        assertNull(img.getUrl());
    }

    @Test
    public void setUrl_emptyString_returnsEmpty() {
        ImageUrl img = new ImageUrl();
        img.setUrl("");
        assertEquals("", img.getUrl());
    }
}

