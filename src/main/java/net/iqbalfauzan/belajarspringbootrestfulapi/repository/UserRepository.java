package net.iqbalfauzan.belajarspringbootrestfulapi.repository;

import net.iqbalfauzan.belajarspringbootrestfulapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by IqbalMF on 2024.
 * Package net.iqbalfauzan.belajarspringbootrestfulapi.repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
