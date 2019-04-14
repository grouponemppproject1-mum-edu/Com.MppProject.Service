package com.MppProject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.MppProject.models.*;
import com.MppProject.repositories.*;

@Component
//  A Validator's for application-specific objects.
/**
 * this class will implement and defines the Validator's methods of supports and  validate method
 */
public class UserValidator implements Validator {
	// giving a task to userRepository
    @Autowired
    UserRepository userRepository;
    
    /**
     *the supports method is an abstract method in Validator's class, 
     *implemented in this class to compare if the given user isAssignabeleFrom(clazz)  where the clazz 
     *is may be user or super class of user or interface of a user.
     */
    public boolean supports(Class<?> clazz){
        return User.class.isAssignableFrom(clazz);
    }

    /**
     *  this method validate is the implementation of Validate interface which validates the object received,
     *  and provides any resulting validation error.
     * 
     */
   
    public void validate(Object target, Errors errors){
    	
        User user = (User) target;// casting to User  because the we received the object obj.     
        
        /**
         * the following Static methods of ValidationUtils will provide the given error if value is empty
          */
        //Reject the given field with the given error code if the value is empty.->(name)(email)(password)/
        //value = errors.getFieldValue(field); field = name
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "user.name.empty");;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "user.email.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "user.password.empty");
        
        /**
        * the following Strings are the fields of User
        * due to some regulations of group discussion this methods will throws errors declared in Org.springFramework.validation
        *  Example Error error;then errors.rejectValue("String Name","String "error-code")
        *   
        */
        
        String email = user.getEmail();// getting email from user        
        String name = user.getName();// getting name of the user       
        String password = user.getPassword(); // getting users password       
        String authority = user.getAuthority(); // getting the authority string from user
        
        // counts character in email: Error reject value Stores and exposes information about data-binding and validation
        if(userRepository.countByEmail(email)>0){
            errors.rejectValue("email","user.email.duplicate");
        }
        // counts character by name, Error reject value Stores and exposes information about data-binding and validation
        if(userRepository.countByName(name)>0){
            errors.rejectValue("name","user.name.duplicate");
        }
        // getting password length and, Error reject value Stores and exposes information about data-binding and validation
        if(password.length() < 5){
            errors.rejectValue("password","user.password.tooShort");
        }
        // authority should not be null  or authority length should not be<1 otherwise
        //Error reject value Stores and exposes information about data-binding and validation
        if(authority == null || authority.length()<1){
            errors.rejectValue("authority","user.authority.notChoose");
        }
    }
}
