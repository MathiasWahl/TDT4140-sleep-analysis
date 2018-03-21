package tdt4140.gr1816.app.api;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import org.junit.Before;
import tdt4140.gr1816.app.api.auth.AuthContext;
import tdt4140.gr1816.app.api.types.User;

public class ApiBaseCase {
  @Before
  public void executedBeforeEach() {
    GraphQLEndpoint.mongo.drop();
  }

  public GraphQL getGraph() {
    GraphQLSchema schema = GraphQLEndpoint.buildSchema();

    return GraphQL.newGraphQL(schema).build();
  }

  public ExecutionResult executeQuery(String query, AuthContext ctx) {
    return getGraph().execute(query, ctx);
  }

  public ExecutionResult executeQuery(String query) {
    return getGraph().execute(query);
  }

  public AuthContext forceAuth(User user) {
    return new AuthContext(user, null, null);
  }

  /*
  Create a user in the database.
  The returned user will contain an ID.
   */
  public User createUser(User user) {
    return GraphQLEndpoint.userRepository.saveUser(user);
  }

  public User createUser() {
    return GraphQLEndpoint.userRepository.saveUser(new User("test", "test", false, "male", 30));
  }

  public User createDoctor() {
    return GraphQLEndpoint.userRepository.saveUser(new User("doctor", "doctor", true, "male", 32));
  }

  public AuthContext forceAuth(String username) {
    User user = GraphQLEndpoint.userRepository.findByUsername(username);
    return forceAuth(user);
  }
}