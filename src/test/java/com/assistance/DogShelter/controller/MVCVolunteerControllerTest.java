package com.assistance.DogShelter.controller;
import com.assistance.DogShelter.db.model.Volunteer;
import com.assistance.DogShelter.service.VolunteerService;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VolunteerController.class)
class MVCVolunteerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VolunteerService volunteerService;

    @Test
    public void addVolunteerTest() throws Exception {
        Long id = 1L;
        Long chatId = 123456789L;
        String name = "Анна Ананьева";

        JSONObject volunteerObject = new JSONObject();
        volunteerObject.put("chatId", chatId);
        volunteerObject.put("name", name);
        volunteerObject.put("isBusy", false);

        Volunteer newVolunteer = new Volunteer(id, chatId, name, false);

        when(volunteerService.addVolunteer(any(Volunteer.class))).thenReturn(newVolunteer);

        mockMvc.perform(post("/volunteers/add")
                        .content(volunteerObject.toString())
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.intValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.chatId").value(chatId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isBusy").value(false));
    }

    @Test
    public void findVolunteerByIdTest() throws Exception {
        Long id = 1L;
        Long chatId = 123456789L;
        String name = "Анна Ананьева";

        Volunteer expectedVolunteer = new Volunteer(id, chatId, name, false);

        when(volunteerService.findVolunteerById(id)).thenReturn(Optional.of(expectedVolunteer));

        mockMvc.perform(get("/volunteers/{id}", id)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.intValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.chatId").value(chatId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isBusy").value(false));
    }

    @Test
    public void editVolunteerTest() throws Exception {
        Long id = 1L;
        Long chatId = 123456789L;
        String name = "Анна Ананьева";

        JSONObject volunteerObject = new JSONObject();
        volunteerObject.put("chatId", chatId);
        volunteerObject.put("name", name);
        volunteerObject.put("isBusy", false);

        Volunteer updatedVolunteer = new Volunteer(id, chatId, name, false);

        when(volunteerService.findVolunteerById(id)).thenReturn(Optional.of(updatedVolunteer));
        when(volunteerService.editVolunteer(any(Volunteer.class))).thenReturn(updatedVolunteer);

        mockMvc.perform(put("/volunteers/{id}", id)
                        .content(volunteerObject.toString())
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.intValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.chatId").value(chatId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isBusy").value(false));
    }

    @Test
    public void deleteVolunteerTest() throws Exception {
        Long id = 1L;

        when(volunteerService.findVolunteerById(id)).thenReturn(Optional.of(new Volunteer()));
        doNothing().when(volunteerService).deleteVolunteer(id);

        mockMvc.perform(delete("/volunteers/{id}", id)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteVolunteerByChatIdTest() throws Exception {
        Long chatId = 123456789L;

        when(volunteerService.findVolunteerByChatId(chatId)).thenReturn(Optional.of(new Volunteer()));
        doNothing().when(volunteerService).deleteVolunteerChatId(chatId);

        mockMvc.perform(delete("/volunteers/chatId/{chatId}", chatId)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void setVolunteerBusyTest() throws Exception {
        Long id = 1L;
        boolean busy = true;

        Volunteer volunteer = new Volunteer(id, 123456789L, "Анна Ананьева", !busy);

        when(volunteerService.findVolunteerById(id)).thenReturn(Optional.of(volunteer));
        when(volunteerService.keepVolunteerBusy(id, busy)).thenReturn(new Volunteer(id, 123456789L, "Анна Ананьева", busy));

        mockMvc.perform(post("/volunteers/{userId}", id)
                        .param("busy", String.valueOf(busy))
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.intValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isBusy").value(busy));
    }

    @Test
    public void getAllVolunteersTest() throws Exception {
        List<Volunteer> volunteers = Arrays.asList(
                new Volunteer(1L, 123456789L, "Анна Ананьева", false),
                new Volunteer(2L, 987654321L, "Петр Петров", false)
        );

        when(volunteerService.getAllVolunteers()).thenReturn(volunteers);

        mockMvc.perform(get("/volunteers/all")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2))); // Проверяем, что вернулась коллекция из двух волонтеров
    }
}