package org.pasang.dbproxy;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.*;

@Path("/proxy")
public class SqlExecutorController {

    @Inject
    DatabaseService databaseService;

    @GET
    @Path("/{dbId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response proxyQuery(@PathParam("dbId") String dbId, @QueryParam("query") String query) {
        try (Connection conn = databaseService.getConnection(dbId);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                JsonObjectBuilder objBuilder = Json.createObjectBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    objBuilder.add(columnName, rs.getString(i));
                }
                arrayBuilder.add(objBuilder);
            }
            return Response.ok(arrayBuilder.build()).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Json.createObjectBuilder().add("error", e.getMessage()).build())
                    .build();
        }
    }

}
