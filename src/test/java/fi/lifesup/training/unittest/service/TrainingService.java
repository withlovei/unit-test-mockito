package fi.lifesup.training.unittest.service;

import org.springframework.stereotype.Service;

import fi.lifesup.training.unittest.domain.User;

@Service
public class TrainingService {

	
	public void changeData(User user){
		if(user != null){
			user.setCreatedBy("ky.pham");
			
		}
	}
	
}
