package com.alliot.inventory;

import com.alliot.inventory.model.ImageUrl;
import com.alliot.inventory.model.PictureFile;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PictureFileTest {

    private PictureFile file;

    @Before
    public void setUp() {
        file = new PictureFile();
    }

    @Test
    public void getBestUrl_prefersLarge_overMediumAndIcon() {
        file.setLarge(imageUrl("large.jpg"));
        file.setMedium(imageUrl("medium.jpg"));
        file.setIcon(imageUrl("icon.jpg"));
        file.setUrl("base.jpg");

        assertEquals("large.jpg", file.getBestUrl());
    }

    @Test
    public void getBestUrl_fallsToMedium_whenNoLarge() {
        file.setMedium(imageUrl("medium.jpg"));
        file.setIcon(imageUrl("icon.jpg"));
        file.setUrl("base.jpg");

        assertEquals("medium.jpg", file.getBestUrl());
    }

    @Test
    public void getBestUrl_fallsToIcon_whenNoLargeOrMedium() {
        file.setIcon(imageUrl("icon.jpg"));
        file.setUrl("base.jpg");

        assertEquals("icon.jpg", file.getBestUrl());
    }

    @Test
    public void getBestUrl_fallsToBaseUrl_whenNoVariants() {
        file.setUrl("base.jpg");
        assertEquals("base.jpg", file.getBestUrl());
    }

    @Test
    public void getBestUrl_returnsNull_whenEverythingIsNull() {
        assertNull(file.getBestUrl());
    }

    @Test
    public void getBestUrl_skipsLarge_whenLargeUrlIsNull() {
        ImageUrl largeWithNullUrl = new ImageUrl();
        file.setLarge(largeWithNullUrl);
        file.setMedium(imageUrl("medium.jpg"));

        assertEquals("medium.jpg", file.getBestUrl());
    }

    @Test
    public void getBestUrl_skipsLargeAndMedium_whenBothUrlsNull() {
        file.setLarge(new ImageUrl());
        file.setMedium(new ImageUrl());
        file.setIcon(imageUrl("icon.jpg"));

        assertEquals("icon.jpg", file.getBestUrl());
    }

    @Test
    public void getMediumUrl_prefersMedium() {
        file.setMedium(imageUrl("medium.jpg"));
        file.setLarge(imageUrl("large.jpg"));
        file.setIcon(imageUrl("icon.jpg"));
        file.setUrl("base.jpg");

        assertEquals("medium.jpg", file.getMediumUrl());
    }

    @Test
    public void getMediumUrl_fallsToLarge_whenNoMedium() {
        file.setLarge(imageUrl("large.jpg"));
        file.setIcon(imageUrl("icon.jpg"));
        file.setUrl("base.jpg");

        assertEquals("large.jpg", file.getMediumUrl());
    }

    @Test
    public void getMediumUrl_fallsToIcon_whenNoMediumOrLarge() {
        file.setIcon(imageUrl("icon.jpg"));
        file.setUrl("base.jpg");

        assertEquals("icon.jpg", file.getMediumUrl());
    }

    @Test
    public void getMediumUrl_fallsToBaseUrl_whenNoVariants() {
        file.setUrl("base.jpg");
        assertEquals("base.jpg", file.getMediumUrl());
    }

    @Test
    public void getMediumUrl_returnsNull_whenAllNull() {
        assertNull(file.getMediumUrl());
    }

    @Test
    public void getMediumUrl_skipsMedium_whenMediumUrlIsNull() {
        file.setMedium(new ImageUrl());
        file.setLarge(imageUrl("large.jpg"));

        assertEquals("large.jpg", file.getMediumUrl());
    }

    @Test
    public void getLargeUrl_prefersLarge() {
        file.setLarge(imageUrl("large.jpg"));
        file.setMedium(imageUrl("medium.jpg"));
        file.setUrl("base.jpg");

        assertEquals("large.jpg", file.getLargeUrl());
    }

    @Test
    public void getLargeUrl_fallsToMedium_whenNoLarge() {
        file.setMedium(imageUrl("medium.jpg"));
        file.setUrl("base.jpg");

        assertEquals("medium.jpg", file.getLargeUrl());
    }

    @Test
    public void getLargeUrl_fallsToBaseUrl_whenNoLargeOrMedium() {
        file.setUrl("base.jpg");
        assertEquals("base.jpg", file.getLargeUrl());
    }

    @Test
    public void getLargeUrl_returnsNull_whenAllNull() {
        assertNull(file.getLargeUrl());
    }

    @Test
    public void getLargeUrl_skipsLarge_whenLargeUrlIsNull() {
        file.setLarge(new ImageUrl());
        file.setMedium(imageUrl("medium.jpg"));

        assertEquals("medium.jpg", file.getLargeUrl());
    }

    @Test
    public void gettersAndSetters_work() {
        ImageUrl icon = imageUrl("icon.jpg");
        ImageUrl medium = imageUrl("medium.jpg");
        ImageUrl large = imageUrl("large.jpg");

        file.setUrl("base.jpg");
        file.setIcon(icon);
        file.setMedium(medium);
        file.setLarge(large);

        assertEquals("base.jpg", file.getUrl());
        assertSame(icon, file.getIcon());
        assertSame(medium, file.getMedium());
        assertSame(large, file.getLarge());
    }

    private static ImageUrl imageUrl(String url) {
        ImageUrl img = new ImageUrl();
        img.setUrl(url);
        return img;
    }
}
