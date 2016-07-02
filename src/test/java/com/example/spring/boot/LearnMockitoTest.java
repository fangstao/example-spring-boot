package com.example.spring.boot;

import org.junit.Test;
import org.mockito.Mockito;
import java.util.List;

import static org.mockito.Mockito.*;
/**
 * Created by pc on 2016/6/29.
 */
public class LearnMockitoTest {

    @Test
    public void verify() throws Exception {
        List mockList = mock(List.class);
        when(mockList.add("one")).thenReturn(true);
       // mockList.add("one");

    }
}
