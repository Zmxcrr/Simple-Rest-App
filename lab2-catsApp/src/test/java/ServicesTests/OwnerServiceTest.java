package ServicesTests;

import dto.OwnerDto;
import entities.Owner;
import exceptions.UnknownEntityIdException;
import interfaces.OwnerDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import services.OwnerService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class OwnerServiceTest {

    @Mock
    private OwnerDao ownerDao;

    @InjectMocks
    private OwnerService ownerService;

    private Owner testOwner;
    private OwnerDto testOwnerDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testOwner = new Owner();
        testOwner.setId(1L);
        testOwner.setFirstName("TestName");
        testOwner.setLastName("TestLastName");
        testOwner.setBirthdate(LocalDate.of(1990, 5, 15));
        testOwner.setCats(new ArrayList<>());

        testOwnerDto = new OwnerDto();
        testOwnerDto.setId(1L);
        testOwnerDto.setFirstName("TestName");
        testOwnerDto.setLastName("TestLastName");
        testOwnerDto.setBirthdate(LocalDate.of(1990, 5, 15));
        testOwnerDto.setCatIds(new ArrayList<>());
    }

    @Test
    void createOwner_ShouldReturnCreatedOwner() {
        when(ownerDao.save(any(Owner.class))).thenReturn(testOwner);

        Owner result = ownerService.createOwner(testOwnerDto);

        assertNotNull(result);
        assertEquals(testOwner.getId(), result.getId());
        assertEquals(testOwner.getFirstName(), result.getFirstName());
        assertEquals(testOwner.getLastName(), result.getLastName());
        assertEquals(testOwner.getBirthdate(), result.getBirthdate());

        verify(ownerDao, times(1)).save(any(Owner.class));
    }

    @Test
    void updateOwner_WhenOwnerExists_ShouldReturnUpdatedOwner() {
        when(ownerDao.getById(anyLong())).thenReturn(testOwner);
        when(ownerDao.update(any(Owner.class))).thenReturn(testOwner);

        Owner result = ownerService.updateOwner(testOwnerDto);

        assertNotNull(result);
        assertEquals(testOwner.getId(), result.getId());
        assertEquals(testOwnerDto.getFirstName(), result.getFirstName());
        assertEquals(testOwnerDto.getLastName(), result.getLastName());
        assertEquals(testOwnerDto.getBirthdate(), result.getBirthdate());

        verify(ownerDao, times(1)).getById(anyLong());
        verify(ownerDao, times(1)).update(any(Owner.class));
    }

    @Test
    void updateOwner_WhenOwnerDoesNotExist_ShouldThrowException() {
        when(ownerDao.getById(anyLong())).thenReturn(null);

        assertThrows(UnknownEntityIdException.class, () -> ownerService.updateOwner(testOwnerDto));
        verify(ownerDao, times(1)).getById(anyLong());
        verify(ownerDao, never()).update(any(Owner.class));
    }

    @Test
    void deleteOwner_ShouldCallDaoMethod() {
        ownerService.deleteOwner(1L);

        verify(ownerDao, times(1)).deleteById(1L);
    }

    @Test
    void getOwner_ShouldReturnOwner() {
        when(ownerDao.getById(1L)).thenReturn(testOwner);

        Owner result = ownerService.getOwner(1L);

        assertNotNull(result);
        assertEquals(testOwner, result);
        verify(ownerDao, times(1)).getById(1L);
    }

    @Test
    void getAllOwners_ShouldReturnAllOwners() {
        Owner secondOwner = new Owner();
        secondOwner.setId(2L);
        secondOwner.setFirstName("TestName");
        secondOwner.setLastName("TestLastName");

        List<Owner> ownerList = Arrays.asList(testOwner, secondOwner);
        when(ownerDao.getAll()).thenReturn(ownerList);

        List<Owner> result = ownerService.getAllOwners();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(ownerList, result);
        verify(ownerDao, times(1)).getAll();
    }
}
