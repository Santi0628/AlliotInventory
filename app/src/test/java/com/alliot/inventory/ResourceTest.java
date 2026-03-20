package com.alliot.inventory;

import com.alliot.inventory.repository.ErrorType;
import com.alliot.inventory.repository.Resource;

import org.junit.Test;

import static org.junit.Assert.*;

public class ResourceTest {

    @Test
    public void loading_hasCorrectStatus() {
        Resource<String> resource = Resource.loading();
        assertEquals(Resource.Status.LOADING, resource.getStatus());
        assertNull(resource.getData());
        assertNull(resource.getErrorType());
    }

    @Test
    public void success_carriesData() {
        Resource<String> resource = Resource.success("test_data");
        assertEquals(Resource.Status.SUCCESS, resource.getStatus());
        assertEquals("test_data", resource.getData());
        assertNull(resource.getErrorType());
    }

    @Test
    public void error_carriesErrorType() {
        ErrorType errorType = ErrorType.fromHttpCode(401);
        Resource<String> resource = Resource.error(errorType);
        assertEquals(Resource.Status.ERROR, resource.getStatus());
        assertNull(resource.getData());
        assertEquals(ErrorType.UNAUTHORIZED, resource.getErrorType());
    }

    @Test
    public void success_withNullData_isValid() {
        Resource<Object> resource = Resource.success(null);
        assertEquals(Resource.Status.SUCCESS, resource.getStatus());
        assertNull(resource.getData());
    }
}
