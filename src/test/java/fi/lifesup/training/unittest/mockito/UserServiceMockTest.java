package fi.lifesup.training.unittest.mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import fi.lifesup.training.unittest.domain.User;
import fi.lifesup.training.unittest.repository.AuthorityRepository;
import fi.lifesup.training.unittest.repository.UserRepository;
import fi.lifesup.training.unittest.service.TrainingService;
import fi.lifesup.training.unittest.service.UserService;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceMockTest {
	
	private UserService userService;
	
	@Mock
	private UserRepository userRepository;

	@Mock
    private PasswordEncoder passwordEncoder;
	
	@Mock
    private AuthorityRepository authorityRepository;

	@Mock
    private CacheManager cacheManager;
	
	@Mock
	private TrainingService trainingService;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		
		userService = new UserService();
		
		userRepository = mock(UserRepository.class);
		passwordEncoder = mock(PasswordEncoder.class);
		authorityRepository = mock(AuthorityRepository.class);
		cacheManager = mock(CacheManager.class);
		trainingService = mock(TrainingService.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
		
		ReflectionTestUtils.setField(userService, "userRepository", userRepository);
		ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);
		ReflectionTestUtils.setField(userService, "authorityRepository", authorityRepository);
		ReflectionTestUtils.setField(userService, "cacheManager", cacheManager);
		ReflectionTestUtils.setField(userService, "trainingService", trainingService);


		
	}
	
	@Test
	public void completePasswordResetTest(){
//		public Optional<User> completePasswordReset(String newPassword, String key) {
//		       log.debug("Reset user password for reset key {}", key);
//
//		       return userRepository.findOneByResetKey(key)
//		           .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
//		           .map(user -> {
//		                user.setPassword(passwordEncoder.encode(newPassword));
//		                user.setResetKey(null);
//		                user.setResetDate(null);
//		                cacheManager.getCache("users").evict(user.getLogin());
//		                return user;
//		           });
//		    }
		when(passwordEncoder.encode("6787676")).thenReturn("123456");
		
		when(userRepository.findOneByResetKey(anyString())).then(in -> {
			User user = new User();
			user.setResetDate(Instant.now().minusSeconds(1000));
			user.setResetKey("abc");
			Optional<User> option = Optional.of(user);
			return option;
		});
		
		Optional<User> afterUser = userService.completePasswordReset("6787676", "key");
		assertThat(afterUser.isPresent()).isTrue();
		assertThat(afterUser.get().getPassword()).isEqualTo("123456");
		assertThat(afterUser.get().getResetKey()).isEqualTo(null);
	}
	
	
	@Test
	public void completePasswordResetTest1(){

		when(passwordEncoder.encode(anyString())).thenReturn("123456");
		
		doAnswer(in->{
			User user = new User();
			user.setResetDate(Instant.now().minusSeconds(1000));
			user.setResetKey("sdasda");
			Optional<User> option = Optional.ofNullable(user);
			return option;
		}).when(userRepository).findOneByResetKey(anyString());
		
		Optional<User> afterUser = userService.completePasswordReset("12345", "key");
		assertThat(afterUser.isPresent()).isTrue();
		assertThat(afterUser.get().getPassword()).isEqualTo("123456");
		assertThat(afterUser.get().getResetKey()).isEqualTo(null);
	}
	
	@Test
	public void testSaveDb(){
		User user = new User();
		doReturn(user).when(userRepository).save(user);
		
		User testUser = userRepository.save(user);
		
		assertThat(testUser.getId()).isNull();
		
		doAnswer(in -> {
			user.setId(1000L);
			return user;
		}).when(userRepository).save(user);
		
		User testUser2 = userRepository.save(user);
		
		assertThat(testUser2.getId()).isEqualTo(1000L);
	}
	
	@Test
	public void testRepo(){
		
		User user = new User();
		
		doReturn(user).when(userRepository).findOne(1000L);
		
		User userTest = userRepository.findOne(1L);
		assertThat(userTest).isNull();
		user.setId(1000L);
		User userTest1 = userRepository.findOne(1000L);
		assertThat(userTest1).isNotNull();
		
		doAnswer(in -> {
			user.setId(1001L);
			return user;
		}).when(userRepository).save(any(User.class));
		
		User userTest2 = userRepository.save(user);
		
		assertThat(userTest2.getId()).isEqualTo(1001L);
		
		
	}
	
	@Test
	public void testRepo1(){
		
		User user = new User();
		
		
		
		doAnswer(in -> {
			user.setId(1000L);
			return user;
		}).when(userRepository).save(any(User.class));
		
		User userTest2 = userRepository.save(user);
		
		assertThat(userTest2.getId()).isEqualTo(1000L);
		
		User user1 = new User();
		
		User user2Test3 = userRepository.save(user1);
		
		assertThat(user2Test3.getId()).isEqualTo(1000L);
		
		
	}
	
	@Test
	public void anwserTest(){
		User user = new User();
		user.setCreatedBy("test system");
		user.setActivationKey("abc");
		userService.updateUser(user);
		//trainingService.changeData(user);
				
		assertThat(user.getCreatedBy()).isEqualTo("ky.pham");
		
		
	}

}
