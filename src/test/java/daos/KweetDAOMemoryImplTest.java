package daos;

import database.memory.InMemoryDatabase;
import dtos.KweetDTO;
import models.Kweet;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class KweetDAOMemoryImplTest extends KweetDAOTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(Kweet.class)
                .addClass(IKweetDAO.class)
                .addClass(KweetDAOMemoryImpl.class)
                .addClass(KweetDTO.class)
                .addClass(KweetDAOTest.class)
                .addClass(InMemoryDatabase.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @After
    public void cleanUp(){
        InMemoryDatabase.getInMemoryDatabase().cleanUp();
    }
}
