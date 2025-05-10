package ServicesTests;

import Zmxcrr.dto.OwnerDto;
import Zmxcrr.repositories.OwnerRepository;
import Zmxcrr.services.OwnerServiceImpl;
import Zmxcrr.entities.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OwnerServiceTests {
    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private OwnerServiceImpl ownerService;

    private Owner testOwner;
    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        testOwner = new Owner();
        testOwner.setId(1L);
        testOwner.setLastName("TestLastName");
        testOwner.setFirstName("TestFirstName");
        testOwner.setCats(new ArrayList<>());

        Mockito.when(ownerRepository.findById(1L)).thenReturn(Optional.ofNullable(testOwner));
    }

    @Test
    public void getById_WhenPresent_ShouldReturnDto() {
        var expectedDto = new OwnerDto(testOwner);

        var actualOptional = ownerService.getById(1L);
        assertTrue(actualOptional.isPresent());

        var actualDto = actualOptional.get();

        assertEquals(expectedDto, actualDto);
    }
}