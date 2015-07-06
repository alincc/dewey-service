package no.nb.microservice.dewey.rest.controller;

import no.nb.microservice.dewey.Application;
import no.nb.microservice.dewey.rest.model.DeweyRoot;
import no.nb.microservice.dewey.rest.model.Discipline;
import no.nb.microservice.dewey.rest.model.Record;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class DeweyControllerTest {

    private MockMvc mockMvc;
    private DeweyController deweyController;

    @Before
    public void setup() {
        deweyController = new DeweyController();
        mockMvc = MockMvcBuilders.standaloneSetup(deweyController).build();
    }

//    @Test
//    public void dewey() throws Exception{
//        mockMvc.perform(get("/?class=001&language=nob")).andExpect(status().isOk());
//    }

    @Test
    public void deweyValues() {

    }
}