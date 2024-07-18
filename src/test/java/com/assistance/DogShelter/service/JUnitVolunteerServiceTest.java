package com.assistance.DogShelter.service;

import com.assistance.DogShelter.db.model.Volunteer;
import com.assistance.DogShelter.db.repository.VolunteerRepository;
import com.assistance.DogShelter.exceptions.VolunteerNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class JUnitVolunteerServiceTest {

    @Mock
    private VolunteerRepository volunteerRepository;

    @InjectMocks
    private VolunteerService volunteerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddVolunteer() {
        Volunteer volunteer = new Volunteer();
        when(volunteerRepository.save(volunteer)).thenReturn(volunteer);

        Volunteer result = volunteerService.addVolunteer(volunteer);

        Assertions.assertNotNull(result);
        verify(volunteerRepository, times(1)).save(volunteer);
    }

    @Test
    void testFindVolunteerById() {
        Volunteer volunteer = new Volunteer();
        when(volunteerRepository.findById(1L)).thenReturn(Optional.of(volunteer));

        Optional<Volunteer> result = volunteerService.findVolunteerById(1L);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(volunteer, result.get());
        verify(volunteerRepository, times(1)).findById(1L);
    }

    @Test
    void testFindVolunteerByChatId() {
        Volunteer volunteer = new Volunteer();
        when(volunteerRepository.findByChatId(123L)).thenReturn(Optional.of(volunteer));

        Optional<Volunteer> result = volunteerService.findVolunteerByChatId(123L);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(volunteer, result.get());
        verify(volunteerRepository, times(1)).findByChatId(123L);
    }

    @Test
    void testFindFreeVolunteer() {
        Volunteer volunteer = new Volunteer();
        when(volunteerRepository.findFirstByIsBusy(false)).thenReturn(Optional.of(volunteer));

        Optional<Volunteer> result = volunteerService.findFreeVolunteer();

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(volunteer, result.get());
        verify(volunteerRepository, times(1)).findFirstByIsBusy(false);
    }

    @Test
    void testKeepVolunteerBusy() {
        Volunteer volunteer = new Volunteer();
        volunteer.setChatId(123L);
        when(volunteerRepository.findByChatId(123L)).thenReturn(Optional.of(volunteer));
        when(volunteerRepository.save(volunteer)).thenReturn(volunteer);

        Volunteer result = volunteerService.keepVolunteerBusy(123L, true);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isBusy());
        verify(volunteerRepository, times(1)).findByChatId(123L);
        verify(volunteerRepository, times(1)).save(volunteer);
    }

    @Test
    void testKeepVolunteerBusyNotFound() {
        when(volunteerRepository.findByChatId(123L)).thenReturn(Optional.empty());

        Assertions.assertThrows(VolunteerNotFoundException.class, () -> {
            volunteerService.keepVolunteerBusy(123L, true);
        });

        verify(volunteerRepository, times(1)).findByChatId(123L);
        verify(volunteerRepository, times(0)).save(any(Volunteer.class));
    }

    @Test
    void testEditVolunteer() {
        Volunteer volunteer = new Volunteer();
        when(volunteerRepository.save(volunteer)).thenReturn(volunteer);

        Volunteer result = volunteerService.editVolunteer(volunteer);

        Assertions.assertNotNull(result);
        verify(volunteerRepository, times(1)).save(volunteer);
    }

    @Test
    void testDeleteVolunteer() {
        doNothing().when(volunteerRepository).deleteById(1L);

        volunteerService.deleteVolunteer(1L);

        verify(volunteerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteVolunteerChatId() {
        Volunteer volunteer = new Volunteer();
        volunteer.setId(1L);
        when(volunteerRepository.findByChatId(123L)).thenReturn(Optional.of(volunteer));
        doNothing().when(volunteerRepository).deleteById(1L);

        volunteerService.deleteVolunteerChatId(123L);

        verify(volunteerRepository, times(1)).findByChatId(123L);
        verify(volunteerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAllVolunteers() {
        Volunteer volunteer1 = new Volunteer();
        Volunteer volunteer2 = new Volunteer();
        when(volunteerRepository.findAll()).thenReturn(Arrays.asList(volunteer1, volunteer2));

        Collection<Volunteer> result = volunteerService.getAllVolunteers();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        verify(volunteerRepository, times(1)).findAll();
    }
}
