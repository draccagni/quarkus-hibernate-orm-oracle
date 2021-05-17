package org.acme.hibernate.orm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("test")
public class TestResource {

    private static final Logger LOGGER = Logger.getLogger(TestResource.class.getName());

    @Inject
    EntityManager entityManager;

    @Inject
    @ConfigProperty(name = "quarkus.hibernate-orm.database.default-schema")
    String schema;

    @GET
    @Path("{test}")
    public List<Test> get(@PathParam String test) throws SQLException {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery(schema + ".dummy_proc")
                .registerStoredProcedureParameter("p_test", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_cur", Object.class, ParameterMode.REF_CURSOR)
                .setParameter("p_test", test)
        ;

        List<Test> result = new ArrayList<>();
        ResultSet resultSet = (ResultSet) query.getOutputParameterValue("p_cur");
        while (resultSet.next()) {
            result.add(new Test(resultSet.getString(1)));
        }
        return result;
    }
}
