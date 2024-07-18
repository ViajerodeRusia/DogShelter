package com.assistance.DogShelter.controller;

import com.assistance.DogShelter.controller.dto.PetDto;
import com.assistance.DogShelter.controller.dto.ShelterDto;
import com.assistance.DogShelter.db.model.User;
import com.assistance.DogShelter.db.repository.PetRepository;
import com.assistance.DogShelter.db.repository.ShelterRepository;
import com.assistance.DogShelter.mapper.PetMapper;
import com.assistance.DogShelter.mapper.ShelterMapper;
import com.assistance.DogShelter.service.PetService;
import com.assistance.DogShelter.service.ShelterService;
import com.assistance.DogShelter.service.UserService;
import jakarta.ws.rs.core.MediaType;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ShelterController.class)
class MVCShelterControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ShelterRepository shelterRepository;
    @MockBean
    private ShelterMapper shelterMapper;
    @MockBean
    private ShelterService shelterService;
    @Autowired
    private ShelterController shelterController;

    @Test
    public void getPetByIdTest() throws Exception {
        Long id = 1L;
        String name = "Приют1";
        String address = "г. Москва, ул. Пушкина, д.10";

        ShelterDto expectedShelter = new ShelterDto();
        expectedShelter.setId(id);
        expectedShelter.setName(name);
        expectedShelter.setAddress(address);


        when(shelterService.findShelterById(id)).thenReturn(Optional.of(expectedShelter));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelters/{id}", id)
                        .contentType(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
                        .accept(jakarta.ws.rs.core.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value(address));
    }

    @Test
    void getAllSheltersTest() throws Exception {
        List<ShelterDto> shelters = Arrays.asList(
                new ShelterDto(1L, "Приют1", "г. Москва, ул. Пушкина, д.10", null),
                new ShelterDto(2L, "Приют2", "г. Санкт-Петербург, ул. Ленина, д.15", null)
        );
        when(shelterService.getAllShelters()).thenReturn(shelters);

        mockMvc.perform(get("/shelters/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
    }

    @Test
    public void createShelterTest() throws Exception {
        Long id = 1L;
        String name = "Приют1";
        String address = "г. Москва, ул. Пушкина, д.10";

        JSONObject shelterObject = new JSONObject();
        shelterObject.put("name", name);
        shelterObject.put("address", address);

        ShelterDto newShelter = new ShelterDto();
        newShelter.setId(id);
        newShelter.setName(name);
        newShelter.setAddress(address);

        when(shelterService.addShelter(any(ShelterDto.class))).thenReturn(newShelter);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/shelters/add")
                        .content(shelterObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.intValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value(address));
    }

    @Test
    public void deleteShelterTest() throws Exception {
        Long id = 1L;

        doNothing().when(shelterService).deleteShelter(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/shelters/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void editShelterTest() throws Exception {
        Long id = 1L;
        String name = "Приют1";
        String address = "г. Москва, ул. Пушкина, д.10";

        JSONObject shelterObject = new JSONObject();
        shelterObject.put("name", name);
        shelterObject.put("address", address);

        ShelterDto updatedShelter = new ShelterDto();
        updatedShelter.setId(id);
        updatedShelter.setName(name);
        updatedShelter.setAddress(address);

        when(shelterService.findShelterById(id)).thenReturn(Optional.of(updatedShelter));
        when(shelterService.editShelter(any(ShelterDto.class))).thenReturn(updatedShelter);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/shelters/{id}", id)
                        .content(shelterObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.intValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value(address));
    }
}