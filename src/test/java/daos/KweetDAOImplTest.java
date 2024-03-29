package daos;

import dtos.KweetDTO;
import models.Kweet;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class KweetDAOImplTest extends KweetDAOTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(Kweet.class)
                .addClass(KweetDTO.class)
                .addClass(IKweetDAO.class)
                .addClass(KweetDAOImpl.class)
                .addClass(KweetDAOTest.class)
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
}
