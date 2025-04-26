package ServicesTests;

import dto.CatDto;
import entities.Cat;
import entities.Owner;
import enums.CatColor;
import exceptions.UnknownEntityIdException;
import interfaces.CatDao;
import interfaces.OwnerDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import services.CatService;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CatServiceTest {

    @Mock
    private CatDao catDao;

    @Mock
    private OwnerDao ownerDao;

    @InjectMocks
    private CatService catService;

    private Cat testCat;
    private CatDto testCatDto;
    private Owner testOwner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testOwner = new Owner();
        testOwner.setId(1L);
        testOwner.setFirstName("TestName");
        testOwner.setLastName("TestSurname");
        testOwner.setBirthdate(LocalDate.of(1990, 5, 15));
        testOwner.setCats(new ArrayList<>());

        testCat = new Cat();
        testCat.setId(1L);
        testCat.setName("TestCatName");
        testCat.setBirthdate(LocalDate.of(2020, 3, 10));
        testCat.setBreed("TestBreed");
        testCat.setColor(CatColor.BLACK);
        testCat.setOwner(testOwner);
        testCat.setFriends(new HashSet<>());

        testCatDto = new CatDto();
        testCatDto.setId(1L);
        testCatDto.setName("TestCatName");
        testCatDto.setBirthdate(LocalDate.of(2020, 3, 10));
        testCatDto.setBreed("TestBreed");
        testCatDto.setColor(CatColor.BLACK);
        testCatDto.setOwner(1L);
        testCatDto.setFriends(new ArrayList<>());
    }

    @Test
    void createCat_WithValidData_ShouldReturnCreatedCat() {
        when(ownerDao.getById(1L)).thenReturn(testOwner);
        when(catDao.save(any(Cat.class))).thenReturn(testCat);

        Cat result = catService.createCat(testCatDto);

        assertNotNull(result);
        assertEquals(testCat.getId(), result.getId());
        assertEquals(testCat.getName(), result.getName());
        assertEquals(testCat.getBirthdate(), result.getBirthdate());
        assertEquals(testCat.getBreed(), result.getBreed());
        assertEquals(testCat.getColor(), result.getColor());
        assertEquals(testCat.getOwner(), result.getOwner());

        verify(ownerDao, times(1)).getById(anyLong());
        verify(catDao, times(1)).save(any(Cat.class));
    }

    @Test
    void createCat_WithInvalidOwnerId_ShouldThrowException() {
        when(ownerDao.getById(anyLong())).thenReturn(null);

        assertThrows(UnknownEntityIdException.class, () -> catService.createCat(testCatDto));
        verify(ownerDao, times(1)).getById(anyLong());
        verify(catDao, never()).save(any(Cat.class));
    }

    @Test
    void updateCat_WithNonExistentCat_ShouldThrowException() {
        when(catDao.getById(anyLong())).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> catService.updateCat(testCatDto));
        assertEquals("Cat not found with id: " + testCatDto.getId(), exception.getMessage());

        verify(catDao, times(1)).getById(anyLong());
        verify(catDao, never()).save(any(Cat.class));
    }

    @Test
    void updateCat_WithInvalidOwnerId_ShouldThrowException() {
        when(catDao.getById(anyLong())).thenReturn(testCat);
        when(ownerDao.getById(anyLong())).thenReturn(null);

        assertThrows(UnknownEntityIdException.class, () -> catService.updateCat(testCatDto));

        verify(catDao, times(1)).getById(anyLong());
        verify(ownerDao, times(1)).getById(anyLong());
        verify(catDao, never()).save(any(Cat.class));
    }

    @Test
    void updateCat_WithFriends_ShouldAddFriends() {
        Cat friendCat = new Cat();
        friendCat.setId(2L);
        friendCat.setName("TestCatName");

        testCatDto.setFriends(List.of(2L));

        when(catDao.getById(1L)).thenReturn(testCat);
        when(catDao.getById(2L)).thenReturn(friendCat);
        when(ownerDao.getById(1L)).thenReturn(testOwner);
        when(catDao.save(any(Cat.class))).thenReturn(testCat);

        Cat result = catService.updateCat(testCatDto);

        assertNotNull(result);
        verify(catDao, times(1)).getById(1L);
        verify(catDao, times(1)).getById(2L);
        verify(catDao, times(1)).save(any(Cat.class));
    }

    @Test
    void deleteCat_ShouldCallDaoMethod() {
        catService.deleteCat(1L);

        verify(catDao, times(1)).deleteById(1L);
    }

    @Test
    void getCat_ShouldReturnCat() {
        when(catDao.getById(1L)).thenReturn(testCat);

        Cat result = catService.getCat(1L);

        assertNotNull(result);
        assertEquals(testCat, result);
        verify(catDao, times(1)).getById(1L);
    }
}