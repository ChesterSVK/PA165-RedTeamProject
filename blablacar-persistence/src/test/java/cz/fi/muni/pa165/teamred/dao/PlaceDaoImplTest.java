package cz.fi.muni.pa165.teamred.dao;

import cz.fi.muni.pa165.teamred.PersistenceSampleApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import cz.fi.muni.pa165.teamred.entity.Place;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author miroslav.laco@gmail.com
 */
@Transactional
@ContextConfiguration(classes = PersistenceSampleApplicationContext.class)
@TestExecutionListeners(TransactionalTestExecutionListener.class)
public class PlaceDaoImplTest extends AbstractTestNGSpringContextTests {
    
    @Autowired
    private PlaceDao placeDao;

    @PersistenceContext
    private EntityManager em;
    
    private Place place1, place2;
    
    @BeforeMethod
    void init() {
        place1 = new Place();
        place1.setName("Brno");
        
        place2 = new Place();
        place2.setName("Praha");

        em.persist(place1);
        em.persist(place2);
        em.flush();
    }

    @Test
    public void createTest() {
        Place testPlace = new Place("Jicin");
        placeDao.create(testPlace);

        Place foundPlace = em.find(Place.class, testPlace.getId());
        assertThat(foundPlace).isNotNull().isEqualTo(testPlace);
    }

    @Test
    public void createNullPlaceTest() {
        assertThatThrownBy(() -> placeDao.create(null)).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void createNullPlaceNameTest() {
        Place testPlace = new Place();
        testPlace.setName(null);
        assertThatThrownBy(() -> placeDao.create(null))
                .hasRootCauseExactlyInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void createNotUniquePlaceNameTest() {
        Place testPlace = new Place(place1.getName());
        assertThatThrownBy(() -> placeDao.create(testPlace))
                .hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void createFindDeleteTest(){
        placeDao.create(place1);
        placeDao.create(place2);

        assertThat(placeDao.findById(place1.getId()).getName())
                .isEqualTo("Brno");
        assertThat(placeDao.findAll())
                .containsExactlyInAnyOrder(place1,place2);

        placeDao.delete(place2);

        assertThat(placeDao.findAll())
                .containsExactly(place1);
    }
    
    @Test
    public void createNullTest() {
        assertThatThrownBy(() -> placeDao.create(null)).isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void deleteNullTest() {
        assertThatThrownBy(() -> placeDao.delete(null)).isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void findByIdNullTest() {
        assertThatThrownBy(() -> placeDao.findById(null)).isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void findByIdNonExistingTest() {
        Place placeFound = placeDao.findById(-1L);
        assertThat(placeFound).isNull();
    }
}
