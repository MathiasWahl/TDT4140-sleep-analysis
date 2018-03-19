package tdt4140.gr1816.app.api.resolvers;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import graphql.GraphQLException;
import graphql.schema.DataFetchingEnvironment;
import tdt4140.gr1816.app.api.DataAccessRequestRepository;
import tdt4140.gr1816.app.api.SleepRepository;
import tdt4140.gr1816.app.api.StepsRepository;
import tdt4140.gr1816.app.api.UserRepository;
import tdt4140.gr1816.app.api.auth.AuthContext;
import tdt4140.gr1816.app.api.auth.AuthData;
import tdt4140.gr1816.app.api.types.DataAccessRequest;
import tdt4140.gr1816.app.api.types.DataAccessRequestStatus;
import tdt4140.gr1816.app.api.types.SigninPayload;
import tdt4140.gr1816.app.api.types.Sleep;
import tdt4140.gr1816.app.api.types.Steps;
import tdt4140.gr1816.app.api.types.User;

public class Mutation implements GraphQLRootResolver {

  private final UserRepository userRepository;
  private final SleepRepository sleepRepository;
  private final StepsRepository stepsRepository;
  private final DataAccessRequestRepository dataAccessRequestRepository;

  public Mutation(
      UserRepository userRepository,
      SleepRepository sleepRepository,
      StepsRepository stepsRepository,
      DataAccessRequestRepository dataAccessRequestRepository) {
    this.userRepository = userRepository;
    this.sleepRepository = sleepRepository;
    this.stepsRepository = stepsRepository;
    this.dataAccessRequestRepository = dataAccessRequestRepository;
  }

  public User createUser(AuthData auth, boolean isDoctor, String gender, int age) {
    User newUser = new User(auth.getUsername(), auth.getPassword(), isDoctor, gender, age);
    return userRepository.saveUser(newUser);
  }

  public boolean deleteUser(AuthData auth) {
    User user = userRepository.findByUsername(auth.getUsername());
    if (user == null) {
      throw new GraphQLException("Nonexistent username");
    }

    if (user.getPassword().equals(auth.getPassword())) {
      boolean acknowledged = userRepository.deleteUser(user);
      return acknowledged;
    }
    throw new GraphQLException("Invalid credentials");
  }

  public SigninPayload signinUser(AuthData auth) throws IllegalAccessException {
    User user = userRepository.findByUsername(auth.getUsername());
    if (user == null) {
      throw new GraphQLException("Invalid user");
    }

    if (user.getPassword().equals(auth.getPassword())) {
      return new SigninPayload(user.getId(), user);
    }
    throw new GraphQLException("Invalid credentials");
  }

  public Sleep createSleep(String userId, String date, int duration, int efficiency) {
    Sleep newSleep = new Sleep(userId, date, duration, efficiency);
    return sleepRepository.saveSleep(newSleep);
  }

  public Boolean deleteSleep(String sleepId) {
    Sleep sleep = sleepRepository.findById(sleepId);
    if (sleep == null) {
      throw new GraphQLException("Invalid sleep id");
    }
    return sleepRepository.deleteSleep(sleep);
  }

  public Steps createSteps(String userId, String date, int steps) {
    Steps newSteps = new Steps(userId, date, steps);
    return stepsRepository.saveSteps(newSteps);
  }

  public Boolean deleteSteps(String stepsId) {
    Steps steps = stepsRepository.findById(stepsId);
    if (steps == null) {
      throw new GraphQLException("Invalid steps id");
    }
    return stepsRepository.deleteSteps(steps);
  }

  public DataAccessRequest requestDataAccess(String dataOwnerId, DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      throw new GraphQLException("Invalid user");
    }
    if (!user.isDoctor()) {
      throw new GraphQLException("User must be a doctor....");
    }
    return dataAccessRequestRepository.createDataRequest(dataOwnerId, user);
  }

  public DataAccessRequest answerDataAccessRequest(
      String dataAccessRequestId, DataAccessRequestStatus status, DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    return dataAccessRequestRepository.answerDataAccessRequest(dataAccessRequestId, status, user);
  }
}
