package com.bezkoder.springjwt.services;

import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.models.VerificationToken;
import com.bezkoder.springjwt.payload.request.SignupRequest;

public interface IUserService {

    User registerNewUserAccount(SignupRequest signUpRequest);

    User getUser(String verificationToken);

    void saveRegisteredUser(User user);

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);
}
