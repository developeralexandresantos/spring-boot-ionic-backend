package br.com.developeralexandresantos.cursomc.services;

import br.com.developeralexandresantos.cursomc.domain.Cliente;
import br.com.developeralexandresantos.cursomc.repositories.ClienteRepository;
import br.com.developeralexandresantos.cursomc.security.UserSS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final long serialVersionUID = 1L;

    @Autowired
    ClienteRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Cliente cli = repo.findByEmail(email);
        if(cli == null ) {
            throw new UsernameNotFoundException(email);
        }
        return new UserSS(cli.getId(), cli.getEmail(), cli.getSenha(),cli.getPerfis());
    }
}
