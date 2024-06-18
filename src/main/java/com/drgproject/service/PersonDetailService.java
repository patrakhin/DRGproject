package com.drgproject.service;

import com.drgproject.entity.User;
import com.drgproject.repository.PeopleRepository;
import com.drgproject.security.PersonDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailService implements UserDetailsService {

    private final PeopleRepository peopleRepository;

    public PersonDetailService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = peopleRepository.findUserByFio(username);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("User not found!");
        }
        return new PersonDetails(user.get());
    }
}
